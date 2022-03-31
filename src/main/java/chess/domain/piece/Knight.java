package chess.domain.piece;

import chess.domain.Color;

public final class Knight extends Piece {
    public Knight(Color color) {
        super(color);
    }

    @Override
    public boolean isKnight() {
        return true;
    }
}
