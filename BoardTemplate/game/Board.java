// ===== Board state =====
	package game;
	
    import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;





public final class Board {
        public Piece[] board = new Piece[64];
        public Color sideToMove = Color.WHITE;
        // castling rights bit mask: 1=K,2=Q,4=k,8=q (white short/long, black short/long)
        public int castling = 0b1111;
        // en passant target square (-1 if none). If a double pawn move happened, this is the square behind it.
        public int epSquare = -1;
        public int halfmoveClock = 0;
        public int fullmoveNumber = 1;

        // Undo stack
        public static final class State {
            final int castling, epSquare, halfmoveClock;
            final Piece captured;
            public State(int c,int ep,int hm,Piece cap){ this.castling=c; this.epSquare=ep; this.halfmoveClock=hm; this.captured=cap; }
        }
        private final Deque<State> states = new ArrayDeque<>();

        public Board(){}

        // --- FEN loader (simple, supports standard fields) ---
        public static Board fromFEN(String fen){
            String[] parts = fen.trim().split("\\s+");
            if (parts.length < 4) throw new IllegalArgumentException("Bad FEN");
            Board b = new Board();
            String[] ranks = parts[0].split("/");
            if (ranks.length!=8) throw new IllegalArgumentException("Bad FEN ranks");

            for (int r=7; r>=0; r--){
                int idx = 7-r; // FEN starts from rank 8 down to 1
                String row = ranks[idx];
                int f=0;
                for (char c: row.toCharArray()){
                    if (Character.isDigit(c)){
                        f += (c - '0');
                    } else {
                        Color col = Character.isUpperCase(c)? Color.WHITE: Color.BLACK;
                        PieceType pt;
                        switch (Character.toLowerCase(c)){
                            case 'k': pt=PieceType.K; break;
                            case 'q': pt=PieceType.Q; break;
                            case 'r': pt=PieceType.R; break;
                            case 'b': pt=PieceType.B; break;
                            case 'n': pt=PieceType.N; break;
                            case 'p': pt=PieceType.P; break;
                            default: throw new IllegalArgumentException("Bad piece in FEN");
                        }
                        b.board[sq(f, r)] = new Piece(pt, col);
                        f++;
                    }
                }
                if (f!=8) throw new IllegalArgumentException("Bad FEN row width");
            }
            b.sideToMove = parts[1].equals("w")? Color.WHITE: Color.BLACK;
            b.castling = 0;
            if (!parts[2].equals("-")){
                if (parts[2].contains("K")) b.castling |= 1;
                if (parts[2].contains("Q")) b.castling |= 2;
                if (parts[2].contains("k")) b.castling |= 4;
                if (parts[2].contains("q")) b.castling |= 8;
            }
            b.epSquare = parts[3].equals("-")? -1 : parseSq(parts[3]);
            if (parts.length>=5) b.halfmoveClock = Integer.parseInt(parts[4]);
            if (parts.length>=6) b.fullmoveNumber = Integer.parseInt(parts[5]);
            return b;
        }

        public String toFEN(){
            StringBuilder sb = new StringBuilder();
            for (int r=7; r>=0; r--){
                int empty=0;
                for (int f=0; f<8; f++){
                    Piece p = board[sq(f,r)];
                    if (p==null){ empty++; }
                    else {
                        if (empty>0){ sb.append(empty); empty=0; }
                        sb.append(pieceChar(p));
                    }
                }
                if (empty>0) sb.append(empty);
                if (r>0) sb.append('/');
            }
            sb.append(' ').append(sideToMove==Color.WHITE?'w':'b').append(' ');
            String cs="";
            if ((castling&1)!=0) cs+="K";
            if ((castling&2)!=0) cs+="Q";
            if ((castling&4)!=0) cs+="k";
            if ((castling&8)!=0) cs+="q";
            sb.append(cs.isEmpty()? "-": cs);
            sb.append(' ').append(epSquare==-1? "-": sqName(epSquare));
            sb.append(' ').append(halfmoveClock);
            sb.append(' ').append(fullmoveNumber);
            return sb.toString();
        }

        private static int parseSq(String s){
            if (s.length()!=2) throw new IllegalArgumentException("Bad square");
            int f = "abcdefgh".indexOf(s.charAt(0));
            int r = s.charAt(1)-'1';
            if (f<0 || r<0 || r>7) throw new IllegalArgumentException("Bad square");
            return sq(f,r);
        }
        private static char pieceChar(Piece p){
            char c;
            switch (p.type){
                case K: c='k'; break; case Q: c='q'; break; case R: c='r'; break;
                case B: c='b'; break; case N: c='n'; break; default: c='p';
            }
            return p.color==Color.WHITE? Character.toUpperCase(c): c;
        }

        // ===== Move generation (legal) =====
        public List<Move> legalMoves(){
            List<Move> res = new ArrayList<>();
            List<Move> pseudo = pseudoLegalMoves();
            for (Move m: pseudo){
                if (makesLegal(m)) res.add(m);
            }
            return res;
        }

        private List<Move> pseudoLegalMoves(){
            List<Move> ms = new ArrayList<>();
            for (int s=0; s<64; s++){
                Piece p = board[s];
                if (p==null || p.color!=sideToMove) continue;
                switch (p.type){
                    case P: genPawn(s, p.color, ms); break;
                    case N: genKnight(s, p.color, ms); break;
                    case B: genSlide(s, p.color, ms, new int[]{+9,+7,-9,-7}); break;
                    case R: genSlide(s, p.color, ms, new int[]{+8,-8,+1,-1}); break;
                    case Q: genSlide(s, p.color, ms, new int[]{+9,+7,-9,-7,+8,-8,+1,-1}); break;
                    case K: genKing(s, p.color, ms); break;
                }
            }
            genCastling(ms);
            return ms;
        }

        public static int sq(int file, int rank){ return rank*8 + file; }
        public static int fileOf(int sq){ return sq & 7; }
        public static int rankOf(int sq){ return sq >>> 3; }
        public static boolean onBoard(int file, int rank){ return file>=0 && file<8 && rank>=0 && rank<8; }
        public static String sqName(int s){ return "abcdefgh".charAt(fileOf(s)) + Integer.toString(rankOf(s)+1); }
        
        
        
        private void genPawn(int s, Color c, List<Move> ms){
            int rank = rankOf(s), file=fileOf(s);
            int dir = (c==Color.WHITE)? +1 : -1;
            int startRank = (c==Color.WHITE)? 1 : 6;
            int promoRank = (c==Color.WHITE)? 6 : 1;
            int oneRank = rank + dir;

            // quiet forward
            if (onBoard(file, oneRank) && board[sq(file, oneRank)]==null){
                if (rank==promoRank){
                    for (PieceType pt: new PieceType[]{PieceType.Q,PieceType.R,PieceType.B,PieceType.N})
                        ms.add(new Move(s, sq(file, oneRank), pt, false, false));
                } else {
                    ms.add(new Move(s, sq(file, oneRank)));
                    // double
                    if (rank==startRank){
                        int twoRank = rank + 2*dir;
                        if (board[sq(file,twoRank)]==null) ms.add(new Move(s, sq(file, twoRank)));
                    }
                }
            }
            // captures (including promotion)
            for (int df: new int[]{-1,+1}){
                int nf=file+df, nr=rank+dir;
                if (!onBoard(nf,nr)) continue;
                int to = sq(nf,nr);
                if (board[to]!=null && board[to].color!=c){
                    if (rank==promoRank){
                        for (PieceType pt: new PieceType[]{PieceType.Q,PieceType.R,PieceType.B,PieceType.N})
                            ms.add(new Move(s,to,pt,false,false));
                    } else ms.add(new Move(s,to));
                }
            }
            // en passant
            if (epSquare!=-1){
                int epF=fileOf(epSquare);
                if (rank + dir == rankOf(epSquare) && Math.abs(epF - file)==1){
                    ms.add(new Move(s, epSquare, null, true, false));
                }
            }
        }

        private void genKnight(int s, Color c, List<Move> ms){
            int[][] deltas = {{+1,+2},{+2,+1},{+2,-1},{+1,-2},{-1,-2},{-2,-1},{-2,+1},{-1,+2}};
            int f=fileOf(s), r=rankOf(s);
            for (int[] d: deltas){
                int nf=f+d[0], nr=r+d[1];
                if (!onBoard(nf,nr)) continue;
                int to = sq(nf,nr);
                if (board[to]==null || board[to].color!=c) ms.add(new Move(s,to));
            }
        }

        private void genSlide(int s, Color c, List<Move> ms, int[] dirs){
            for (int d: dirs){
                int cur=s;
                while (true){
                    int nf=fileOf(cur)+dx(d), nr=rankOf(cur)+dy(d);
                    if (!onBoard(nf,nr)) break;
                    int to = sq(nf,nr);
                    if (board[to]==null){
                        ms.add(new Move(s,to));
                        cur = to;
                    } else {
                        if (board[to].color!=c) ms.add(new Move(s,to));
                        break;
                    }
                }
            }
        }

        private void genKing(int s, Color c, List<Move> ms){
            for (int df=-1; df<=1; df++)
                for (int dr=-1; dr<=1; dr++){
                    if (df==0 && dr==0) continue;
                    int nf=fileOf(s)+df, nr=rankOf(s)+dr;
                    if (!onBoard(nf,nr)) continue;
                    int to = sq(nf,nr);
                    if (board[to]==null || board[to].color!=c) ms.add(new Move(s,to));
                }
        }

        private void genCastling(List<Move> ms){
            // squares & rights depend on side
            Color c = sideToMove;
            int rank = (c==Color.WHITE)? 0 : 7;
            int kingSq = sq(4,rank);
            Piece k = board[kingSq];
            if (k==null || k.type!=PieceType.K || k.color!=c) return;
            boolean inCheck = isSquareAttacked(kingSq, c.opp());

            if (c==Color.WHITE){
                // K-side: rights 1, squares f1,g1 empty & not attacked; rook at h1
                if ((castling & 1)!=0 &&
                        board[sq(5,0)]==null && board[sq(6,0)]==null &&
                        !inCheck && !isSquareAttacked(sq(5,0), Color.BLACK) && !isSquareAttacked(sq(6,0), Color.BLACK) &&
                        rookPresentAt(sq(7,0), Color.WHITE)){
                    ms.add(new Move(kingSq, sq(6,0), null, false, true));
                }
                // Q-side: rights 2, squares b1,c1,d1 empty & c1,d1 not attacked; rook at a1
                if ((castling & 2)!=0 &&
                        board[sq(1,0)]==null && board[sq(2,0)]==null && board[sq(3,0)]==null &&
                        !inCheck && !isSquareAttacked(sq(3,0), Color.BLACK) && !isSquareAttacked(sq(2,0), Color.BLACK) &&
                        rookPresentAt(sq(0,0), Color.WHITE)){
                    ms.add(new Move(kingSq, sq(2,0), null, false, true));
                }
            } else {
                if ((castling & 4)!=0 &&
                        board[sq(5,7)]==null && board[sq(6,7)]==null &&
                        !inCheck && !isSquareAttacked(sq(5,7), Color.WHITE) && !isSquareAttacked(sq(6,7), Color.WHITE) &&
                        rookPresentAt(sq(7,7), Color.BLACK)){
                    ms.add(new Move(kingSq, sq(6,7), null, false, true));
                }
                if ((castling & 8)!=0 &&
                        board[sq(1,7)]==null && board[sq(2,7)]==null && board[sq(3,7)]==null &&
                        !inCheck && !isSquareAttacked(sq(3,7), Color.WHITE) && !isSquareAttacked(sq(2,7), Color.WHITE) &&
                        rookPresentAt(sq(0,7), Color.BLACK)){
                    ms.add(new Move(kingSq, sq(2,7), null, false, true));
                }
            }
        }

        private boolean rookPresentAt(int sq, Color c){
            Piece p = board[sq];
            return p!=null && p.type==PieceType.R && p.color==c;
        }

        private static int dx(int dir){
            if (dir==+1 || dir==-1) return dir;              // horizontal
            if (dir==+9 || dir==-7 || dir==+8 || dir==-8) return 0;
            if (dir==+7 || dir==-9) return -1;
            return 0;
        }
        private static int dy(int dir){
            if (dir==+8) return +1; if (dir==-8) return -1;
            if (dir==+9) return +1; if (dir==-9) return -1;
            if (dir==+7) return +1; if (dir==-7) return -1;
            return 0;
        }

        // ===== Legality filter (king safety) =====
        private boolean makesLegal(Move m){
            State st = makeMove(m);
            boolean legal = !inCheck(sideToMove.opp());
            unmakeMove(m, st);
            return legal;
        }

        public boolean inCheck(Color side){
            int kingSq = -1;
            for (int i=0;i<64;i++){
                Piece p=board[i];
                if (p!=null && p.color==side && p.type==PieceType.K){ kingSq=i; break; }
            }
            return isSquareAttacked(kingSq, side.opp());
        }

        public boolean isSquareAttacked(int target, Color by){
            // Pawn
            int r=rankOf(target), f=fileOf(target);
            int dir = (by==Color.WHITE)? +1 : -1;
            for (int df: new int[]{-1,+1}){
                int nf=f+df, nr=r+dir;
                if (onBoard(nf,nr)){
                    Piece p = board[sq(nf,nr)];
                    if (p!=null && p.color==by && p.type==PieceType.P) return true;
                }
            }
            // Knight
            int[][] nd={{+1,+2},{+2,+1},{+2,-1},{+1,-2},{-1,-2},{-2,-1},{-2,+1},{-1,+2}};
            for (int[] d: nd){
                int nf=f+d[0], nr=r+d[1];
                if (!onBoard(nf,nr)) continue;
                Piece p = board[sq(nf,nr)];
                if (p!=null && p.color==by && p.type==PieceType.N) return true;
            }
            // King
            for (int df=-1; df<=1; df++)
                for (int dr=-1; dr<=1; dr++){
                    if (df==0 && dr==0) continue;
                    int nf=f+df, nr=r+dr;
                    if (!onBoard(nf,nr)) continue;
                    Piece p = board[sq(nf,nr)];
                    if (p!=null && p.color==by && p.type==PieceType.K) return true;
                }
            // Sliders
            if (rayAttack(target, by, new int[]{+8,-8,+1,-1}, PieceType.R, PieceType.Q)) return true;
            if (rayAttack(target, by, new int[]{+9,+7,-9,-7}, PieceType.B, PieceType.Q)) return true;
            return false;
        }

        private boolean rayAttack(int target, Color by, int[] dirs, PieceType needed, PieceType alt){
            for (int d: dirs){
                int cur=target;
                while (true){
                    int nf=fileOf(cur)+dx(d), nr=rankOf(cur)+dy(d);
                    if (!onBoard(nf,nr)) break;
                    cur = sq(nf,nr);
                    Piece p = board[cur];
                    if (p==null) continue;
                    if (p.color==by && (p.type==needed || p.type==alt)) return true;
                    break;
                }
            }
            return false;
        }

        // ===== Make / unmake moves =====
        public State makeMove(Move m){
            Piece moving = board[m.from];
            Piece captured = m.isEnPassant? board[epCapturedSquare(m)] : board[m.to];
            states.push(new State(castling, epSquare, halfmoveClock, captured));

            // update halfmove clock
            if (moving.type==PieceType.P || captured!=null) halfmoveClock=0;
            else halfmoveClock++;

            // clear EP by default
            epSquare = -1;

            // move piece
            board[m.to] = moving;
            board[m.from] = null;

            // promotion
            if (m.promotion!=null) board[m.to] = new Piece(m.promotion, moving.color);

            // en passant capture
            if (m.isEnPassant) board[epCapturedSquare(m)] = null;

            // castling rook move
            if (m.isCastle){
                if (m.to == sq(6,0)) { // white O-O
                    board[sq(5,0)] = board[sq(7,0)]; board[sq(7,0)] = null;
                } else if (m.to == sq(2,0)) { // white O-O-O
                    board[sq(3,0)] = board[sq(0,0)]; board[sq(0,0)] = null;
                } else if (m.to == sq(6,7)) { // black O-O
                    board[sq(5,7)] = board[sq(7,7)]; board[sq(7,7)] = null;
                } else if (m.to == sq(2,7)) { // black O-O-O
                    board[sq(3,7)] = board[sq(0,7)]; board[sq(0,7)] = null;
                }
            }

            // set EP square if double pawn push
            if (moving.type==PieceType.P){
                int fromR=rankOf(m.from), toR=rankOf(m.to);
                if (Math.abs(toR-fromR)==2){
                    int epR=(fromR+toR)/2;
                    epSquare = sq(fileOf(m.from), epR);
                }
            }

            // update castling rights if king/rook moved or rook captured
            updateCastlingRights(m, moving, captured);

            // side to move & fullmove number
            if (sideToMove==Color.BLACK) fullmoveNumber++;
            sideToMove = sideToMove.opp();

            return states.peek();
        }

        public void unmakeMove(Move m, State st){
            sideToMove = sideToMove.opp();
            if (sideToMove==Color.BLACK) fullmoveNumber--;

            Piece moving = board[m.to];
            // undo castling rook
            if (m.isCastle){
                if (m.to == sq(6,0)) { // white O-O
                    board[sq(7,0)] = board[sq(5,0)]; board[sq(5,0)] = null;
                } else if (m.to == sq(2,0)) { // white O-O-O
                    board[sq(0,0)] = board[sq(3,0)]; board[sq(3,0)] = null;
                } else if (m.to == sq(6,7)) { // black O-O
                    board[sq(7,7)] = board[sq(5,7)]; board[sq(5,7)] = null;
                } else if (m.to == sq(2,7)) { // black O-O-O
                    board[sq(0,7)] = board[sq(3,7)]; board[sq(3,7)] = null;
                }
            }
            // undo promotion
            if (m.promotion!=null) moving = new Piece(PieceType.P, moving.color);

            board[m.from] = moving;
            board[m.to] = null;

            // restore captured
            if (m.isEnPassant){
                board[epCapturedSquare(m)] = st.captured;
            } else if (st.captured!=null){
                board[m.to] = st.captured;
            }

            // restore state
            this.castling = st.castling;
            this.epSquare = st.epSquare;
            this.halfmoveClock = st.halfmoveClock;

            states.pop();
        }

        private int epCapturedSquare(Move m){
            // pawn captured is behind the EP square relative to mover
            int dir = (sideToMove==Color.WHITE)? -1 : +1; // because sideToMove has already been flipped in makeMove
            int r = rankOf(m.to)+dir, f=fileOf(m.to);
            return sq(f,r);
        }

        private void updateCastlingRights(Move m, Piece moving, Piece captured){
            // If a king moves, lose both rights; if rook moves/captured from corner, lose that side
            // White king/rooks
            if (moving.type==PieceType.K){
                if (moving.color==Color.WHITE) castling &= ~(1|2);
                else castling &= ~(4|8);
            }
            if (moving.type==PieceType.R){
                if (moving.color==Color.WHITE){
                    if (m.from==sq(0,0)) castling &= ~2; // a1 rook
                    if (m.from==sq(7,0)) castling &= ~1; // h1 rook
                } else {
                    if (m.from==sq(0,7)) castling &= ~8; // a8 rook
                    if (m.from==sq(7,7)) castling &= ~4; // h8 rook
                }
            }
            if (captured!=null && captured.type==PieceType.R){
                if (m.to==sq(0,0)) castling &= ~2;
                if (m.to==sq(7,0)) castling &= ~1;
                if (m.to==sq(0,7)) castling &= ~8;
                if (m.to==sq(7,7)) castling &= ~4;
            }
        }

        // ===== Game status =====
        public boolean hasLegalMove(){ return !legalMoves().isEmpty(); }
        public boolean isCheckmate(){ return inCheck(sideToMove) && !hasLegalMove(); }
        public boolean isStalemate(){ return !inCheck(sideToMove) && !hasLegalMove(); }
        public boolean isDrawByFiftyMove(){ return halfmoveClock>=100; }
        // (threefold repetition, insufficient material, etc. can be added later)
    }

    