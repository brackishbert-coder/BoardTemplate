package game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;

public class tile implements Serializable {

	private int row;
	private int column;

	public tile(int r, int c) {
		setRow(r);
		setColumn(c);

	}

	@Override
	public String toString() {
        return "tile(" + row + "," + column + ") with piece '" + piece + "'";
    }
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public void render(Graphics2D pANE_ONE_CHANNEL, int displayWidth, int displayHeight, Color imageColor,
			double xOffset, double yOffset) {
		pANE_ONE_CHANNEL.setColor(imageColor);
		pANE_ONE_CHANNEL.drawRect(column * 50 + ((int) xOffset),
                row * 50 + ((int) yOffset), 50, 50);
		

		// Draw piece overlay if there’s a piece on this tile
		if (piece != ' ') {
			pANE_ONE_CHANNEL.setColor(Color.RED);
			pANE_ONE_CHANNEL.setFont(new Font("Monospaced", Font.BOLD, 22));
			int px = (int) (column * 50 + xOffset + 25 - 5);
			int py = (int) (row * 50 + yOffset + 25 + 5);

			pANE_ONE_CHANNEL.drawString(String.valueOf(piece), px, py);

		}
		
		
			pANE_ONE_CHANNEL.setColor(Color.GREEN);
			pANE_ONE_CHANNEL.setFont(new Font("Monospaced", Font.BOLD, 12));
			int px = (int) (column * 50 + xOffset+10  - 5);
			int py = (int) (row * 50 + yOffset +10 + 5);

			pANE_ONE_CHANNEL.drawString(row+""+column, px, py);

		

	}

	private char piece = ' '; // add this near the top

	public boolean hasPiece() {
		return piece != ' ';
	}

	public char getPiece() {
		return piece;
	}

	public void setPiece(char c) {
		//System.out.println("Setting piece at (" + row + "," + column + ") to '" + c + "'");
		piece = c;
	}

	public void clearPiece() {
		piece = ' ';
	}

}
