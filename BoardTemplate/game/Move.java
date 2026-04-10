package game;


public final class Move {
        public final int from, to;
        public final PieceType promotion;  // null if not a promotion
        public final boolean isEnPassant;
        public final boolean isCastle;

        public Move(int from, int to){ this(from,to,null,false,false); }
        public Move(int from, int to, PieceType promo, boolean ep, boolean castle){
            this.from=from; this.to=to; this.promotion=promo; this.isEnPassant=ep; this.isCastle=castle;
        }
        public String toString(){
            String s = sqName(from)+"-"+sqName(to);
            if (promotion!=null) s += "="+promotion;
            if (isEnPassant) s += " e.p.";
            if (isCastle) s += " castle";
            return s;
        }
        
        
        public static int sq(int file, int rank){ return rank*8 + file; }
        public static int fileOf(int sq){ return sq & 7; }
        public static int rankOf(int sq){ return sq >>> 3; }
        public static boolean onBoard(int file, int rank){ return file>=0 && file<8 && rank>=0 && rank<8; }
        public static String sqName(int s){ return "abcdefgh".charAt(fileOf(s)) + Integer.toString(rankOf(s)+1); }
    }