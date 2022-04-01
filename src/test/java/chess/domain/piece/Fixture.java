package chess.domain.piece;

import chess.domain.board.Position;

public class Fixture {
    public static final Position E4 = Position.from("E4");
    public static final Bishop BISHOP_WHITE = new Bishop(Color.WHITE);
    public static final Rook ROOK_WHITE = new Rook(Color.WHITE);
    public static final Queen QUEEN_WHITE = new Queen(Color.WHITE);
}
