package game;

import java.util.Arrays;
import java.util.List;

public class VectorMoveValidator {

	private static int width = 8;
	private static int height = 8;
	private final static boolean whiteMovesUp = true;

	public VectorMoveValidator( int width, int height) {

		VectorMoveValidator.width = width;
		VectorMoveValidator.height = height;
	}

	private static int toIndex(double v, int max, boolean flipY) {
	    // Clamp into [0,1)
	    if (Double.isNaN(v)) v = 0.0;
	    v = Math.max(0.0, Math.min(0.9999, v));

	    // Convert normalized coordinate (0–1) to board index (0–7)
	    int idx = (int)Math.floor(v * max);
	    if (idx >= max) idx = max - 1;
	    if (idx < 0) idx = 0;

	    return idx;
	}


	// --- Core legality API ---
	public static boolean isLegalMove(double[] vector, boolean isMapped) {
		int sx = 0;
		int sy = 0;
		int dx = 0;
		int dy = 0;
		if (!isMapped) {

			// interpret incoming values as already normalized (0–1)
			sx = BoardUtils.toBoardCol(vector[0]);
       	 sy = BoardUtils.toBoardRow(vector[1]);
       	 dx = BoardUtils.toBoardCol(vector[2]);
       	 dy = BoardUtils.toBoardRow(vector[3]);
			
			sy = 7 - (int)(sy);
			 dy = 7 - (int)(dy );
		} else {

			sx = (int) vector[0];
			sy = (int) vector[1];
			dx = (int) vector[2];
			dy = (int) vector[3];
		}

		

		 char src = get(sx, sy);
		    char dst = get(dx, dy);

		    if (isEmpty(src))
		        return false;
		    if (!isEmpty(dst) && sameColor(src, dst))
		        return false;

		    // Basic movement pattern check
		    boolean patternOk = switch (Character.toLowerCase(src)) {
		        case 'p' -> validPawn(src, sx, sy, dx, dy);
		        case 'n' -> validKnight(sx, sy, dx, dy);
		        case 'b' -> validBishop(sx, sy, dx, dy);
		        case 'r' -> validRook(sx, sy, dx, dy);
		        case 'q' -> validQueen(sx, sy, dx, dy);
		        case 'k' -> validKing(sx, sy, dx, dy);
		        default  -> false;
		    };
		    if (!patternOk) return false;

		    // Prevent moving into or staying in check
		    tile from = BoardUtils.getTile(sx, sy, 8);
		    tile to   = BoardUtils.getTile(dx, dy, 8);
		    if (from == null || to == null) return false;

		    // simulate move
		    char piece = from.getPiece();
		    char captured = to.getPiece();
		    to.setPiece(piece);
		    from.setPiece('\0');
		    boolean stillInCheck = isKingInCheck(Character.isUpperCase(piece));
		    // rollback
		    from.setPiece(piece);
		    to.setPiece(captured);

		    if (stillInCheck)
		        return false;

		    return true;
	}
	public static char legalMoveSrc(double[] vector, boolean isMapped) {
		int sx = 0;
		int sy = 0;
		if (!isMapped) {
			sx = BoardUtils.toBoardCol(vector[0]);
	       	 sy = BoardUtils.toBoardRow(vector[1]);
				
				sy = 7 - (int)(sy);
			
		} else {
			sx = (int) vector[0];
			sy = (int) vector[1];
		}
		char src = get(sx, sy);
		if (isEmpty(src))
			return ' ';
		else
			return src;
	}


	public static boolean isCapture(double[] vector, boolean isMapped) {
	    if (vector == null || vector.length < 4 || BoardUtils.tiles == null) 
	        return false;

	    int sx, sy, dx, dy;

	    if (!isMapped) {
	        // Normalized → board coordinates (0–7)
	    	sx = BoardUtils.toBoardCol(vector[0]);
	       	 sy = BoardUtils.toBoardRow(vector[1]);
	       	 dx = BoardUtils.toBoardCol(vector[2]);
	       	 dy = BoardUtils.toBoardRow(vector[3]);
				
				sy = 7 - (int)(sy);
				 dy = 7 - (int)(dy );
	    } else {
	        // Already integer indices
	        sx = (int) vector[0];
	        sy = (int) vector[1];
	        dx = (int) vector[2];
	        dy = (int) vector[3];
	    }

	    tile from = BoardUtils.getTile(sx, sy, 8,8);
	    tile to   = BoardUtils.getTile(dx, dy, 8,8);

	    if (from == null || to == null) return false;

	    char dst = to.getPiece();
	    return !(dst == '.' || dst == ' ' || dst == '\0');
	}


	/** Checks if the move would leave the moving side’s king in check. */
	public static boolean leavesSelfInCheck(tile from, tile to) {
		char movingPiece = from.getPiece();
		boolean white = Character.isUpperCase(movingPiece);

		// simulate move
		char oldTo = to.getPiece();
		to.setPiece(movingPiece);
		from.setPiece('.');

		boolean inCheck = isKingInCheck( white);

		// revert
		from.setPiece(movingPiece);
		to.setPiece(oldTo);

		return inCheck;
	}

	/** Checks if the move puts the opponent’s king in check. */
	public static boolean putsOpponentInCheck(tile from, tile to) {
		char movingPiece = from.getPiece();
		boolean white = Character.isUpperCase(movingPiece);

		// simulate move
		char oldTo = to.getPiece();
		to.setPiece(movingPiece);
		from.setPiece('.');

		boolean inCheck = isKingInCheck( !white);

		// revert
		from.setPiece(movingPiece);
		to.setPiece(oldTo);

		return inCheck;
	}

	// --- Check detection helper ---
	private static boolean isKingInCheck( boolean whiteKing) {
		tile kingTile = null;


		for (tile t : BoardUtils.tiles) {
			char p = t.getPiece();
			if (p == (whiteKing ? 'K' : 'k')) {
				kingTile = t;
				break;
			}
		}
		if (kingTile == null)
			return false;

		int kingX = kingTile.getColumn();
		int kingY = kingTile.getRow();

		// check if any enemy piece can legally move to the king's square
		for (tile t : BoardUtils.tiles) {
			char p = t.getPiece();
			if (p == '.' || p == ' ' || p == '\0')
				continue;
			boolean sameColor = (Character.isUpperCase(p) == whiteKing);
			if (sameColor)
				continue;

			double[] vec = new double[4];
			vec[0] = t.getColumn();
			vec[1] = t.getRow();
			vec[2] = kingX;
			vec[3] = kingY;

			if (VectorMoveValidator.isLegalMove(vec, true))
				return true;
		}
		return false;
	}

	public static double normalizeIndex(int i, int max) {
		return (i + 0.5) / max; // center of the square
	}

	// --- Piece move implementations ---
	private static boolean validPawn(char src, int sx, int sy, int dx, int dy) {
		int dir = isWhite(src) ? (whiteMovesUp ? -1 : 1) : (whiteMovesUp ? 1 : -1);
		int startRank = isWhite(src) ? (whiteMovesUp ? height - 2 : 1) : (whiteMovesUp ? 1 : height - 2);

		int dyStep = dy - sy;
		int dxStep = dx - sx;

		if (dxStep == 0 && dyStep == dir) {
			boolean b = isEmpty(get(dx, dy));
			if(b) {GameStats.registerPawnMove();
			}
			return b;
		}
		if (dxStep == 0 && dyStep == 2 * dir && sy == startRank) {
			boolean b = isEmpty(get(sx, sy + dir)) && isEmpty(get(dx, dy));
			if(b) {GameStats.registerPawnMove();
			}
			return b;
		}
		if (Math.abs(dxStep) == 1 && dyStep == dir) {
			
			boolean b = !isEmpty(get(dx, dy)) && !sameColor(src, get(dx, dy));
			if(b) {GameStats.registerPawnMove();
			}
			return b;
		}
		return false;
	}

	private static boolean validKnight(int sx, int sy, int dx, int dy) {
		int dxAbs = Math.abs(dx - sx);
		int dyAbs = Math.abs(dy - sy);

		return (dxAbs == 1 && dyAbs == 2) || (dxAbs == 2 && dyAbs == 1);
	}

	private static boolean validBishop(int sx, int sy, int dx, int dy) {
		int dxAbs = Math.abs(dx - sx);
		int dyAbs = Math.abs(dy - sy);
		if (dxAbs != dyAbs)
			return false;
		return clearLine(sx, sy, dx, dy);
	}

	private static boolean validRook(int sx, int sy, int dx, int dy) {
		if (sx != dx && sy != dy)
			return false;
		return clearLine(sx, sy, dx, dy);
	}

	private static boolean validQueen(int sx, int sy, int dx, int dy) {
		int dxAbs = Math.abs(dx - sx);
		int dyAbs = Math.abs(dy - sy);
		if (sx == dx || sy == dy || dxAbs == dyAbs)
			return clearLine(sx, sy, dx, dy);
		return false;
	}

	private static boolean validKing(int sx, int sy, int dx, int dy) {
		int dxAbs = Math.abs(dx - sx);
		int dyAbs = Math.abs(dy - sy);
		return dxAbs <= 1 && dyAbs <= 1 && (dxAbs + dyAbs > 0);
	}

	private static boolean clearLine(int sx, int sy, int dx, int dy) {
		int stepX = Integer.compare(dx, sx);
		int stepY = Integer.compare(dy, sy);
		int x = sx + stepX;
		int y = sy + stepY;
		while (x != dx || y != dy) {
			if (!inBounds(x, y))
				return false;
			if (!isEmpty(get(x, y)))
				return false;
			x += stepX;
			y += stepY;
		}
		return true;
	}

	public static boolean isLegalMove(tile from, tile to) {


		double[] vec = new double[] { from.getColumn(), from.getRow(), to.getColumn(), to.getRow() };

		return isLegalMove(vec, true);
	}



	public static double evaluateMove(double[] vector,  int width, int height,boolean isMapped) {
		// --- convert normalized coordinates to board indices ---
		if (vector == null || vector.length < 4)
			return -1.0; // invalid move signal

//		System.out.printf(
//			    "Input Vector: %.6f, %.6f, %.6f, %.6f | width=%d height=%d flipY=%b%n",
//			    vector[0], vector[1], vector[2], vector[3], width, height, false
//			);

		int sx = 0;
		int sy = 0;
		int dx = 0;
		int dy = 0;
		if (!isMapped) {

			sx = BoardUtils.toBoardCol(vector[0]);
	       	 sy = BoardUtils.toBoardRow(vector[1]);
	       	 dx = BoardUtils.toBoardCol(vector[2]);
	       	 dy = BoardUtils.toBoardRow(vector[3]);
				
				sy = 7 - (int)(sy);
				 dy = 7 - (int)(dy );
		} else {

			sx = (int) vector[0];
			sy = (int) vector[1];
			dx = (int) vector[2];
			dy = (int) vector[3];

		}
		
		System.out.printf("Converted indices: sx=%d sy=%d dx=%d dy=%d%n", sx, sy, dx, dy);
		if (!inBounds(sx, sy, width, height) || !inBounds(dx, dy, width, height))
			return -0.9; // out-of-bounds penalty

		// --- identify the corresponding tiles ---
		tile from = getTileAt(sx, sy, width);
		tile to = getTileAt( dx, dy, width);
		


		
		char sp = from != null ? from.getPiece() : '?';
		char dp = to != null ? to.getPiece() : '?';
		System.out.printf("src=%c dst=%c%n", sp, dp);
		// --- empty or invalid piece? ---
		if (from == null || to == null)
			return -0.9;
		if (isEmpty(from.getPiece()))
			return -0.8;

		// --- rule-based feedback scoring ---
		if (!VectorMoveValidator.isLegalMove(vector,false)) {
			System.out.println("✖ Illegal move");
			return -0.8; // illegal = penalty
		}
		if (VectorMoveValidator.isCapture(vector,false)) {
			System.out.println("✔ Capture detected");
			GameStats.registerCapture(BoardUtils.isWhiteTurn);

			return +0.4; // small reward for capture
		}
		if (VectorMoveValidator.putsOpponentInCheck(from, to)) {
			return +0.8; // bigger reward for putting opponent in check

		}
		if (VectorMoveValidator.leavesSelfInCheck(from, to))
			return -0.6; // penalty if player exposes own king
		System.out.println("✔ Quiet move");
		return +0.1; // quiet legal move = mild reward
	}

	// --- helpers ---
	private static tile getTileAt( int x, int y, int width) {

	    return BoardUtils.tiles.get(y * width + x);
	}

	private static boolean inBounds(int x, int y, int width, int height) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	// --- Helpers ---
	private static boolean inBounds(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	private static char get(int x, int y) {
		// Ensure valid bounds
		if (x < 0 || y < 0 || x >= width || y >= height) {
			System.err.printf("⚠ get(): out of bounds x=%d y=%d width=%d height=%d%n", x, y,width,height);
			return ' '; // or return '.' if you use that for empty
		}


		int index = y * width + x;
		if (index < 0 || index >= BoardUtils.tiles.size()) {
			System.err.printf("⚠ get(): invalid index=%d for (%d,%d)%n", index, x, y);
			return ' ';
		}

		return BoardUtils.tiles.get(index).getPiece();
	}

	private static boolean isWhite(char c) {
		return Character.isUpperCase(c);
	}

	private static boolean sameColor(char a, char b) {
		if (isEmpty(a) || isEmpty(b))
			return false;
		return (isWhite(a) && isWhite(b)) || (!isWhite(a) && !isWhite(b));
	}

	private static boolean isEmpty(char c) {
		return c == '.' || c == ' ' || c == '\0';
	}


}
