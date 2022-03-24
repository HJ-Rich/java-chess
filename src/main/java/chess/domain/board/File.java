package chess.domain.board;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public enum File {
    A("A", 1),
    B("B", 2),
    C("C", 3),
    D("D", 4),
    E("E", 5),
    F("F", 6),
    G("G", 7),
    H("H", 8),
    ;

    private static final String INVALID_RANGE = "유효하지 않은 범위입니다.";

    private final String value;
    private final int order;

    File(String value, int order) {
        this.value = value;
        this.order = order;
    }

    public static File of(String value) {
        return Arrays.stream(values())
                .filter(file -> file.value.equalsIgnoreCase(value))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(INVALID_RANGE));
    }

    public static List<File> getBetween(File from, File to) {
        File maxOrder = getHigherOrder(from, to);
        File minOrder = getLowerOrder(from, to);

        return Arrays.stream(values())
                .filter(file -> file.order < maxOrder.order && file.order > minOrder.order)
                .collect(Collectors.toList());
    }

    private static File getHigherOrder(File from, File to) {
        if (from.order > to.order) {
            return from;
        }
        return to;
    }

    private static File getLowerOrder(File from, File to) {
        if (from.order < to.order) {
            return from;
        }
        return to;
    }

    public int calculate(File other) {
        return Math.abs(order - other.order);
    }
}