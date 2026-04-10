package game;
import java.util.ArrayList;
import java.util.List;

import test4.LegalMoveLibrary;

public class BoardUtils {
	
	public static volatile ArrayList<tile> tiles;
	public static volatile boolean isWhiteTurn=true;

    static {
        if (tiles == null) {
            System.out.println("🧩 BoardUtils initializing default board...");
            create_a_eight_by_eight_of_tiles();
            populateBoard();
        }
    }
 // --- unified coordinate mapping (white at bottom) ---
    public static int toBoardRow(double normalizedY) {
        // (0,0) bottom-left; (7,7) top-right
        return 7 - (int)Math.round(normalizedY * 7);
    }

    public static int toBoardCol(double normalizedX) {
        return (int)Math.round(normalizedX * 7);
    }

    // --- Canonical indexing (row-major) ---
    public static int indexOf(int col, int row, int width, int height) {
        if (col < 0 || row < 0 || col >= width || row >= height) return -1;
        return row * width + col;
    }
public static boolean isWhitePiece(double[] v) {
	int sx = BoardUtils.toBoardCol(v[0]);
	 int sy = BoardUtils.toBoardRow(v[1]);
		
    if (sx < 0 || sy < 0 || sx >= 8 || sy >= 8)
        return false;

    char piece = BoardUtils.get(sx, sy);

    return piece >= 'A' && piece <= 'Z';
}

public static boolean isBlackPiece(double[] v) {
	int sx = BoardUtils.toBoardCol(v[0]);
 	 int sy = BoardUtils.toBoardRow(v[1]);
		
    if (sx < 0 || sy < 0 || sx >= 8 || sy >= 8)
        return false;

    char piece = BoardUtils.get(sx, sy);

    return piece >= 'a' && piece <= 'z';
}


    public static tile getTile(int col, int row, int width, int height) {
        int idx = indexOf(col, row, width, height);
        return (idx >= 0 && idx < width*height) ? tiles.get(idx) : null;
    }
    public static void create_a_eight_by_eight_of_tiles() {
		tiles = new ArrayList<tile>();

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				tiles.add(new tile(i, j));
			}
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print( i+""+j+" ");
			}
			System.out.println();
		}

    }
    
    public static void populateBoard() {
		   char[] whiteBackRank = {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'};
		    char[] blackBackRank = {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'};

		    for (tile t : tiles) {
		        int row = t.getRow();
		        int col = t.getColumn();

		        switch (row) {
		            case 7 -> t.setPiece(whiteBackRank[col]);  // bottom rank = white pieces
		            case 6 -> t.setPiece('P');                 // white pawns
		            case 1 -> t.setPiece('p');                 // black pawns
		            case 0 -> t.setPiece(blackBackRank[col]);  // top rank = black pieces
		            default -> t.setPiece(' ');                // empty squares
		        }
		    }
    }

    public static void setPieceAt( int x, int y, char piece) {
        tiles.stream()
            .filter(t -> t.getRow() == x && t.getColumn() == y)
            .findFirst()
            .ifPresent(t -> t.setPiece(piece));
    }
    // --- Normalization (center of cell) ---
    public static double norm(int i, int max) {
        return (i + 0.5) / (double) max;  // [0,1)
    }

    public static int deNorm(double v, int max) {
        int idx = (int) Math.floor(v * max);
        return Math.max(0, Math.min(idx, max - 1));
    }

    // --- One-time sanity check at startup or before a game ---
    public static void assertTileLayout(List<tile> tiles, int width, int height) {
        // Verify list order matches row-major and tile coords match their slot
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = indexOf(col, row, width, height);
                tile t = tiles.get(idx);
                if (t.getColumn() != col || t.getRow() != row) {
                    throw new IllegalStateException(String.format(
                        "Tile layout mismatch at idx=%d: expected (%d,%d) but tile reports (%d,%d)",
                        idx, col, row, t.getColumn(), t.getRow()));
                }
            }
        }
    }

    // Optional: repair a column-major list to row-major (one-time migration)
    public static void convertColumnMajorToRowMajor(List<tile> tiles, int width, int height) {
        // Only use if you discover the list is column-major (x*height + y)
        ArrayList<tile> copy = new ArrayList<>(tiles);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int rowMajor = indexOf(col, row, width, height);
                int colMajor = col * height + row;
                tiles.set(rowMajor, copy.get(colMajor));
            }
        }
    }

	public static ArrayList<tile> getTiles() {
		return tiles;
	}

	public static char get(int nx, int ny) {
		// TODO Auto-generated method stub
		return getTile(nx, ny, 8, 8).getPiece();
	}
	// --- simple tile helper (if you don't already have it) ---
	public static tile getTile(int x, int y, int width) {
	    if (tiles == null || x < 0 || x >= width || y < 0 || y >= width) return null;
	    return tiles.get(y * width + x);
	}

	// Overload (optional)
	public static tile getTile(int x, int y) { return getTile(x, y, 8); }

	// --- core empty check (reuse yours if it exists) ---
	private static boolean isEmpty(char p) { return p == '\0' || p == ' ' || p == '.'; }

	// --- move piece (used for real moves OR quick sims) ---
	public static void movePiece(int sx, int sy, int dx, int dy) {
	    tile from = getTile(sx, sy, 8, 8);
	    tile to = getTile(dx, dy, 8, 8);
	    if (from == null || to == null) return;
	    to.setPiece(from.getPiece());
	    from.setPiece(' ');
	}
	public static boolean moveLeavesKingInCheck(int sx, int sy, int dx, int dy, boolean isWhite) {
	    tile from = getTile(sx, sy, 8, 8);
	    tile to = getTile(dx, dy, 8, 8);
	    if (from == null || to == null) return false;

	    char movingPiece = from.getPiece();
	    char captured = to.getPiece();

	    // simulate move
	    to.setPiece(movingPiece);
	    from.setPiece(' ');

	    boolean inCheck = isKingInCheck(isWhite);

	    // revert
	    from.setPiece(movingPiece);
	    to.setPiece(captured);

	    return inCheck;
	}


	// --- simulation-friendly apply/undo pair ---
	public static char applyMove(int sx, int sy, int dx, int dy) {
	    tile from = getTile(sx, sy, 8);
	    tile to   = getTile(dx, dy, 8);
	    if (from == null || to == null) return '\0';

	    char moving   = from.getPiece();
	    char captured = to.getPiece();
	    if (isEmpty(moving)) return '\0';

	    to.setPiece(moving);
	    from.setPiece('\0');
	    return captured; // hand back what was on the dst so we can undo exactly
	}

	public static void undoMove(int sx, int sy, int dx, int dy, char captured) {
	    tile from = getTile(sx, sy, 8);
	    tile to   = getTile(dx, dy, 8);
	    if (from == null || to == null) return;

	    // piece currently at (dx,dy) is the mover
	    char mover = to.getPiece();
	    from.setPiece(mover);
	    to.setPiece(captured);
	}

	// --- color helpers (reuse yours if you already have them) ---
	public static boolean isWhite(char p) { return Character.isUpperCase(p); }
	public static boolean sameColor(char a, char b) {
	    if (isEmpty(a) || isEmpty(b)) return false;
	    return Character.isUpperCase(a) == Character.isUpperCase(b);
	}
	// === BoardUtils helpers: snapshot/restore & move ===

	/** Save only the piece array (keeps the same tiles list/reference alive). */
	public static char[] snapshotPieces() {
	    if (tiles == null) return null;
	    char[] snap = new char[tiles.size()];
	    for (int i = 0; i < tiles.size(); i++) {
	        snap[i] = tiles.get(i).getPiece();
	    }
	    return snap;
	}

	/** Restore only the piece array into the existing tiles. */
	public static void restorePieces(char[] snap) {
	    if (snap == null || tiles == null || snap.length != tiles.size()) return;
	    for (int i = 0; i < tiles.size(); i++) {
	        tiles.get(i).setPiece(snap[i]);
	    }
	}

	public static char[] cloneTilesPieces() { return snapshotPieces(); }
	public static void restoreFromClone(char[] pieces) { restorePieces(pieces); }


	// --- find-king + check detection for stalemate/checkmate logic ---
public static boolean isKingInCheck(boolean isWhite) {
    tile kingTile = null;
    for (tile t : tiles) {
        char p = t.getPiece();
        if (p == (isWhite ? 'K' : 'k')) {
            kingTile = t;
            break;
        }
    }
    if (kingTile == null) return false; // king captured already

    double[] kingPos = new double[]{
        kingTile.getColumn() / 7.0,
        kingTile.getRow() / 7.0,
        kingTile.getColumn() / 7.0,
        kingTile.getRow() / 7.0
    };

    // Scan all enemy moves—if any can hit the king, we’re in check
    for (tile t : tiles) {
        char p = t.getPiece();
        if (p == ' ' || p == '.' || p == '\0') continue;
        boolean enemy = Character.isUpperCase(p) != isWhite;
        if (!enemy) continue;

        int fx = t.getColumn(), fy = t.getRow();
        List<int[]> candidates = new ArrayList<>();
        switch (Character.toLowerCase(p)) {
            case 'p' -> candidates.addAll(LegalMoveLibrary.genPawnMoves(p, fx, fy));
            case 'n' -> candidates.addAll(LegalMoveLibrary.genKnightMoves(fx, fy));
            case 'b' -> candidates.addAll(LegalMoveLibrary.genSlidingMoves(fx, fy,
                new int[][]{{1,1},{1,-1},{-1,1},{-1,-1}}));
            case 'r' -> candidates.addAll(LegalMoveLibrary.genSlidingMoves(fx, fy,
                new int[][]{{1,0},{-1,0},{0,1},{0,-1}}));
            case 'q' -> candidates.addAll(LegalMoveLibrary.genSlidingMoves(fx, fy,
                new int[][]{{1,0},{-1,0},{0,1},{0,-1},{1,1},{1,-1},{-1,1},{-1,-1}}));
            case 'k' -> candidates.addAll(LegalMoveLibrary.genKingMoves(fx, fy));
        }

        for (int[] c : candidates) {
            if (c[0] == kingTile.getColumn() && c[1] == kingTile.getRow()) {
            	GameStats.registerCheck(BoardUtils.isWhiteTurn);

                return true;
            }
        }
    }
    return false;
}


}
