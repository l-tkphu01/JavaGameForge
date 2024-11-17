package DoAnCuoiKi;

import java.util.ArrayList;
import java.util.List;

public class Soldier extends Piece {

	public Soldier(boolean isWhite, Position position) {
		super(isWhite, position);
	}

	@Override
	public boolean isValidMove(Position newPosition, ChessBoard board) {
		// Kiểm tra xem vị trí có nằm trong giới hạn không
		if (newPosition.getRow() < 0 || newPosition.getRow() > 9 || newPosition.getCol() < 0
				|| newPosition.getCol() > 8) {
			throw new IllegalArgumentException("Error: (0 <= Row <= 9) and (0 <= Column <= 8)");
		}

		// Lấy vị trí hiện tại của quân Tốt
		int currentRow = position.getRow();
		int currentCol = position.getCol();

		// Nếu quân Tốt đang ở hàng chưa qua sông
		if ((currentRow <= 4 && isWhite) || (currentRow >= 5 && !isWhite)) {
			// Chỉ có thể di chuyển thẳng
			if (newPosition.getRow() == currentRow + (isWhite ? 1 : -1) && newPosition.getCol() == currentCol) {
				Position frontPosition = new Position(currentRow + (isWhite ? 1 : -1), currentCol);
				Piece pieceAtFront = board.getPieceAt(frontPosition);
				// Nếu ô phía trước trống, di chuyển hợp lệ
				if (pieceAtFront == null) {
					return true; // Di chuyển thẳng hợp lệ
				} else if (pieceAtFront.isWhite() != this.isWhite) {
					return true; // Ăn quân đối phương
				}
			}
			return false; // Không di chuyển nếu có quân của mình ở phía trước
		}

		// Nếu đã qua sông
		if ((isWhite && currentRow > 4) || (!isWhite && currentRow < 5)) {
			// Ngăn không cho di chuyển lùi
			if ((isWhite && newPosition.getRow() < currentRow) || (!isWhite && newPosition.getRow() > currentRow)) {
				return false; // Không thể di chuyển lùi
			}

			// Di chuyển thẳng
			if (newPosition.getRow() == currentRow + (isWhite ? 1 : -1) && newPosition.getCol() == currentCol) {
				Position frontPosition = new Position(currentRow + (isWhite ? 1 : -1), currentCol);
				Piece pieceAtFront = board.getPieceAt(frontPosition);
				if (pieceAtFront == null) {
					return true; // Di chuyển thẳng hợp lệ
				} else if (pieceAtFront.isWhite() != this.isWhite) {
					return true; // Ăn quân đối phương
				}
			}

			// Di chuyển sang trái
			if (newPosition.getRow() == currentRow && newPosition.getCol() == currentCol - 1) {
				Position leftPosition = new Position(currentRow, currentCol - 1);
				Piece leftPiece = board.getPieceAt(leftPosition);
				if (leftPiece == null) {
					return true; // Di chuyển sang trái hợp lệ
				} else if (leftPiece.isWhite() != this.isWhite) {
					return true; // Ăn quân đối phương bên trái
				}
			}

			// Di chuyển sang phải
			if (newPosition.getRow() == currentRow && newPosition.getCol() == currentCol + 1) {
				Position rightPosition = new Position(currentRow, currentCol + 1);
				Piece rightPiece = board.getPieceAt(rightPosition);
				if (rightPiece == null) {
					return true; // Di chuyển sang phải hợp lệ
				} else if (rightPiece.isWhite() != this.isWhite) {
					return true; // Ăn quân đối phương bên phải
				}
			}
		}

		return false; // Di chuyển không hợp lệ
	}

	@Override
	public String getSymbol() {
		return isWhite ? "Soldi(1)" : "Soldi(2)"; // "兵" cho Tốt trắng, "卒" cho Tốt đen.
	}

	@Override
	public String getName() {
		return isWhite ? "Tốt Trắng" : "Tốt Đen"; // Tên quân cờ
	}

	@Override
	public List<Position> getValidMoves(ChessBoard board) {
		List<Position> validMoves = new ArrayList<>();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 9; col++) {
				Position newPosition = new Position(row, col);
				if (isValidMove(newPosition, board)) {
					validMoves.add(newPosition);
				}
			}
		}
		return validMoves;
	}

	public void printValidMoves(ChessBoard board) {
		List<Position> validMoves = getValidMoves(board);
		String color = isWhite ? "1" : "2"; // Xác định màu của quân
		StringBuilder result = new StringBuilder("Vị trí di chuyển hợp lệ của quân 'Soldier(" + color + ")': ");

		if (validMoves.isEmpty()) {
			result.append("Không có nước đi hợp lệ.");
		} else {
			result.append("[");
			for (int i = 0; i < validMoves.size(); i++) {
				Position pos = validMoves.get(i);
				result.append("(").append(pos.getRow() + 1).append(", ").append(pos.getCol() + 1).append(")");
				if (i < validMoves.size() - 1) {
					result.append(", "); // Thêm dấu phẩy giữa các vị trí
				}
			}
			result.append("]");
		}

		// Xuất chuỗi kết quả
		System.out.println(result.toString());
		printCapturePositions(board); // Gọi để in ra các vị trí có thể ăn quân
	}

	// Phương thức để in ra các vị trí có thể ăn quân
	public void printCapturePositions(ChessBoard board) {
		List<Position> capturePositions = new ArrayList<>();
		Position currentPosition = this.getPosition();
		int currentRow = currentPosition.getRow();
		int currentCol = currentPosition.getCol();

		// Kiểm tra ô phía trước
		Position frontPosition = new Position(currentRow + (isWhite ? 1 : -1), currentCol);
		Piece pieceAtFront = board.getPieceAt(frontPosition);
		if (pieceAtFront != null && pieceAtFront.isWhite() != this.isWhite) {
			capturePositions.add(frontPosition); // Ăn quân đối phương ở ô phía trước
		}

		// Kiểm tra các ô bên trái
		if (currentCol > 0) { // Vị trí bên trái
			Position leftPosition = new Position(currentRow, currentCol - 1);
			Piece leftPiece = board.getPieceAt(leftPosition);
			if (leftPiece != null && leftPiece.isWhite() != this.isWhite) {
				capturePositions.add(leftPosition); // Ăn quân đối phương bên trái
			}
		}

		// Kiểm tra các ô bên phải
		if (currentCol < 8) { // Vị trí bên phải
			Position rightPosition = new Position(currentRow, currentCol + 1);
			Piece rightPiece = board.getPieceAt(rightPosition);
			if (rightPiece != null && rightPiece.isWhite() != this.isWhite) {
				capturePositions.add(rightPosition); // Ăn quân đối phương bên phải
			}
		}

		String color = isWhite ? "1" : "2";
		StringBuilder result = new StringBuilder("Các vị trí có thể ăn quân của 'Soldier(" + color + ")':");
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