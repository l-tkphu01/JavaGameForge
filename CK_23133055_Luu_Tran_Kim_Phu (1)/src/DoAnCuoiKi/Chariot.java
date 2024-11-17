package DoAnCuoiKi;

import java.util.ArrayList;
import java.util.List;

public class Chariot extends Piece {
    public Chariot(boolean isWhite, Position position) {
        super(isWhite, position);
    }

    @Override
    public boolean isValidMove(Position newPosition, ChessBoard board) {
        // Kiểm tra nếu vị trí mới vượt ra ngoài bàn cờ
        if (newPosition.getRow() < 0 || newPosition.getRow() > 9 || newPosition.getCol() < 0
                || newPosition.getCol() > 8) {
            throw new IllegalArgumentException("Error: (0 <= Row <= 9) and (0 <= Column <= 8)");
        }

        Position from = this.getPosition(); // Lấy vị trí hiện tại của quân cờ

        int rowDiff = newPosition.getRow() - from.getRow();
        int colDiff = newPosition.getCol() - from.getCol();

        // Kiểm tra di chuyển chỉ theo chiều dọc hoặc ngang
        if (rowDiff != 0 && colDiff != 0) {
            return false; // Không phải di chuyển theo chiều dọc hay ngang
        }

        // Kiểm tra xem ô đích có quân của chính mình không
        Piece targetPiece = board.getPieceAt(newPosition);
        if (targetPiece != null && targetPiece.isWhite() == this.isWhite()) {
            return false; // Không thể di chuyển đến ô đã có quân đồng minh
        }

        // Kiểm tra không có quân cản trên đường đi
        if (rowDiff == 0) { // Di chuyển ngang
            int step = colDiff > 0 ? 1 : -1;
            for (int col = from.getCol() + step; col != newPosition.getCol(); col += step) {
                if (board.getPieceAt(new Position(from.getRow(), col)) != null) {
                    return false; // Nếu có quân cản, không hợp lệ
                }
            }
        } else if (colDiff == 0) { // Di chuyển dọc
            int step = rowDiff > 0 ? 1 : -1;
            for (int row = from.getRow() + step; row != newPosition.getRow(); row += step) {
                if (board.getPieceAt(new Position(row, from.getCol())) != null) {
                    return false; // Nếu có quân cản, không hợp lệ
                }
            }
        }

        // Nếu không có quân cản, nước đi hợp lệ
        return true;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "Chari(1)" : "Chari(2)"; // "俥" cho quân trắng, "車" cho quân đen.
    }

    @Override
    public String getName() {
        return isWhite ? "Xe Trắng" : "Xe Đen"; // Tên quân cờ
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
        StringBuilder result = new StringBuilder("Vị trí di chuyển hợp lệ của quân 'Chariot(" + color + ")': ");
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
        printCapturePositions(board); // Gọi để in ra các vị trí có thể ăn quân
    }

    // Phương thức để kiểm tra ăn quân đối phương theo chiều dọc
    public boolean canCaptureVertically(ChessBoard board) {
        Position currentPosition = this.getPosition();

        // Kiểm tra ô phía trên
        for (int row = currentPosition.getRow() - 1; row >= 0; row--) {
            Position targetPosition = new Position(row, currentPosition.getCol());
            Piece targetPiece = board.getPieceAt(targetPosition);
            if (targetPiece != null) {
                if (targetPiece.isWhite() != this.isWhite()) {
                    return true; // Có quân đối phương
                }
                break; // Dừng lại nếu gặp quân đồng minh
            }
        }

        // Kiểm tra ô phía dưới
        for (int row = currentPosition.getRow() + 1; row <= 9; row++) {
            Position targetPosition = new Position(row, currentPosition.getCol());
            Piece targetPiece = board.getPieceAt(targetPosition);
            if (targetPiece != null) {
                if (targetPiece.isWhite() != this.isWhite()) {
                    return true; // Có quân đối phương
                }
                break; // Dừng lại nếu gặp quân đồng minh
            }
        }

        return false; // Không có quân đối phương nào để ăn
    }

    // Phương thức để in ra các vị trí có thể ăn quân
    public void printCapturePositions(ChessBoard board) {
        List<Position> capturePositions = new ArrayList<>();
        Position currentPosition = this.getPosition();

        // Kiểm tra các ô phía trên
        for (int row = currentPosition.getRow() - 1; row >= 0; row--) {
            Position targetPosition = new Position(row, currentPosition.getCol());
            Piece targetPiece = board.getPieceAt(targetPosition);
            if (targetPiece != null) {
                if (targetPiece.isWhite() != this.isWhite()) {
                    capturePositions.add(targetPosition); // Thêm vào nếu có quân đối phương
                }
                break; // Dừng lại khi gặp quân
            }
        }

        // Kiểm tra các ô phía dưới
        for (int row = currentPosition.getRow() + 1; row <= 9; row++) {
            Position targetPosition = new Position(row, currentPosition.getCol());
            Piece targetPiece = board.getPieceAt(targetPosition);
            if (targetPiece != null) {
                if (targetPiece.isWhite() != this.isWhite()) {
                    capturePositions.add(targetPosition); // Thêm vào nếu có quân đối phương
                }
                break; // Dừng lại khi gặp quân
            }
        }

        String color = isWhite ? "1" : "2"; 
        StringBuilder result = new StringBuilder("Các vị trí có thể ăn quân của 'Chariot(" + color + ")':");
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