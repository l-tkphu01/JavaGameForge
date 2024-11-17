package DoAnCuoiKi;

public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        if (row < 0 || row >= 10 || col < 0 || col >= 9) {
            throw new IllegalArgumentException("Error: (0 <= Row <= 9) and (0 <= Column <= 8).");
        }
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return 31 * row + col;
    }

    @Override
    public String toString() {
        return "(" + (row + 1) + ", " + (col + 1) + ")"; // Cộng 1 để hiển thị từ 1 thay vì 0
    }
}