package game;
import java.util.List;

public class VectorToBoardTranslator {


    private final int width, height;

    public VectorToBoardTranslator( int width, int height) {

        this.width = width;
        this.height = height;
    }

	private static int toIndex(double v, int max, boolean flipY) {
	    if (Double.isNaN(v)) v = 0.0;

	    // Clamp into [0, 1]
	    v = Math.max(0.0, Math.min(1.0, v));

	    // Apply flip before scaling
	    if (flipY) v = 1.0 - v;

	    // Convert to discrete coordinate space (0 ... max-1)
	    int idx = (int) Math.floor(v * max); // note: multiply by max, not max-1
	    if (idx == max) idx = max - 1; // handle v = 1.0 edge case

	    return idx;
	}

    private tile getTile(int x, int y, int width) {
    	

    	    return BoardUtils.tiles.get(y * width + x);
   
    }
    private static int[] unpackWeightedCoords(double v, int width, int height) {
        double f = Math.abs(v - Math.floor(v));
        double x = f;

        // chaotic mixing — all digits affect all outputs
        for (int i = 0; i < 10; i++) x = (x * 3.141592653589793) % 1.0;

        double a = (x * 1.61803398875) % 1.0;
        double b = (x * 2.71828182846) % 1.0;
        double c = (x * 1.41421356237) % 1.0;
        double d = (x * 0.5772156649) % 1.0;

        int sx = BoardUtils.toBoardCol(a);
   	 int sy = BoardUtils.toBoardRow(b);
   	 int dx = BoardUtils.toBoardCol(c);
   	 int dy = BoardUtils.toBoardRow(d);
        sy = 7 - (int)(sy);
		 dy = 7 - (int)(dy );
        return new int[]{
            Math.min(Math.max(sx, 0), width  - 1),
            Math.min(Math.max(sy, 0), height - 1),
            Math.min(Math.max(dx, 0), width  - 1),
            Math.min(Math.max(dy, 0), height - 1)
        };
    }


    private static boolean isEmpty(char c) {
        return c == '\0' || c == ' ' || c == '.';
    }

    public boolean applyNormalizedVector(double[] vector, boolean isMapped) {
        if (vector == null || vector.length < 4) return false;

        int sx, sy, dx, dy;
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

        tile fromTile = getTile(sx, sy, width);
        tile toTile   = getTile(dx, dy, width);

        if (fromTile == null || toTile == null) {
            System.out.printf("Invalid move: from=(%d,%d) to=(%d,%d)%n", sx, sy, dx, dy);
            return false;
        }
        System.out.println("BoardMain tiles hash: " + System.identityHashCode(BoardUtils.tiles));


        char movingPiece = fromTile.getPiece();
        char targetPiece = toTile.getPiece();

        if (isEmpty(movingPiece)) {
            System.out.println("Moving piece blank: cannot move.");
            return false;
        }

        if (!isEmpty(targetPiece) && sameColor(movingPiece, targetPiece)) {
            System.out.printf("Blocked move: %c can't capture own piece %c%n", movingPiece, targetPiece);
            return false;
        }

        // 🔒 Prevent concurrent board modification
        synchronized (BoardUtils.tiles) {
            // Perform the move
            if (isEmpty(targetPiece)) {
                System.out.printf("Moved %c from (%d,%d) → (%d,%d)%n", movingPiece, sx, sy, dx, dy);
            } else {
                System.out.printf("%c captures %c at (%d,%d)%n", movingPiece, targetPiece, dx, dy);
            }

            // Update live board references, not copies
            toTile.setPiece(movingPiece);
            fromTile.setPiece(' '); // Always use space for empty
        }

        return true;
    }




    private boolean sameColor(char a, char b) {
        return (Character.isUpperCase(a) && Character.isUpperCase(b))
            || (Character.isLowerCase(a) && Character.isLowerCase(b));
    }
}
