package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.board.Position;
import java.util.List;

public final class Rook extends Piece {
    private static final List<Integer> ROOK_ANGLES = List.of(90, 0, -90, 180);

    public Rook(Color color) {
        super(color);
    }

    @Override
    public boolean movable(Position from, Position to, Board board) {
        try {
            validateAngle(ROOK_ANGLES, from, to);   // 1. 각도 확인
            validatePieceOnWay(from, to, board);    // 2. 중간 기물 확인
            validateTarget(board, to);              // 3. 목표 기물 확인
            return true;
        } catch (IllegalStateException exception) {
            return false;
        }
    }
}
