package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;


public class BoardMain {

	final static String FILE_TO_LOAD_JPG = "DUCK_COP_THE_GAME.jpg";
	static JFrame frameThatisthedemoyouareWATCHINGNOW;
	private static JPanel pane1ThiS_here_stallion_can_get_yo_fo_here_to_to_to_to_to_here;
	private static JTabbedPane tabbedPaneThi_i_i_i_ight;
	private static String Experiment_1_NAME;
	private static Panel displaysStatsAboutRunningScenario;
	private static BufferedImage img;
	private static String currentWorkspaceDirectory = "C:\\Users\\BitBot01\\workspace\\Board\\";
	private static String nicePictureToShowWhenNotLogging = currentWorkspaceDirectory + "resources\\data\\IMAGES\\"
			+ FILE_TO_LOAD_JPG;
	private static Graphics2D PANE_ONE_CHANNEL;
	private static JLabel imageForSeed;
	private static JTextArea txtrWidth;
	private static JTextArea txtrHeight;
	private static JTextArea txtrTurnIndicator;
	private static JTextArea txtrLastPiece;
	private static JTextArea txtrLastMove;
	
	private static JTextArea txtrTotalNumberOfPixels;
	private static JTextArea txtrFile;
	private static ArrayList<JTextArea> factsaboutanexperimentthatisrunning = new ArrayList<JTextArea>();
	private static int height=8;
	private static int width=8;
	private static int total_number_of_pixel_s_;

	private static String FileONEthefiletoprocess;
	private static String filePathForFileONE;
	private static String FileONE_with_constructed_path;
	private static PrintWriter NSA_olive_ve_Let_tray_er_s_Edition;
	private static JTextArea txtrWhiteWins;
	private static JTextArea txtrBlackWins;
	private static JTextArea txtrDraws;
	private static JTextArea txtrLongestGame;
	private static JTextArea txtrShortestGame;
	private static JTextArea txtrGamesPlayed;
	private static JTextArea txtrTurnCount;


	private static final int ONETHOUSAND = 1;
	private static final int FIVE = 5;
	private static final double THREE = 3.0000001;
	private static final int TWENTYFIVE = 25;
	private static final int FOURTEEN = 14;
	private static final int THIRTEEN = 13;
	private static final int ONE = 1;
	private static final int TWENTYFOUR = 24;
	private static final int TWENTYTHREE = 23;
	private static final double ONEHUNDREDTWENTYEIGHT = 128;
	private static final int TWO = 2;
	private static final int TWOHUNDREDFIFTYFIVE = 255;
	private static final int ZERO = 0;
	private static final int TWOTHOUSAND = 2000;
	private static final int THREETHOUSAND = 3000;
	private static final int FOURTHOUSAND = 4000;
	private static final int FIVETHOUSAND = 5000;
	private static final int SIXTHOUSAND = 6000;
	private static final int SEVENTHOUSAND = 7000;
	private static final int EIGHTTHOUSAND = 8000;
	private static final int NINETHOUSAND = 9000;
	private static final int TENTHOUSAND = 10000;
	private static int max = FIVE;
	private static ArrayList<vector> pixels;

	private static int ONETHOUSANDTWENTYFOUR = 1024;
	private static int mouseWheelNotches;
	private static double mousePreciseRotation;
	static Point currentLocation = MouseInfo.getPointerInfo().getLocation();
	static Point initialLocation = currentLocation;
	private static ArrayList<vector> moveBlackList = new ArrayList<>();
	private static ArrayList<vector> moveWhiteList = new ArrayList<>();
	private static String lastPieceMoved = "";
	private static String lastMoveFromTo = "";
	private static boolean isGameOver = false;
	private static String gameOverMessage = "";
	// === Timeout & turn tracking ===
	private static int totalTurnCount = 0;          // total turns played
	private static int consecutiveTimeoutsWhite = 0;
	private static int consecutiveTimeoutsBlack = 0;
	private static final int MAX_TIMEOUTS = 3;      // 3 per player = lose
	private static final int MAX_TURNS = 50;        // global game cap
	private static final long TURN_TIME_LIMIT_MS = 100000; // 10 seconds per move (example)
	private static long turnStartTime = System.currentTimeMillis();

	private static JButton nextTurnButton;
	private static tile selectedTile = null;
	static MouseMotionListener mouseListen = new MouseMotionListener() {

		@Override
		public void mouseMoved(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			currentLocation = e.getLocationOnScreen();
		}
	};
	private static ArrayList<vector> moves;
	private static VectorToBoardTranslator translatorWhite;
	private static VectorToBoardTranslator translatorBlack;
	private static ClientVectorizer clientVectorizer;
	private static VectorMoveValidator validator;
	private static boolean printedOnce;
	private static final int TILE_SIZE = 50;
	private static double xOffset = 0; // update from wherever you set these
	private static double yOffset = 0;
	private static BoardToWebcamServer boardSender = new BoardToWebcamServer("localhost", 5050);
	private static BoardUtils utils;
	private static JTextArea txtrGameOver;

//	static JFrame frame1 = new JFrame("Bang Stream Viewer");
//    static JLabel imageLabel = new JLabel();
//    
//	

	public static void main(String[] args) {
//		frame1.getContentPane().add(imageLabel, BorderLayout.CENTER);
//	    frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	    frame1.setSize(640, 480);
//	    frame1.setVisible(true);

		BoardUtils.create_a_eight_by_eight_of_tiles();
		BoardUtils.populateBoard();
		
			System.out.println("A)Current board snapshot:");
			for (int y = 0; y < height; y++) {
			    for (int x = 0; x < width; x++) {
			        System.out.print(BoardUtils.getTiles().get(y*width+x).getPiece() + " ");
			    }
			    System.out.println();
			}
		clientVectorizer = new ClientVectorizer();
		Thread thread = new Thread(clientVectorizer);
		thread.start();
		set_up_display_stuff();

		validator = new VectorMoveValidator(8, 8);
		// Create translator
		translatorBlack = new VectorToBoardTranslator(8, 8);

		// Example move list — you can load these dynamically later

		translatorWhite = new VectorToBoardTranslator(8, 8);

		// Example move list — you can load these dynamically later

		// Create the button
		nextTurnButton = new JButton("Next Turn");

		// Add listener
		nextTurnButton.addActionListener(e -> {
		    // Flip the turn
		    BoardUtils.isWhiteTurn = !BoardUtils.isWhiteTurn;

		    // Update turn label and send updated state
		    updateStatsPanel();
		    sendTiles();

		    System.out.println("🔄 Turn flipped manually → " +
		        (BoardUtils.isWhiteTurn ? "White's turn" : "Black's turn"));
		});


		// Add the button to your frame/panel (assuming you use BorderLayout)
		displaysStatsAboutRunningScenario.add(nextTurnButton, BorderLayout.SOUTH);

		// Example initialization
		// Correct back rank order: a–h files

		moves = new ArrayList<>();
		GameStats.startNewGame();

		while (true) {
			if(isGameOver) {
				BoardUtils.create_a_eight_by_eight_of_tiles();
				BoardUtils.populateBoard();
				BoardUtils.isWhiteTurn = true;   // fresh board: white always moves first
				consecutiveTimeoutsWhite=0;
				consecutiveTimeoutsBlack=0;
				totalTurnCount=0;
				turnStartTime = System.currentTimeMillis(); // reset timer on every successful move
				GameStats.registerTurnTimeout(BoardUtils.isWhiteTurn);

//				set_up_display_stuff();
				isGameOver=false;

				GameStats.startNewGame();

			}
			try {
				Thread.sleep(555);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clear_screen();

			calculate_offset_and_print_tiles_in_grid_fassion(currentLocation, initialLocation);
			frameThatisthedemoyouareWATCHINGNOW.repaint();
			playNextMove(currentLocation.getX() - initialLocation.getX(),
					currentLocation.getY() - initialLocation.getY());
			frameThatisthedemoyouareWATCHINGNOW.repaint();

		}
	}

	private static void sendTiles() {

		
		
//		/// below is a stub to fake data to VectorClientDummyServer
//		try (Socket s = new Socket("localhost", 5021);
//				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {
//			out.writeObject(getTiles()); // tiles is a List<tile>
//			out.flush();
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		/// below is a stub to fake data to VectorClientDummyServer
		try (Socket s = new Socket("localhost", 5021);
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {
			out.writeObject(getTiles()); // tiles is a List<tile>
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		try (Socket s = new Socket("localhost", 5022);
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {
			out.writeObject(getTiles()); // tiles is a List<tile>
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		try (Socket s = new Socket("localhost", 5023);
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {
			out.writeObject(BoardUtils.isWhiteTurn); // tiles is a List<tile>
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
		try (Socket s = new Socket("localhost", 5024);
				ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {
			out.writeObject(BoardUtils.isWhiteTurn); // tiles is a List<tile>
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}




private static void checkEndConditions() {
    boolean inCheck = BoardUtils.isKingInCheck(BoardUtils.isWhiteTurn);
    boolean hasMoves = LegalMoveLibrary.hasAnyLegalMoves(BoardUtils.isWhiteTurn);

    if(System.currentTimeMillis()-turnStartTime>=TURN_TIME_LIMIT_MS) {
    	if(BoardUtils.isWhiteTurn) {
    		System.out.println("🕒 White turn timeout");
    		BoardUtils.isWhiteTurn=!BoardUtils.isWhiteTurn;
    		consecutiveTimeoutsWhite++;
    		turnStartTime = System.currentTimeMillis(); 
    	}else {
    		 System.out.println("🕒 Black turn timeout");
    		 BoardUtils.isWhiteTurn=!BoardUtils.isWhiteTurn;
    		 consecutiveTimeoutsBlack++;
    		 turnStartTime = System.currentTimeMillis(); 
    	}
    }
    // 🕑 Check for timeouts
    if (consecutiveTimeoutsWhite >= MAX_TIMEOUTS) {
        System.out.println("🕒 White lost by timeout");
        System.out.println("Black wins!");
        isGameOver=true;
        GameStats.recordGameResult("Black wins!", totalTurnCount);

        GameStats.registerGameEnd("Black wins!", false, true);

    }
    if (consecutiveTimeoutsBlack >= MAX_TIMEOUTS) {
        System.out.println("🕒 Black lost by timeout");
        System.out.println("White wins!");
        GameStats.recordGameResult("White wins!", totalTurnCount);
        GameStats.registerGameEnd("White wins!", true, false);
        isGameOver=true;
    }

    // 🧮 Check for total turn cap
    if (totalTurnCount >= MAX_TURNS) {
        System.out.println("🏁 50-turn limit reached – Draw!");
        GameStats.recordGameResult("🏁 50-turn limit reached – Draw!", totalTurnCount);
        GameStats.registerGameEnd("🏁 50-turn limit reached – Draw!", false, false);
        isGameOver=true;
    }

    // ♟ Normal check/checkmate/stalemate handling
    if (!hasMoves) {
        if (inCheck) {
            String winner = BoardUtils.isWhiteTurn ? "Black" : "White";
            System.out.println("♔ Checkmate! " + winner + " wins!");

            boolean win =BoardUtils.isWhiteTurn;

            GameStats.recordGameResult("♔ Checkmate! ", totalTurnCount);
            GameStats.registerGameEnd("♔ Checkmate! " + winner + " wins!", win, !win);
        } else {
            System.out.println("🤝 Stalemate!");

            GameStats.recordGameResult("♔ Checkmate! ", totalTurnCount);
            GameStats.registerGameEnd("🤝 Stalemate!", false, false);
            
        }
        isGameOver=true;
    }
}


private static char getHighestCapturedPiece(boolean forWhite) {
    // Replace this with your actual captured list if you track it
    // Otherwise return a default high-value piece for testing
    return 'Q'; // Always promote to Queen if you don’t track captures
}

public static boolean hasAnyLegalMoves(boolean isWhite) {
    if (BoardUtils.tiles == null) return false;

    for (tile src : BoardUtils.tiles) {
        char piece = src.getPiece();
        if (piece == ' ' || piece == '.' || piece == '\0') continue;
        if (Character.isUpperCase(piece) != isWhite) continue;

        int sx = src.getColumn();
        int sy = src.getRow();

        for (int dy = 0; dy < 8; dy++) {
            for (int dx = 0; dx < 8; dx++) {
                if (sx == dx && sy == dy) continue;

                double[] moveVec = {
                    sx / 7.0, sy / 7.0,
                    dx / 7.0, dy / 7.0
                };

                if (!VectorMoveValidator.isLegalMove(moveVec, false))
                    continue;

                // Simulate move
                tile from = BoardUtils.getTile(sx, sy, 8,8);
                tile to   = BoardUtils.getTile(dx, dy, 8,8);
                char captured = to.getPiece();

                BoardUtils.movePiece(sx, sy, dx, dy);

                boolean kingStillInCheck = BoardUtils.isKingInCheck(isWhite);

                // Rollback
                BoardUtils.movePiece(dx, dy, sx, sy);
                to.setPiece(captured);

                if (!kingStillInCheck)
                    return true;
            }
        }
    }
    return false;
}

	private static void playNextMove(double xOffset, double yOffset) {
		if (isGameOver) {
		    System.out.println("Game over: " + gameOverMessage);
		    return;
		}

		
		

		if (BoardUtils.isWhiteTurn) {
			System.out.println("White move: ");
			System.out.println("A)Current board snapshot:");
			for (int y = 0; y < 8; y++) {
			    for (int x = 0; x < 8; x++) {
			        System.out.print(BoardUtils.getTiles().get(y*8+x).getPiece() + " ");
			    }
			    System.out.println();
			}
			do {
				sendTiles();
				LegalMoveLibrary.setBoard(BoardUtils.tiles);

				double[] f = clientVectorizer.getFeatureVector();
				System.out.println("Received vector in white section: " + f[0] + "," + f[1] + " -> " + f[2] + "," + f[3]+" legal: "+VectorMoveValidator.isLegalMove(f, false)+" isWhite: "+BoardUtils.isWhitePiece(f));
				if (VectorMoveValidator.isLegalMove(f, false) && BoardUtils.isWhitePiece(f)) {
					char piece = getPieceAtStart(true, f); // helper we’ll define below
				    lastPieceMoved = String.valueOf(piece);
				    lastMoveFromTo = getMoveString(f);
				    System.out.println("Tiles reference hash: " + System.identityHashCode(BoardUtils.tiles));

					translatorWhite.applyNormalizedVector(f, false);
					GameStats.finishTurn(BoardUtils.isWhiteTurn);

					sendTiles();
					checkEndConditions();
					LegalMoveLibrary.setBoard(BoardUtils.tiles);

					// Increment total turns
					totalTurnCount++;

					BoardUtils.isWhiteTurn = false;
					GameStats.startTurn();

					updateStatsPanel();
					System.out.println("White done.");
					
					break;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				checkEndConditions();
			} while (BoardUtils.isWhiteTurn && !isGameOver );
		} else {
			System.out.println("Black move: ");
			System.out.println("A)Current board snapshot:");
			for (int y = 0; y < 8; y++) {
			    for (int x = 0; x < 8; x++) {
			        System.out.print(BoardUtils.tiles.get(y*8+x).getPiece() + " ");
			    }
			    System.out.println();
			}
			do {
				sendTiles();
				LegalMoveLibrary.setBoard(BoardUtils.tiles);

				double[] f = clientVectorizer.getFeatureVector();
				System.out.println("Received vector in black section: " + f[0] + "," + f[1] + " -> " + f[2] + "," + f[3]+" legal: "+VectorMoveValidator.isLegalMove(f, false)+" isBlack: "+BoardUtils.isBlackPiece(f));
				if (VectorMoveValidator.isLegalMove(f, false) && BoardUtils.isBlackPiece(f)) {
					char piece = getPieceAtStart(true, f); // helper we’ll define below
				    lastPieceMoved = String.valueOf(piece);
				    lastMoveFromTo = getMoveString(f);
				    System.out.println("Tiles reference hash: " + System.identityHashCode(BoardUtils.tiles));
	
				    translatorBlack.applyNormalizedVector(f, false);
				    GameStats.finishTurn(BoardUtils.isWhiteTurn);

					sendTiles();
					checkEndConditions();
					LegalMoveLibrary.setBoard(BoardUtils.tiles);

					// Increment total turns
					totalTurnCount++;

					BoardUtils.isWhiteTurn = true;
					GameStats.startTurn();

					updateStatsPanel();
					System.out.println("Black done.");
					break;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				checkEndConditions();
			} while (!BoardUtils.isWhiteTurn&& !isGameOver );
		}
		checkEndConditions();
	}
	
	
	private static char getPieceAtStart(boolean isWhite, double[] moveVec) {
	    int sx = (int)Math.round(moveVec[0]*7);
	    int sy = (int)Math.round(moveVec[1]*7);
	    for (tile t : BoardUtils.tiles) {
	        if (t.getColumn()==sy && t.getRow()==sx) return t.getPiece();
	    }
	    return ' ';
	}
	private static void updateStatsPanel() {

	    // Basic info
	    txtrTurnIndicator.setText("Current Turn: " + (BoardUtils.isWhiteTurn ? "White" : "Black"));

	    txtrLastPiece.setText("Last Piece: " +
	            (lastPieceMoved == null || lastPieceMoved.isEmpty() ? "None" : lastPieceMoved));

	    txtrLastMove.setText("Last Move: " +
	            (lastMoveFromTo == null || lastMoveFromTo.isEmpty() ? "None" : lastMoveFromTo));

	    txtrGameOver.setText(isGameOver ? "Game Over: " + gameOverMessage : "Game Active");

	    // === STATISTICS ===
	    txtrGamesPlayed.setText("Games Played: " + GameStats.gamesPlayed);
	    txtrWhiteWins.setText("White Wins: " + GameStats.whiteWins);
	    txtrBlackWins.setText("Black Wins: " + GameStats.blackWins);
	    txtrDraws.setText("Draws: " + GameStats.draws);

	    txtrLongestGame.setText("Longest Game: " + GameStats.longestGameLength + " turns");

	    if (GameStats.shortestGameLength == Integer.MAX_VALUE)
	        txtrShortestGame.setText("Shortest Game: N/A");
	    else
	        txtrShortestGame.setText("Shortest Game: " + GameStats.shortestGameLength + " turns");

	    txtrTurnCount.setText("Current Game Turns: " + totalTurnCount);
	}


	private static String getMoveString(double[] moveVec) {
	    int sx = (int)Math.floor(moveVec[0]*7);
	    int sy = (int)Math.floor(moveVec[1]*7);
	    int dx = (int)Math.floor(moveVec[2]*7);
	    int dy = (int)Math.floor(moveVec[3]*7);
	    return "(" + sx + "," + sy + ") → (" + dx + "," + dy + ")";
	}

	private static void calculate_offset_and_print_tiles_in_grid_fassion(Point currentLocation,
			Point previousLocation) {
		xOffset = currentLocation.getX() - previousLocation.getX();
		yOffset = currentLocation.getY() - previousLocation.getY();

		print_tiles_in_grid_fassion(xOffset, yOffset);
		if (!printedOnce) {
			printedOnce = true;
			System.out.println("BoardMain debug:");
			System.out.println("imageForSeed height = " + imageForSeed.getHeight());
			System.out.println("yOffset used in tile.render = " + yOffset);
		}

	}

	private static void clear_screen() {
		PANE_ONE_CHANNEL.setColor(Color.BLACK);

		PANE_ONE_CHANNEL.fillRect(0, 0, width - 1, height - 1);
	}

	private static void print_tiles_in_grid_fassion(double xOffset, double yOffset) {
		for (tile thespotontheboard : getTiles()) {
			int displayWidth = 1024;
			int displayHeight = 800;
			thespotontheboard.render(PANE_ONE_CHANNEL, displayWidth, displayHeight, new Color(255, 125, 0), xOffset,
					yOffset);
		}
	}

	private static void set_up_display_stuff() {
		frameThatisthedemoyouareWATCHINGNOW = new JFrame("Chess Board Demo");

		frameThatisthedemoyouareWATCHINGNOW.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				mouseWheelNotches = e.getWheelRotation();
				mousePreciseRotation = e.getPreciseWheelRotation();
				// System.out.println("mouse wheel notches "+mouseWheelNotches);
				System.out.println("mouse wheel rotation " + mousePreciseRotation);
			}
		});

		pane1ThiS_here_stallion_can_get_yo_fo_here_to_to_to_to_to_here = new JPanel();

		pane1ThiS_here_stallion_can_get_yo_fo_here_to_to_to_to_to_here.addMouseMotionListener(mouseListen);
		// === Add this after creating 'imageForSeed' in set_up_display_stuff() ===

		tabbedPaneThi_i_i_i_ight = new JTabbedPane(JTabbedPane.TOP);
		tabbedPaneThi_i_i_i_ight.addTab(Experiment_1_NAME, null,
				pane1ThiS_here_stallion_can_get_yo_fo_here_to_to_to_to_to_here, null);

		frameThatisthedemoyouareWATCHINGNOW.add(tabbedPaneThi_i_i_i_ight);
		frameThatisthedemoyouareWATCHINGNOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pane1ThiS_here_stallion_can_get_yo_fo_here_to_to_to_to_to_here
				.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		pane1ThiS_here_stallion_can_get_yo_fo_here_to_to_to_to_to_here
				.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		displaysStatsAboutRunningScenario = new Panel();

		displaysStatsAboutRunningScenario.setLayout(new GridLayout(7, 1, 0, 0));

		pane1ThiS_here_stallion_can_get_yo_fo_here_to_to_to_to_to_here.add(displaysStatsAboutRunningScenario);

		img = new BufferedImage(1024, 800, BufferedImage.TYPE_INT_RGB);
		// try {

		// img = ImageIO.read(new File(nicePictureToShowWhenNotLogging));
		getHeightAndWidthOfImage(img);
		calculate_total_number_of_pixels_SLASH_nodes___();

		PANE_ONE_CHANNEL = img.createGraphics();
		// } catch (IOException e) {
		// System.out.println(nicePictureToShowWhenNotLogging);
		// e.printStackTrace();
		// }
		{
			ZERO_regester_a_fact_or_statistic_about___();

			ONE_add_regestered_facts_added_to_info_panel();

			TWO_write_statistics_to_a_text_box_single_line();
		}
		;

		ImageIcon image = new ImageIcon(img);

		imageForSeed = new JLabel(image);
		imageForSeed.setOpaque(true);
		imageForSeed.setEnabled(true);
		imageForSeed.setFocusable(true);
		imageForSeed.requestFocusInWindow();

		imageForSeed.setHorizontalAlignment(SwingConstants.TRAILING);
		// --- Click-to-move system ---
		/*
		 * imageForSeed.addMouseListener(new java.awt.event.MouseAdapter() {
		 * 
		 * @Override public void mouseClicked(MouseEvent e) {
		 * 
		 * // --- Turn check --- if (isWhitesTurn) {
		 * System.out.println("It's not your turn."); return; }
		 * 
		 * // --- Get clicked tile --- tile clicked = getTileFromClick(e); if (clicked
		 * == null) { System.out.println("Click outside board"); return; }
		 * 
		 * char piece = clicked.getPiece();
		 * 
		 * // --- Selection phase --- if (selectedTile == null) { if (piece != '.' &&
		 * piece != ' ' && piece != '\0') {
		 * 
		 * if (isWhitesTurn && !isWhitePiece(piece)) { System.out.
		 * println("Illegal selection: it's White's turn, but that is a Black piece.");
		 * return; } if (!isWhitesTurn && !isBlackPiece(piece)) { System.out.
		 * println("Illegal selection: it's Black's turn, but that is a White piece.");
		 * return; } selectedTile = clicked;
		 * System.out.printf("Selected piece '%s' at (%d,%d)%n", piece,
		 * clicked.getColumn(), clicked.getRow()); highlightTile(clicked); // optional
		 * visual cue } return; }
		 * 
		 * // --- Move phase --- double[] moveVec = toVector(selectedTile, clicked); //
		 * [fromCol, fromRow, toCol, toRow] (integers)
		 * 
		 * // Mirror horizontally for downstream board representation
		 * 
		 * // System.out.printf("Attempting move from (%d,%d) to (%d,%d)%n", //
		 * selectedTile.getColumn(), selectedTile.getRow(), // clicked.getColumn(),
		 * clicked.getRow());
		 * 
		 * // --- Legality check + move execution --- if
		 * (VectorMoveValidator.isLegalMove(moveVec, true)) { //
		 * System.out.printf("Legal move from (%d,%d) to (%d,%d)%n", //
		 * selectedTile.getColumn(), selectedTile.getRow(), // clicked.getColumn(),
		 * clicked.getRow());
		 * 
		 * if (!isWhitesTurn) { System.out.println("Black move: ");
		 * translatorBlack.applyNormalizedVector(moveVec, true); isWhitesTurn = true;
		 * 
		 * System.out.println("Black done."); } BufferedImage boardOnly =
		 * BoardSnapshot.captureBoardRegion(imageForSeed, TILE_SIZE, xOffset, yOffset);
		 * boardSender.sendBoardFrame(boardOnly); } else {
		 * System.out.println("Illegal move attempted."); }
		 * 
		 * selectedTile = null; frameThatisthedemoyouareWATCHINGNOW.repaint(); } });
		 */
		pane1ThiS_here_stallion_can_get_yo_fo_here_to_to_to_to_to_here.add(imageForSeed);

		System.out.println("flash pixels red green blue random for 3");

		fill_in_list_of_pixels_before_you_make_any_changes_to_the_screen();

		flash_pixels_red_green_blue_random_for(3);

		frameThatisthedemoyouareWATCHINGNOW.pack();
		frameThatisthedemoyouareWATCHINGNOW.setVisible(true);
		frameThatisthedemoyouareWATCHINGNOW.repaint();

		// System.out.println("load image from saved pixels");
		// load_image_from_saved_pixels();

		System.out.println("Oh what a Nice Picture: " + nicePictureToShowWhenNotLogging);

	}

	private static void ZERO_regester_a_fact_or_statistic_about___() {
		txtrWidth = new JTextArea();
		txtrHeight = new JTextArea();
		txtrTotalNumberOfPixels = new JTextArea();
		txtrFile = new JTextArea();
		txtrTurnIndicator = new JTextArea();
		txtrLastPiece = new JTextArea();
		txtrLastMove = new JTextArea();
		txtrWhiteWins = new JTextArea();
		txtrBlackWins = new JTextArea();
		txtrDraws = new JTextArea();
		txtrLongestGame = new JTextArea();
		txtrShortestGame = new JTextArea();
		txtrGamesPlayed = new JTextArea();
		txtrTurnCount = new JTextArea();
		
		txtrGamesPlayed = new JTextArea("Games Played: 0");
		txtrWhiteWins = new JTextArea("White Wins: 0");
		txtrBlackWins = new JTextArea("Black Wins: 0");
		txtrDraws = new JTextArea("Draws: 0");

		txtrLongestGame = new JTextArea("Longest Game: 0 turns");
		txtrShortestGame = new JTextArea("Shortest Game: 0 turns");

		txtrTurnCount = new JTextArea("Current Game Turns: 0");

		// formatting
		for (JTextArea t : new JTextArea[] {
		        txtrGamesPlayed, txtrWhiteWins, txtrBlackWins, txtrDraws,
		        txtrLongestGame, txtrShortestGame, txtrTurnCount }) {
		    t.setEditable(false);
		    t.setBackground(Color.BLACK);
		    t.setForeground(Color.WHITE);
		}


	}

	private static void ONE_add_regestered_facts_added_to_info_panel() {
		factsaboutanexperimentthatisrunning.add(txtrHeight);
		factsaboutanexperimentthatisrunning.add(txtrWidth);
		factsaboutanexperimentthatisrunning.add(txtrTotalNumberOfPixels);
		factsaboutanexperimentthatisrunning.add(txtrFile);

		displaysStatsAboutRunningScenario.add(txtrHeight);
		displaysStatsAboutRunningScenario.add(txtrWidth);
		displaysStatsAboutRunningScenario.add(txtrTotalNumberOfPixels);
		displaysStatsAboutRunningScenario.add(txtrFile);
		factsaboutanexperimentthatisrunning.add(txtrTurnIndicator);
		factsaboutanexperimentthatisrunning.add(txtrLastPiece);
		factsaboutanexperimentthatisrunning.add(txtrLastMove);

		displaysStatsAboutRunningScenario.add(txtrTurnIndicator);
		displaysStatsAboutRunningScenario.add(txtrLastPiece);
		displaysStatsAboutRunningScenario.add(txtrLastMove);
		txtrGameOver = new JTextArea();
		factsaboutanexperimentthatisrunning.add(txtrGameOver);
		displaysStatsAboutRunningScenario.add(txtrGameOver);
		
		displaysStatsAboutRunningScenario.add(txtrGamesPlayed);
		displaysStatsAboutRunningScenario.add(txtrWhiteWins);
		displaysStatsAboutRunningScenario.add(txtrBlackWins);
		displaysStatsAboutRunningScenario.add(txtrDraws);
		displaysStatsAboutRunningScenario.add(txtrLongestGame);
		displaysStatsAboutRunningScenario.add(txtrShortestGame);
		displaysStatsAboutRunningScenario.add(txtrTurnCount);


	}

	private static void TWO_write_statistics_to_a_text_box_single_line() {

		txtrHeight.setText("height: " + height);
		txtrWidth.setText("width: " + width);
		txtrTotalNumberOfPixels.setText("total pixel count: " + total_number_of_pixel_s_);
		txtrFile.setText("file: " + FILE_TO_LOAD_JPG);

		System.out.println("height: " + height);
		System.out.println("width: " + width);
		System.out.println("total pixel count: " + total_number_of_pixel_s_);
		System.out.println("file: " + FILE_TO_LOAD_JPG);
	}

	private static void getHeightAndWidthOfImage(BufferedImage img) {
		height = img.getHeight();
		width = img.getWidth();
	}

	private static void calculate_total_number_of_pixels_SLASH_nodes___() {
		total_number_of_pixel_s_ = height * width;
	}

	private static void flash_pixels_red_green_blue_random_for(int some_amount_of_t) {

		Color red = new Color(TWOHUNDREDFIFTYFIVE, ZERO, ZERO);
		Color pink = new Color(TWOHUNDREDFIFTYFIVE, ZERO, TWOHUNDREDFIFTYFIVE);
		Color yellow = new Color(TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE, ZERO);
		Color greenBlue = new Color(0, TWOHUNDREDFIFTYFIVE, TWOHUNDREDFIFTYFIVE);
		Color green = new Color(ZERO, TWOHUNDREDFIFTYFIVE, ZERO);
		Color blue = new Color(ZERO, ZERO, TWOHUNDREDFIFTYFIVE);

		am_i_winking_blinking_or_noding(red, pink);
		now_can_you_see_the_change_in_the_pattern_(green, yellow);
		ok_cool_can_you_now_see_the_blue_dog_(blue, greenBlue);
		ok_now_RANDOM_Red_Green_Blue();

		// dot_dot_dot();

	}

	private static void am_i_winking_blinking_or_noding(Color red, Color white) {
		System.out.println("im gonna giggle red a little can you see it");
		System.out.println("close your eyes and think of red");

		I_SHALL_NOW_FLASH_THIS_MIXEL_PIXEL_RED_WHITE_or_WHITE_RED(red, white);
	}

	private static void now_can_you_see_the_change_in_the_pattern_(Color thesecondcolorichosetoshow, Color white) {
		for (int random = ((int) (Math.random() * 14)); random <= 13; random++) {
			FIRSTTHISPIXELSHALLBEEEEEEEEEEEEEEEEEEEEEEEEEEEE(thesecondcolorichosetoshow);
			// System.out.println("now white ");
			THISPIXELSHALLBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(
					white);
		}

	}

	private static void ok_cool_can_you_now_see_the_blue_dog_(Color blue, Color white) {
		for (int random = ((int) (Math.random() * 14)); random <= 13; random++) {
			FIRSTTHISPIXELSHALLBEEEEEEEEEEEEEE(blue);
			System.out.println("now white ");
			THISPIXELSHALLBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(
					white);
		}

	}

	private static void ok_now_RANDOM_Red_Green_Blue() {
		for (int random = ((int) (Math.random() * TWENTYFOUR)); random <= TWENTYTHREE; random++) {
			System.out.println("now a random Red ");

			Color randomRed = new Color(((int) (Math.random() * TWOHUNDREDFIFTYFIVE)), ZERO, ZERO);
			Color randomGreen = new Color(ZERO, ((int) (Math.random() * TWOHUNDREDFIFTYFIVE)), ZERO);
			Color randomBlue = new Color(ZERO, ZERO, ((int) (Math.random() * TWOHUNDREDFIFTYFIVE)));

			FIRSTTHISPIXELSHALLBEEEEEEEEEEEEEE(randomRed);
			System.out.println("now a random Green ");
			THISPIXELSHALLBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(
					randomGreen);
			System.out.println("now a random Blue ");
			THISPIXELSHALLBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(
					randomBlue);
		}

	}

	private static void FIRSTTHISPIXELSHALLBEEEEEEEEEEEEEE(Color blue) {

		for (int j = 0; j < height; j++) {
			PANE_ONE_CHANNEL.setColor(blue);
			PANE_ONE_CHANNEL.drawRect(0, j, ONETHOUSANDTWENTYFOUR, 1);
		}
		frameThatisthedemoyouareWATCHINGNOW.pack();
		frameThatisthedemoyouareWATCHINGNOW.setVisible(true);
		frameThatisthedemoyouareWATCHINGNOW.repaint();
		okletsalltakeTHREEandcallitthislightingschemeisreallycommingtogetherguysiloveitFIVEMIPPIPPIS();
	}

	private static void okletsalltakeTHREEandcallitthislightingschemeisreallycommingtogetherguysiloveitFIVEMIPPIPPIS() {
		try {
			System.out.println("Sleeping for " + ((int) (Math.random() * FIVE)) * ONETHOUSAND + " ok ok ok ");
			Thread.sleep(((int) (Math.random() * FIVE)) * ONETHOUSAND);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void I_SHALL_NOW_FLASH_THIS_MIXEL_PIXEL_RED_WHITE_or_WHITE_RED(Color red, Color white) {
		for (int random = ((int) ((Math.random() * FOURTEEN) + ONE)); random <= THIRTEEN; random++) {
			FIRSTTHISPIXELSHALLBEEEEEEEEEEEEEEEEEEEEEEEEEEEE(red);
			// System.out.println("now white ");
			THISPIXELSHALLBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(
					white);
		}
	}

	private static void FIRSTTHISPIXELSHALLBEEEEEEEEEEEEEEEEEEEEEEEEEEEE(Color theColorIAmShowingFIRST) {

		for (int j = 0; j < height; j++) {
			PANE_ONE_CHANNEL.setColor(theColorIAmShowingFIRST);
			PANE_ONE_CHANNEL.drawRect(0, j, ONETHOUSANDTWENTYFOUR, ONE);
		}
		frameThatisthedemoyouareWATCHINGNOW.pack();
		frameThatisthedemoyouareWATCHINGNOW.setVisible(true);
		frameThatisthedemoyouareWATCHINGNOW.repaint();
		andtheprinceaskedtheprincesstosleepforasecondwearealmostthereFIVEMIPPIPPIS();
		frameThatisthedemoyouareWATCHINGNOW.pack();
		frameThatisthedemoyouareWATCHINGNOW.setVisible(true);
		frameThatisthedemoyouareWATCHINGNOW.repaint();

	}

	private static void andtheprinceaskedtheprincesstosleepforasecondwearealmostthereFIVEMIPPIPPIS() {
		try {
			int sleep_for_some_amount = (int) (Math.random() * FIVE);
			System.out.println("Red is all set so im going to Sleeping for: " + sleep_for_some_amount + " s");
			Thread.sleep(sleep_for_some_amount * ONETHOUSAND);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void THISPIXELSHALLBEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE(
			Color white) {

		for (int j = 0; j < height; j++) {
			PANE_ONE_CHANNEL.setColor(white);
			PANE_ONE_CHANNEL.drawRect(0, j, ONETHOUSANDTWENTYFOUR, ONE);
		}
		frameThatisthedemoyouareWATCHINGNOW.pack();
		frameThatisthedemoyouareWATCHINGNOW.setVisible(true);
		frameThatisthedemoyouareWATCHINGNOW.repaint();
		int some_number_of_mippippies = (int) (Math.random() * THREE);
		please_be_patient_this_section_is_still_under_construction_delay_s_are_to_be_expected_SOMENUMBEROFMIPPIPPIPPIS(
				some_number_of_mippippies);
		frameThatisthedemoyouareWATCHINGNOW.pack();
		frameThatisthedemoyouareWATCHINGNOW.setVisible(true);
		frameThatisthedemoyouareWATCHINGNOW.repaint();

		System.out.println("What exactly, were/was, the, number/count, of mippippies?");
		int toTHEMAXimum = (int) (Math.random() * max);
		int soMAXIMUMitisalmostEXTREAMextream = (int) (Math.random() * max);
		int imeanthisisLikeMAXonSteroidsTimesInfinityPlusOneForever = (int) (Math.random() * max);
		System.out.println("If my calculations are correct then " + some_number_of_mippippies + " mi"
				+ print_i_please(toTHEMAXimum) + "s" + print_s_please(soMAXIMUMitisalmostEXTREAMextream) + "i"
				+ print_i_please(imeanthisisLikeMAXonSteroidsTimesInfinityPlusOneForever) + "ssippis have passed.");

	}

	private static void please_be_patient_this_section_is_still_under_construction_delay_s_are_to_be_expected_SOMENUMBEROFMIPPIPPIPPIS(
			int i) {
		try {
			// System.out.println("Set the second color so now im going to Sleep
			// for [" + i * ONETHOUSAND + "] ");
			Thread.sleep(i * ONETHOUSAND);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static String print_i_please(int i) {
		hey_max_what_is_your_job();
		well_then_please();
		System.out.println("I i captian;");
		for (; i < max; i++) {
			System.out.print("i");
		}
		System.out.println(";naitpac i I");
		return "i";
	}

	private static void well_then_please() {
		System.out.println("Please print i to the " + hey_max_what_is_your_name_again() + " dude/tude/food thank s");

	}

	public static void hey_max_what_is_your_job() {
		System.out.println("it is printer to the max and no more, after " + max
				+ " i am done, this whole \"joke\" has gone way to far . . .");
	}

	private static String hey_max_what_is_your_name_again() {
		System.out.println(
				"it is max i have only ben gone " + THREE + " second s you couldent have frogotten that much . . . ");
		return "max";
	}

	private static void could_you_do_me_this_favor_and() {
		System.out.println("Please print s to the " + hey_max_what_is_your_name_again() + " dude/tude/food thank s");

	}

	private static String print_s_please(int s) {
		hey_max_what_is_your_job();
		could_you_do_me_this_favor_and();
		System.out.println("S s captian;");
		for (; s < max; s++) {
			System.out.print("s");
		}
		System.out.println(";naitpac s S");
		return "s";
	}

	private static void fill_in_list_of_pixels_before_you_make_any_changes_to_the_screen() {
		pixels = new ArrayList<vector>();

		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				int argb = img.getRGB(i, j);
				pixels.add((j * width) + i, build_a_vector_of_this_pixel_here_take_this_(argb));
				// System.out.println("recently added pixel
				// "+pixels.get((j*height)+i).getxDimension()+","+pixels.get((j*height)+i).getyDimension()+","+pixels.get((j*height)+i).getzDimension());
			}

			// System.out.println("LOADED ROW OF SIZE: "+pixels.get(j).size()+"
			// mixels/pixels/pixelz");
		}

		// System.out.println("TOTAL ROWS LOADED: "+pixels.size()+"
		// pixels/mixels/zlexip");

	}

	public static vector build_a_vector_of_this_pixel_here_take_this_(int plus_one) {
		double alpha = (plus_one >> 24) & 0xff;
		int red = (plus_one >> 16) & 0xff;
		int green = (plus_one >> 8) & 0xff;
		int blue = (plus_one) & 0xff;

		int lottery_for_all_the_BIG_MONNEY = (int) (Math.random() * TWOHUNDREDFIFTYFIVE);
		int lottery_for_all_the_GIB_MONNEY = (int) (Math.random() * TWOHUNDREDFIFTYFIVE);
		int lottery_for_all_the_XXX_MONNEY = (int) (Math.random() * TWOHUNDREDFIFTYFIVE);

		// System.out.println("/alpha/ /red/ /green/ /blue/
		// /lottery_for_all_the_BIG_MONNEY/ "+"/"+alpha+"/ /"+red+"/ /"+green+"/
		// /"+blue+"/ /"+lottery_for_all_the_BIG_MONNEY+"/ ");

		return new vector(red, green, blue, lottery_for_all_the_BIG_MONNEY, lottery_for_all_the_GIB_MONNEY,
				lottery_for_all_the_XXX_MONNEY, alpha);

	}

	public static ArrayList<tile> getTiles() {
		return BoardUtils.getTiles();
	}

}
