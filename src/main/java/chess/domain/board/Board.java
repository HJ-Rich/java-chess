package chess.domain.board;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import java.util.Map;

public final class Board {
    private static final int TWO_KINGS_MEAN_NOT_FINISHED = 2;

    private final Map<Position, Piece> board;

    Board(Map<Position, Piece> board) {
        this.board = board;
    }

    public boolean move(Position from, Position to) {
        final Piece piece = board.get(from);
        if (piece.movable(from, to, this)) {
            board.put(to, piece);
            board.remove(from);
            return true;
        }

        return false;
    }

    public Map<Color, Double> getScore() {
        //TODO 점수 계산
        return null;
    }

    public boolean isFinished() {
        return board.values()
                .stream()
                .filter(Piece::isKing)
                .count() < TWO_KINGS_MEAN_NOT_FINISHED;
    }

    public boolean isSameColorOnTarget(Piece source, Position target) {
        return source.isSameColor(board.get(target));
    }

    public boolean isEnemyOnTarget(Piece source, Position target) {
        return source.isEnemy(board.get(target));
    }

    public boolean exists(Position position) {
        return board.containsKey(position);
    }

    public Map<Position, Piece> getBoard() {
        return board;
    }

    public boolean isTurnOf(Position from, Color currentTurn) {
        return board.get(from).isSameColor(currentTurn);
    }
}
