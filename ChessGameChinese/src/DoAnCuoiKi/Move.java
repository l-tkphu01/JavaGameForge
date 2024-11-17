package DoAnCuoiKi;

public class Move {
	private Piece piece; // Quân cờ đang di chuyển
	private Position start; // Vị trí bắt đầu
	private Position end; // Vị trí kết thúc
	private Piece capturedPiece; // Quân cờ bị ăn, nếu có

	// Constructor
	public Move(Piece piece, Position start, Position end, Piece capturedPiece) {
		this.piece = piece;
		this.start = start;
		this.end = end;
		this.capturedPiece = capturedPiece;
	}

	// Getter cho các thuộc tính
	public Piece getPiece() {
		return piece;
	}

	public Position getStart() {
		return start;
	}

	public Position getEnd() {
		return end;
	}

	public Piece getCapturedPiece() {
		return capturedPiece;
	}

	// Kiểm tra xem nước đi có ăn quân hay không
	public boolean isCapture() {
		return capturedPiece != null;
	}

	// Phương thức toString để ghi lại nước đi ở dạng chuỗi
	@Override
	public String toString() {
		String captureInfo = isCapture() ? "x " + capturedPiece : "";
		return piece + " moves from " + start + " to " + end + " " + captureInfo;
	}
}