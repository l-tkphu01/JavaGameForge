package DoAnCuoiKi;

import java.util.ArrayList;
import java.util.List;

public class Elephant extends Piece {
	public Elephant(boolean isWhite, Position position) {
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

		// Tượng chỉ có thể đi chéo hai ô
		if (rowDiff == 2 && colDiff == 2) {
			// Kiểm tra xem Tượng có bị vượt sông không
			if (isWhite && newPosition.getRow() > 4) {
				return false; // Tượng trắng không được qua sông
			}
			if (!isWhite && newPosition.getRow() < 5) {
				return false; // Tượng đen không được qua sông
			}

			// Xác định vị trí trung gian để kiểm tra có bị chặn không
			int midRow = (position.getRow() + newPosition.getRow()) / 2;
			int midCol = (position.getCol() + newPosition.getCol()) / 2;

			// Kiểm tra vị trí trung gian có bị chặn không
			if (board.getPieceAt(new Position(midRow, midCol)) != null) {
				return false; // Có quân cản ở giữa đường đi
			}
			return true; // Nước đi hợp lệ
		}
		return false; // Không đi chéo hai ô, nên không hợp lệ
	}

	@Override
	public String getSymbol() {
		return isWhite ? "Eleph(1)" : "Eleph(2)"; // "相" cho quân trắng, "象" cho quân đen.
	}

	@Override
	public String getName() {
		return isWhite ? "Tượng Trắng" : "Tượng Đen"; // Tên quân cờ
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
        StringBuilder result = new StringBuilder("Vị trí di chuyển hợp lệ của quân 'Elephant(" + color + ")': ");

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
		for (int row = -2; row <= 2; row += 2) {
			for (int col = -2; col <= 2; col += 2) {
				if (row != 0 && col != 0) { // Chỉ kiểm tra các vị trí chéo
					int newRow = currentPosition.getRow() + row;
					int newCol = currentPosition.getCol() + col;

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
        StringBuilder result = new StringBuilder("Các vị trí có thể ăn quân của 'Elephant(" + color + ")':");
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