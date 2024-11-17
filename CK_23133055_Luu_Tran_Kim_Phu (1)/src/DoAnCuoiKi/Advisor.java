package DoAnCuoiKi;

import java.util.ArrayList;
import java.util.List;

public class Advisor extends Piece {

	public Advisor(boolean isWhite, Position position) {
		super(isWhite, position);
	}

	@Override
	public boolean isValidMove(Position newPosition, ChessBoard board) {
		// Kiểm tra xem vị trí có nằm trong giới hạn không
		if (newPosition.getRow() < 0 || newPosition.getRow() > 9 || newPosition.getCol() < 0
				|| newPosition.getCol() > 8) {
			throw new IllegalArgumentException("Error: (0 <= Row <= 9) and (0 <= Column <= 8)");
		}

		Position from = this.getPosition(); // Lấy vị trí hiện tại của quân cờ

		int rowDiff = Math.abs(newPosition.getRow() - from.getRow());
		int colDiff = Math.abs(newPosition.getCol() - from.getCol());

		// Kiểm tra di chuyển một ô theo đường chéo
		if (rowDiff == 1 && colDiff == 1) {
			// Kiểm tra vùng di chuyển hợp lệ cho quân Trắng
			if (isWhite) {
				return newPosition.getRow() >= 0 && newPosition.getRow() <= 2 && newPosition.getCol() >= 3
						&& newPosition.getCol() <= 5 && (board.getPieceAt(newPosition) == null
								|| board.getPieceAt(newPosition).isWhite() != isWhite);
			}
			// Kiểm tra vùng di chuyển hợp lệ cho quân Đen
			else {
				return newPosition.getRow() >= 7 && newPosition.getRow() <= 9 && newPosition.getCol() >= 3
						&& newPosition.getCol() <= 5 && (board.getPieceAt(newPosition) == null
								|| board.getPieceAt(newPosition).isWhite() != isWhite);
			}
		}

		return false; // Không hợp lệ nếu không di chuyển theo quy tắc
	}

	@Override
	public String getSymbol() {
		return isWhite ? "Advis(1)" : "Advis(2)"; // "仕" cho quân trắng, "士" cho quân đen
	}

	@Override
	public String getName() {
		return isWhite ? "Sĩ Trắng" : "Sĩ Đen"; // Tên quân cờ
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
        StringBuilder result = new StringBuilder("Vị trí di chuyển hợp lệ của quân 'Advisor(" + color + ")': ");
        
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

	// Phương thức để in ra các vị trí có thể ăn quân
	public void printCapturePositions(ChessBoard board) {
		List<Position> capturePositions = new ArrayList<>();
		Position currentPosition = this.getPosition();

		// Kiểm tra các vị trí có thể ăn quân
		for (int row = -1; row <= 1; row++) {
			for (int col = -1; col <= 1; col++) {
				if (Math.abs(row) == Math.abs(col)) { // Chỉ kiểm tra vị trí chéo
					int newRow = currentPosition.getRow() + row;
					int newCol = currentPosition.getCol() + col;

					// Kiểm tra xem vị trí mới có nằm trong giới hạn không
					if (newRow >= 0 && newRow <= 9 && newCol >= 0 && newCol <= 8) {
						Position targetPosition = new Position(newRow, newCol);
						if (isValidMove(targetPosition, board)) {
							Piece targetPiece = board.getPieceAt(targetPosition);
							if (targetPiece != null && targetPiece.isWhite() != this.isWhite) {
								capturePositions.add(targetPosition);
							}
						}
					}
				}
			}
		}

		String color = isWhite ? "1" : "2"; 
        StringBuilder result = new StringBuilder("Các vị trí có thể ăn quân của 'Advisor(" + color + ")':");
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