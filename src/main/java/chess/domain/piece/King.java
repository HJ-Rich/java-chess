package chess.domain.piece;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.view.console.OutputView;

public final class King extends Piece {
    private static final int MAXIMUM_DISTANCE_OF_KING = 1;
    private static final double SCORE = 0.0;
    private static final String EXCEEDED_MAXIMUM_DISTANCE_OF_KING = "King의 이동 범위를 초과했습니다";

    public King(Color color) {
        super(color);
    }

    @Override
    public boolean movable(Position from, Position to, Board board) {
        try {
            validateDistance(from, to); // 1. dx,dy로 이동 가능한 8점 확인
            validateTarget(board, to);  // 2. 목표 기물 확인
            return true;
        } catch (IllegalStateException exception) {
            OutputView.printError(exception);
            return false;
        }
    }

    @Override
    public double getScore() {
        return SCORE;
    }

    private void validateDistance(Position from, Position to) {
        if (Math.abs(from.dx(to)) > MAXIMUM_DISTANCE_OF_KING || Math.abs(from.dy(to)) > MAXIMUM_DISTANCE_OF_KING) {
            throw new IllegalStateException(EXCEEDED_MAXIMUM_DISTANCE_OF_KING);
        }
    }

    @Override
    public boolean isKing() {
        return true;
    }
}
