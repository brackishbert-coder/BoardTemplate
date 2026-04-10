package game;

public final class Piece {
        public final PieceType type;
        public final Color color;
        public Piece(PieceType t, Color c){ this.type=t; this.color=c; }
        public String toString(){ return color==Color.WHITE ? type.name() : type.name().toLowerCase(); }
    }