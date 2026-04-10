package game;


public class GameStats {

    // === GAME COUNTERS ===
    public static int whiteWins = 0;
    public static int blackWins = 0;
    public static int draws = 0;

    public static int gamesPlayed = 0;

    // === MOVE COUNTS ===
    public static int totalMoves = 0;
    public static int whiteMoves = 0;
    public static int blackMoves = 0;

    // === TIMING ===
    public static long turnStartTime = 0;
    public static long longestTurnMs = 0;
    public static long shortestTurnMs = Long.MAX_VALUE;
    public static long totalTurnTime = 0;

    // === GAME LENGTH & HISTORY ===
    public static int currentGameTurnCount = 0;
    public static int longestGameTurns = 0;
    public static int shortestGameTurns = Integer.MAX_VALUE;

    // === SPECIAL EVENTS ===
    public static int checksGivenWhite = 0;
    public static int checksGivenBlack = 0;

    public static int capturesWhite = 0;
    public static int capturesBlack = 0;

    public static int promotions = 0;

    public static int illegalMoveAttempts = 0;
    public static int turnTimeoutsWhite = 0;
    public static int turnTimeoutsBlack = 0;
    public static int consecutiveTurnTimeoutsWhite = 0;
    public static int consecutiveTurnTimeoutsBlack = 0;

    // 50-move rule counter
    public static int movesSinceLastCaptureOrPawnMove = 0;
	public static String totalGames;
	static int longestGameLength;
	static int shortestGameLength;

	public static void recordGameResult(String result, int turns) {

//		gamesPlayed++;

        switch (result) {
            case "WHITE_WINS" -> whiteWins++;
            case "BLACK_WINS" -> blackWins++;
            case "DRAW" -> draws++;
        }

        if (turns > longestGameLength)
            longestGameLength = turns;

        if (turns>=0&&turns < longestGameLength)
            shortestGameLength = turns;
    }
    // ===============================
    //  RESET AT GAME START
    // ===============================
    public static void startNewGame() {
        currentGameTurnCount = 0;
        movesSinceLastCaptureOrPawnMove = 0;

        consecutiveTurnTimeoutsWhite = 0;
        consecutiveTurnTimeoutsBlack = 0;

        turnStartTime = System.currentTimeMillis();
    }


    // ========================================
    //   TURN TRACKING
    // ========================================
    public static void startTurn() {
        turnStartTime = System.currentTimeMillis();
    }

    public static void finishTurn(boolean whiteTurnJustPlayed) {
        long dt = System.currentTimeMillis() - turnStartTime;

        totalTurnTime += dt;
        if (dt > longestTurnMs) longestTurnMs = dt;
        if (dt < shortestTurnMs) shortestTurnMs = dt;

        totalMoves++;
        currentGameTurnCount++;

        if (whiteTurnJustPlayed) whiteMoves++;
        else blackMoves++;
    }


    // ========================================
    //   CAPTURES / PROMOTIONS
    // ========================================
    public static void registerCapture(boolean byWhite) {
        movesSinceLastCaptureOrPawnMove = 0;
        if (byWhite) capturesWhite++;
        else capturesBlack++;
    }

    public static void registerPawnMove() {
        movesSinceLastCaptureOrPawnMove = 0;
    }

    public static void registerPromotion() {
        promotions++;
    }

    // ========================================
    //   CHECKS
    // ========================================
    public static void registerCheck(boolean byWhite) {
        if (byWhite) checksGivenWhite++;
        else checksGivenBlack++;
    }


    // ========================================
    //   TIMEOUT CONDITIONS
    // ========================================
    public static void registerTurnTimeout(boolean whiteTurn) {
        if (whiteTurn) {
            turnTimeoutsWhite++;
            consecutiveTurnTimeoutsWhite++;
            consecutiveTurnTimeoutsBlack = 0;
        } else {
            turnTimeoutsBlack++;
            consecutiveTurnTimeoutsBlack++;
            consecutiveTurnTimeoutsWhite = 0;
        }
    }


    // ========================================
    //   GAME END
    // ========================================
    public static void registerGameEnd(String reason, boolean whiteWon, boolean blackWon) {
        gamesPlayed++;

        // Win / loss / draw tracking
        if (whiteWon) whiteWins++;
        else if (blackWon) blackWins++;
        else draws++;

        // Game-length tracking
        longestGameTurns = Math.max(longestGameTurns, currentGameTurnCount);
        shortestGameTurns = Math.min(shortestGameTurns, currentGameTurnCount);

        System.out.println("=== GAME OVER ===");
        System.out.println("Reason: " + reason);
        System.out.println("Turns: " + currentGameTurnCount);
        System.out.println("=================");

        // prepare for next game
        startNewGame();
    }

}
