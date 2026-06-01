# BoardTemplate

The **hands** of the [Distributed Chess System](../). Receives a **move vector**, turns it
into a board square, checks the move is legal, and **renders** the resulting chess position.
This is where a stream of numbers becomes a played game.

## What it does

- `VectorToBoardTranslator` — maps a vector into **discrete board coordinates**: clamp into
  `[0,1]`, flip, scale to the grid, and *chaotically mix* so every digit affects every output
  coordinate (with the `v = 1.0` edge case handled).
- `VectorMoveValidator` — enforces piece movement rules, captures, and blocked/own-piece
  moves.
- `Board` — holds and draws the 8×8 state.
- `GameStats` — tracks the running game.
- `BoardMain` — the runnable that drives the board from vectors.

## Dependencies

- **JDK 17**, **Maven**. Standard library only — no third-party dependencies.

## Build & run

```bash
mvn compile

# entry point: BoardMain (package BoardTemplate.game)
java -cp target/classes BoardTemplate.game.BoardMain
```

## Structure

```
BoardTemplate/
├── pom.xml                                   JDK 17
└── BoardTemplate/game/
    ├── BoardMain.java                        runnable driver           [main]
    ├── Board.java                            8×8 state + rendering
    ├── BoardUtils.java                       board helpers
    ├── VectorToBoardTranslator.java          vector → board coordinates
    ├── VectorMoveValidator.java              legality / captures
    ├── GameStats.java                        game tracking
    └── vector.java                           vector type
```

---

*Part of the Distributed Chess System: the VectorServer relays a move vector here, and the
board makes it real. Checkmate is only a vector the board agreed to believe.*
