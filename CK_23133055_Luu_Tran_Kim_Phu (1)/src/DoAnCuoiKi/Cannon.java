	package DoAnCuoiKi;
	
	import java.util.ArrayList;
	import java.util.List;
	
	public class Cannon extends Piece {
	
		public Cannon(boolean isWhite, Position position) {
			super(isWhite, position);
		}
	
		@Override
		public boolean isValidMove(Position newPosition, ChessBoard board) {
	// Kiểm tra giới hạn bàn cờ
			if (newPosition.getRow() < 0 || newPosition.getRow() > 9 || newPosition.getCol() < 0
					|| newPosition.getCol() > 8) {
				throw new IllegalArgumentException("Error: (0 <= Row <= 9) and (0 <= Column <= 8)");
			}
	
			int rowDiff = newPosition.getRow() - position.getRow();
			int colDiff = newPosition.getCol() - position.getCol();
	
	// Kiểm tra di chuyển chỉ theo chiều dọc hoặc ngang
			if (rowDiff != 0 && colDiff != 0) {
				return false;
			}
	
			int count = 0;
			Piece target = board.getPieceAt(newPosition);
	
	// Kiểm tra đường đi
			if (rowDiff == 0) { // Di chuyển ngang
				int step = colDiff > 0 ? 1 : -1;
				for (int col = position.getCol() + step; col != newPosition.getCol(); col += step) {
					Piece piece = board.getPieceAt(new Position(position.getRow(), col));
					if (piece != null) {
						count++;
					}
				}
			} else { // Di chuyển dọc
				int step = rowDiff > 0 ? 1 : -1;
				for (int row = position.getRow() + step; row != newPosition.getRow(); row += step) {
					Piece piece = board.getPieceAt(new Position(row, position.getCol()));
					if (piece != null) {
						count++;
					}
				}
			}
	
	// Điều kiện cho phép pháo di chuyển
			boolean canJumpOverEnemy = count == 1 && target != null && target.isWhite() != this.isWhite; // Có thể nhảy qua
																											// quân đối
																											// phương
			boolean canMoveToEmpty = count == 0 && target == null; // Có thể di chuyển đến ô trống
	
			return canJumpOverEnemy || canMoveToEmpty;
		}
	
		@Override
		public String getSymbol() {
			return isWhite ? "Canno(1)" : "Canno(2)";
		}
	
		@Override
		public String getName() {
			return isWhite ? "Pháo Trắng" : "Pháo Đen";
		}
	
		@Override
		public List<Position> getValidMoves(ChessBoard board) {
			List<Position> validMoves = new ArrayList<>();
			for (int row = 0; row < 10; row++) {
				for (int col = 0; col < 9; col++) {
					Position newPosition = new Position(row, col);
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
			StringBuilder result = new StringBuilder("Vị trí di chuyển hợp lệ của quân 'Cannon(" + color + ")': ");
	
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
	
	// Kiểm tra các vị trí có thể ăn quân theo chiều ngang
			for (int col = 0; col < 9; col++) {
				if (col == currentPosition.getCol())
					continue; // Bỏ qua vị trí hiện tại
				Position targetPosition = new Position(currentPosition.getRow(), col);
				if (isValidMove(targetPosition, board)) {
					Piece targetPiece = board.getPieceAt(targetPosition);
					if (targetPiece != null && targetPiece.isWhite() != this.isWhite) {
						boolean hasFriendlyPiece = false;
						int count = 0;
						int step = col > currentPosition.getCol() ? 1 : -1;
	
						for (int c = currentPosition.getCol() + step; c != col; c += step) {
							Piece piece = board.getPieceAt(new Position(currentPosition.getRow(), c));
							if (piece != null) {
								count++;
								if (piece.isWhite() == this.isWhite) {
									hasFriendlyPiece = true;
								}
							}
						}
						if (count == 1 && !hasFriendlyPiece) {
							capturePositions.add(targetPosition);
						}
					}
				}
			}
	
	// Kiểm tra các vị trí có thể ăn quân theo chiều dọc
			for (int row = 0; row < 10; row++) {
				if (row == currentPosition.getRow())
					continue; // Bỏ qua vị trí hiện tại
				Position targetPosition = new Position(row, currentPosition.getCol());
				if (isValidMove(targetPosition, board)) {
					Piece targetPiece = board.getPieceAt(targetPosition);
					if (targetPiece != null && targetPiece.isWhite() != this.isWhite) {
						boolean hasFriendlyPiece = false;
						int count = 0;
						int step = row > currentPosition.getRow() ? 1 : -1;
	
						for (int r = currentPosition.getRow() + step; r != row; r += step) {
							Piece piece = board.getPieceAt(new Position(r, currentPosition.getCol()));
							if (piece != null) {
								count++;
								if (piece.isWhite() == this.isWhite) {
									hasFriendlyPiece = true;
								}
							}
						}
						if (count == 1 && !hasFriendlyPiece) {
							capturePositions.add(targetPosition);
						}
					}
				}
			}
	
			String color = isWhite ? "1" : "2";
			StringBuilder result = new StringBuilder("Các vị trí có thể ăn quân của 'Cannon(" + color + ")':");
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