package chess.domain.board;

import chess.domain.Color;
import chess.domain.MoveResult;
import chess.domain.piece.InvalidPiece;
import chess.domain.piece.King;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Board {
    private final Map<Position, Piece> board;
    private Color colorForTurn = Color.WHITE;

    Board(Map<Position, Piece> board) {
        this.board = new LinkedHashMap<>(board);
    }

    public MoveResult move(Position from, Position to) {
        if (isInvalidTurn(from)) {
            return MoveResult.FAIL;
        }

        if (isNotMovable(from, to)) {
            return MoveResult.FAIL;
        }

        Piece pieceAtTo = movePiece(from, to);

        if (pieceAtTo.isKing()) {
            return MoveResult.ENDED;
        }

        colorForTurn = Color.opposite(colorForTurn);
        return MoveResult.SUCCESS;
    }

    private boolean isInvalidTurn(Position from) {
        final Piece piece = board.getOrDefault(from, InvalidPiece.getInstance());
        return piece.isDifferentColor(colorForTurn);
    }

    private boolean isNotMovable(Position from, Position to) {
        Piece pieceAtFrom = board.getOrDefault(from, InvalidPiece.getInstance());
        Piece pieceAtTo = board.getOrDefault(to, InvalidPiece.getInstance());

        return pieceAtFrom.isInValid()
                || !pieceAtFrom.movable(from.calculateDistance(to), pieceAtTo)
                || !pieceAtFrom.isKnight() && isPieceOnTheWay(from, to);
    }

    private Piece movePiece(Position from, Position to) {
        Piece pieceAtFrom = board.getOrDefault(from, InvalidPiece.getInstance());
        Piece pieceAtTo = board.getOrDefault(to, InvalidPiece.getInstance());

        board.put(to, pieceAtFrom);
        board.remove(from);

        return pieceAtTo;
    }

    public MoveResult move(String from, String to) {
        if (isNotMovable(from, to)) {
            return MoveResult.FAIL;
        }

        Piece pieceAtTo = movePiece(from, to);

        if (pieceAtTo instanceof King) {
            return MoveResult.ENDED;
        }

        return MoveResult.SUCCESS;
    }

    private boolean isNotMovable(String from, String to) {
        Position fromPosition = Position.from(from);
        Position toPosition = Position.from(to);

        Piece pieceAtFrom = board.getOrDefault(fromPosition, InvalidPiece.getInstance());
        Piece pieceAtTo = board.getOrDefault(toPosition, InvalidPiece.getInstance());

        return pieceAtFrom.isInValid()
                || !pieceAtFrom.movable(fromPosition.calculateDistance(toPosition), pieceAtTo)
                || !pieceAtFrom.isKnight() && isPieceOnTheWay(fromPosition, toPosition);
    }

    private Piece movePiece(String from, String to) {
        Position fromPosition = Position.from(from);
        Position toPosition = Position.from(to);

        Piece pieceAtFrom = board.getOrDefault(fromPosition, InvalidPiece.getInstance());
        Piece pieceAtTo = board.getOrDefault(toPosition, InvalidPiece.getInstance());

        board.put(toPosition, pieceAtFrom);
        board.remove(fromPosition);

        return pieceAtTo;
    }

    private boolean isPieceOnTheWay(Position fromPosition, Position toPosition) {
        List<Position> positionsOnTheWay = fromPosition.getPositionBetween(toPosition);

        return positionsOnTheWay.stream()
                .anyMatch(board::containsKey);
    }

    public Map<Color, Double> getScore() {
        List<Position> whitePawnPositions = getCollect(Piece::isWhite);
        List<Position> blackPawnPositions = getCollect(Piece::isBlack);

        double scoreForWhitePawns = calculatePawnScore(whitePawnPositions);
        double scoreForBlackPawns = calculatePawnScore(blackPawnPositions);

        return calculateScore(scoreForWhitePawns, scoreForBlackPawns);
    }

    private List<Position> getCollect(Predicate<Piece> condition) {
        return board.entrySet()
                .stream()
                .filter(entry -> entry.getValue() instanceof Pawn)
                .filter(entry -> condition.test(entry.getValue()))
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

    private double calculatePawnScore(List<Position> pawnPositions) {
        double pawnScore = pawnPositions.size();

        for (File file : File.values()) {
            long count = pawnPositions.stream()
                    .filter(position -> position.isSameFile(file))
                    .count();

            if (count > 1) {
                pawnScore -= count * 0.5;
            }
        }

        return pawnScore;
    }

    private Map<Color, Double> calculateScore(double scoreForWhitePawns, double scoreForBlackPawns) {
        Map<Color, Double> score = new HashMap<>();

        final List<Piece> piecesOnBoard = new ArrayList<>(board.values());
        score.put(Color.WHITE, Score.calculateScore(piecesOnBoard, Color.WHITE));
        score.put(Color.BLACK, Score.calculateScore(piecesOnBoard, Color.BLACK));

        return score;
    }

    public Map<Position, Piece> getBoard() {
        return new LinkedHashMap<>(board);
    }
}
