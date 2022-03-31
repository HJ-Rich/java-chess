package chess.domain.piece;

import chess.domain.Color;

public final class Rook extends Piece {
    public Rook(Color color) {
        super(color);
    }

    @Override
    public boolean isRook() {
        return true;
    }
}
