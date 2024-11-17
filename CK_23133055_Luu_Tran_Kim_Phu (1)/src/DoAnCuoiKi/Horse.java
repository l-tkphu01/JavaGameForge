package DoAnCuoiKi;

import java.util.ArrayList;
import java.util.List;

public class Horse extends Piece {
    public Horse(boolean isWhite, Position position) {
        super(isWhite, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, ChessBoard board) {
        // Kiểm tra xem vị trí có nằm trong giới hạn không
        if (newPosition.getRow() < 0 || newPosition.getRow() > 9 ||
            newPosition.getCol() < 0 || newPosition.getCol() > 8) {
            throw new IllegalArgumentException("Error: (0 <= Row <= 9) and (0 <= Column <= 8)");
        }

        int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
        int colDiff = Math.abs(newPosition.getCol() - position.getCol());

        // Mã đi 2 ô ngang và 1 ô dọc hoặc 2 ô dọc và 1 ô ngang
        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            // Xác định vị trí chặn cản Mã
            int blockRow = position.getRow() + (rowDiff == 2 ? (newPosition.getRow() - position.getRow()) / 2 : 0);
            int blockCol = position.getCol() + (colDiff == 2 ? (newPosition.getCol() - position.getCol()) / 2 : 0);

            // Kiểm tra có quân cản ở vị trí chặn hay không
            if (board.getPieceAt(new Position(blockRow, blockCol)) == null) {
                Piece targetPiece = board.getPieceAt(newPosition);
                // Nếu ô đích có quân, chỉ cho phép di chuyển nếu đó là quân địch
                if (targetPiece == null || targetPiece.isWhite() != this.isWhite) {
                    return true; // Di chuyển hợp lệ
                }
            }
        }
        return false; // Không hợp lệ nếu không đáp ứng quy tắc di chuyển của Mã
    }

    @Override
    public String getSymbol() {
        return isWhite ? "Horse(1)" : "Horse(2)";
    }

    @Override
    public String getName() {
        return isWhite ? "Mã Trắng" : "Mã Đen";
    }

    @Override
    public List<Position> getValidMoves(ChessBoard board) {
        List<Position> validMoves = new ArrayList<>();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 9; col++) {
                Position newPosition = new Position(row, col);
                // Bắt ngoại lệ nếu vị trí không hợp lệ
                try {
                    if (isValidMove(newPosition, board)) {
                        validMoves.add(newPosition);
                    }
                } catch (IllegalArgumentException e) {
                    // Bỏ qua các vị trí không hợp lệ
                }
            }
        }
        return validMoves;
    }

    public void printValidMoves(ChessBoard board) {
        List<Position> validMoves = getValidMoves(board);
        String color = isWhite ? "1" : "2"; // Xác định màu của quân
        StringBuilder result = new StringBuilder("Vị trí di chuyển hợp lệ của quân 'Horse(" + color + ")': ");

        if (validMoves.isEmpty()) {
            result.append("Không có nước đi hợp lệ.");
        } else {
            result.append("[");
            for (int i = 0; i < validMoves.size(); i++) {
                Position pos = validMoves.get(i);
                result.append("(").append(pos.getRow() + 1).append(", ").append(pos.getCol() + 1).append(")");
                if (i < validMoves.size() - 1) {
                    result.append(", ");
                }
            }
            result.append("]");
        }

        System.out.println(result.toString());
        printCapturePositions(board);
    }

    public void printCapturePositions(ChessBoard board) {
        List<Position> capturePositions = new ArrayList<>();
        Position currentPosition = this.getPosition();

        for (int rowDiff = -2; rowDiff <= 2; rowDiff++) {
            for (int colDiff = -2; colDiff <= 2; colDiff++) {
                if ((Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 1) ||
                    (Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 2)) {
                    int newRow = currentPosition.getRow() + rowDiff;
                    int newCol = currentPosition.getCol() + colDiff;

                    if (newRow >= 0 && newRow <= 9 && newCol >= 0 && newCol <= 8) {
                        Position targetPosition = new Position(newRow, newCol);
                        Piece targetPiece = board.getPieceAt(targetPosition);
                        if (targetPiece != null && targetPiece.isWhite() != this.isWhite) {
                            capturePositions.add(targetPosition);
                        }
                    }
                }
            }
        }

        String color = isWhite ? "1" : "2"; 
        StringBuilder result = new StringBuilder("Các vị trí có thể ăn quân của 'Horse(" + color + ")':");
        if (capturePositions.isEmpty()) {
            result.append("Không có quân nào để ăn.");
        } else {
            result.append("[");
            for (int i = 0; i < capturePositions.size(); i++) {
                Position pos = capturePositions.get(i);
                result.append("(").append(pos.getRow() + 1).append(", ").append(pos.getCol() + 1).append(")");
                if (i < capturePositions.size() - 1) {
                    result.append(", ");
                }
            }
            result.append("]");
        }

        System.out.println(result.toString());
    }

    @Override
    protected int getIndex() {
        return isWhite ? 1 : 2;
    }
}