package game;


public enum Color { WHITE, BLACK;
        public Color opp() { return this == WHITE ? BLACK : WHITE; }
    }