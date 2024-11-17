package DoAnCuoiKi;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

	public King(boolean isWhite, Position position) {
		super(isWhite, position);
	}

	@Override
	public boolean isValidMove(Position newPosition, ChessBoard board) {
		// Kiểm tra xem vị trí có nằm trong giới hạn không
		if (newPosition.getRow() < 0 || newPosition.getRow() > 9 || newPosition.getCol() < 0
				|| newPosition.getCol() > 8) {
			throw new IllegalArgumentException("Error: (0 <= Row <= 9) and (0 <= Column <= 8)");
		}

		int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
		int colDiff = Math.abs(newPosition.getCol() - position.getCol());

		// Tướng chỉ có thể di chuyển 1 ô theo chiều ngang hoặc dọc, không cho phép đi
		// chéo
		if ((rowDiff > 1 || colDiff > 1) || (rowDiff == 0 && colDiff == 0) || (rowDiff > 0 && colDiff > 0)) {
			return false; // Không hợp lệ nếu không đáp ứng quy tắc di chuyển của Tướng
		}

		// Kiểm tra phạm vi cung của Tướng
		if (isWhite) {
			if (newPosition.getRow() < 0 || newPosition.getRow() > 2 || newPosition.getCol() < 3
					|| newPosition.getCol() > 5) {
				return false; // Ra ngoài phạm vi cung
			}
		} else {
			if (newPosition.getRow() < 7 || newPosition.getRow() > 9 || newPosition.getCol() < 3
					|| newPosition.getCol() > 5) {
				return false; // Ra ngoài phạm vi cung
			}
		}

		// Kiểm tra ô đích không có quân của chính mình
		Piece pieceAtNewPosition = board.getPieceAt(newPosition);
		if (pieceAtNewPosition != null && pieceAtNewPosition.isWhite() == this.isWhite()) {
			return false; // Không thể ăn quân của chính mình
		}

		return true; // Di chuyển hợp lệ
	}

	@Override
	public String getSymbol() {
		// Ký hiệu quân Tướng: "帥" cho quân trắng và "將" cho quân đen
		return isWhite ? "King (1)" : "King (2)";
	}

	@Override
	public String getName() {
		return isWhite ? "Tướng Trắng" : "Tướng Đen"; // Tên quân cờ
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
		StringBuilder result = new StringBuilder("Vị trí di chuyển hợp lệ của quân 'King(" + color + ")': ");

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
		for (int rowDiff = -1; rowDiff <= 1; rowDiff++) {
			for (int colDiff = -1; colDiff <= 1; colDiff++) {
				// Tướng chỉ có thể di chuyển 1 ô, không đi chéo
				if ((Math.abs(rowDiff) + Math.abs(colDiff)) == 1) {
					int newRow = currentPosition.getRow() + rowDiff;
					int newCol = currentPosition.getCol() + colDiff;

					// Kiểm tra xem vị trí mới có nằm trong giới hạn không
					if (newRow >= 0 && newRow <= 9 && newCol >= 0 && newCol <= 8) {
						Position targetPosition = new Position(newRow, newCol);
						Piece targetPiece = board.getPieceAt(targetPosition);
						if (targetPiece != null && targetPiece.isWhite() != this.isWhite) {
							// Nếu có quân đối phương, thêm vào danh sách
							capturePositions.add(targetPosition);
						}
					}
				}
			}
		}

		String color = isWhite ? "1" : "2";
		StringBuilder result = new StringBuilder("Các vị trí có thể ăn quân của 'King(" + color + ")':");
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