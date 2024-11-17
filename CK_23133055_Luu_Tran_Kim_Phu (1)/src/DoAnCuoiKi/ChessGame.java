package DoAnCuoiKi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChessGame {
	private ChessBoard board;
	private boolean isWhiteTurn;
	private List<String> moveHistory;

	public ChessGame() {
		board = new ChessBoard();
		isWhiteTurn = true; // Trắng đi trước
		moveHistory = new ArrayList<>();
	}

	public void startGame() {
	    Scanner scanner = new Scanner(System.in);
	    boolean gameEnded = false; // Cờ để kiểm soát trạng thái trò chơi

	    while (!gameEnded) {
	        displayBoard();
	        System.out.println("Lượt chơi của " + (isWhiteTurn ? "Trắng (+): " : "Đen (-): "));
	        System.out.println("-".repeat(50));

	        while (true) {
	            if (isCheckmate()) {
	                System.out.println("-".repeat(50));
	                System.out.println("Chiếu bí! Bạn đã thua (game over): Kết quả " + (isWhiteTurn ? "Đen" : "Trắng") + " thắng!");
	                gameEnded = true; // Đánh dấu trò chơi kết thúc
	                break; // Thoát khỏi vòng lặp trong
	            } else if (isCheck()) {
	                System.out.println("Chiếu! Tướng của " + (isWhiteTurn ? "Trắng" : "Đen") + " bị đe dọa.");
	                System.out.println("Tướng đang bị chiếu! Vui lòng di chuyển quân hợp lệ để thoát khỏi tình trạng chiếu.");
	            }

	            Position from = getValidPositionInput(scanner, "Nhập vị trí (Row, Col) quân cờ cần di chuyển: ");
	            Piece piece = board.getPieceAt(from);
	            if (piece == null) {
	                System.out.println("Không có quân cờ tại vị trí đã chỉ định.");
	                continue;
	            }

	            if (piece.isWhite() != isWhiteTurn) {
	                System.out.println("Không đến lượt quân " + (isWhiteTurn ? "Đen" : "Trắng") + " đi.");
	                continue;
	            }

	            displayValidMoves(scanner, piece);
	            Position to = getValidPositionInput(scanner, "Nhập vị trí (Row, Col) quân cờ bạn muốn đến: ");

	            if (makeMove(from, to)) {
	                if (isCheckmate()) {
	                    System.out.println("-".repeat(50));
	                    System.out.println("Chiếu bí! Bạn đã thua (game over): Kết quả "
	                            + (isWhiteTurn ? "Đen" : "Trắng") + " thắng!");
	                    gameEnded = true; // Đánh dấu trò chơi kết thúc
	                    break; // Thoát khỏi vòng lặp trong
	                } else if (isCheck()) {
	                    System.out.println("-".repeat(50));
	                    undoMove(from, to, piece, null);
	                    continue;
	                }

	                // Lưu nước đi vào lịch sử
	                String color = piece.isWhite() ? "(1)" : "(2)";
	                String move = piece.getClass().getSimpleName() + color + " từ " + from + " đến " + to;
	                moveHistory.add(move);
	                System.out.println("-".repeat(50));
	                System.out.println("Lịch sử di chuyển: " + move);
	                printMoveHistory();

	                checkKingsStatus();
	                isWhiteTurn = !isWhiteTurn; // Đổi lượt chơi
	                break; // Thoát vòng lặp yêu cầu nhập quân cờ
	            } else {
	                System.out.println("Di chuyển không thành công!");
	            }
	        }
	    }
	    scanner.close(); // Đóng scanner khi kết thúc trò chơi
	}

	private void undoMove(Position from, Position to, Piece movedPiece, Piece capturedPiece) {
		movedPiece.setPosition(from);
		board.setPieceAt(from, movedPiece);

		if (capturedPiece != null) {
			board.setPieceAt(to, capturedPiece);
		} else {
			board.setPieceAt(to, null);
		}
	}

	private Position getValidPositionInput(Scanner scanner, String prompt) {
		while (true) {
			try {
				System.out.print(prompt);
				int row = scanner.nextInt();
				int col = scanner.nextInt();
				return new Position(row - 1, col - 1);
			} catch (Exception e) {
				System.out.println("Đã xảy ra lỗi. Vui lòng nhập lại.");
				scanner.nextLine();
			}
		}
	}

	private void displayValidMoves(Scanner scanner, Piece piece) {
		String color = piece.isWhite() ? "1" : "2";
		System.out.print("Nhập 'v' để xem các vị trí quân " + piece.getClass().getSimpleName() + "(" + color
				+ ") có thể đi, hoặc nhấn Enter để tiếp tục: ");

		scanner.nextLine();
		while (true) {
			String input = scanner.nextLine().strip().toLowerCase();
			if (input.equals("v")) {
				piece.printValidMoves(board);
				break;
			} else if (input.isEmpty()) {
				System.out.println("Đang thoát.......");
				break;
			} else {
				System.out.print("Nhập không hợp lệ. Vui lòng nhập 'v' hoặc nhấn Enter để thoát: ");
			}
		}
	}

	public void displayBoard() {
		System.out.println();

		// Mã màu ANSI
		final String RESET = "\u001B[0m"; // Màu mặc định
		final String ORANGE = "\u001B[38;5;214m"; // Màu cam cho đường viền
		final String RED = "\u001B[31m"; // Màu đỏ cho quân cờ có chỉ số (1)
		final String BLUE = "\u001B[34m"; // Màu xanh cho quân cờ có chỉ số (2)

		System.out.println(
				"           1           2            3            4            5            6            7            8            9");

		String borderLine = ORANGE
				+ "     +-----------+------------+------------+------------+------------+------------+------------+------------+------------+"
				+ RESET;
		System.out.println(borderLine);

		for (int i = 0; i <= 9; i++) {

			if (i >= 0 && i < 9) {
				System.out.print("  " + (i + 1) + "  " + ORANGE + "| " + RESET);
			} else {
				System.out.print(" " + (i + 1) + "  " + ORANGE + "| " + RESET);
			}

			for (int j = 0; j < 9; j++) {

				Piece piece = board.getPieceAt(new Position(i, j)); 

				if (piece != null) {
					int pieceIndex = piece.getIndex();

					String pieceColor;
					if (pieceIndex == 1) {
						pieceColor = RED; 
					} else if (pieceIndex == 2) {
						pieceColor = BLUE; 
					} else {
						pieceColor = RESET; 
					}
					System.out.print(pieceColor + " " + piece.getSymbol() + " " + ORANGE + "|  " + RESET);
				} else {
					if ((i == 1 || i == 2 || i == 7 || i == 8) && (j >= 3 && j <= 5)) {
						System.out.print("    .     " + ORANGE + "|  " + RESET);
					} else {
						if (i <= 4) {
							System.out.print("    +     " + ORANGE + "|  " + RESET); 
						} else {
							System.out.print("    -     " + ORANGE + "|  " + RESET); 
						}
					}
				}
			}

			System.out.println();

			System.out.println(borderLine);
		}
		System.out.println();
	}

	public boolean makeMove(Position from, Position to) {
		Piece piece = board.getPieceAt(from);
		if (piece == null || piece.isWhite() != isWhiteTurn || !piece.isValidMove(to, board)) {
			System.out.println("Không thể thực hiện di chuyển.");
			return false;
		}

		board.movePiece(from, to);
		return true;
	}

	private void printMoveHistory() {
		System.out.println("=== Danh sách lịch sử di chuyển ===");
		for (String move : moveHistory) {
			System.out.println(move);
		}
		System.out.println();
	}

	// Kiểm tra xem tướng có bị ăn hay không.
	public void checkKingsStatus() {
		// Kiểm tra Tướng Trắng
		if (isKingCaptured(true)) {
			System.out.println("Tướng Trắng đã bị ăn! Bạn đã thua (game over): Kết quả Đen thắng!");
			System.exit(0); // Kết thúc trò chơi
		}

		// Kiểm tra Tướng Đen
		if (isKingCaptured(false)) {
			System.out.println("Tướng Đen đã bị ăn! Bạn đã thua (game over): Kết quả Trắng thắng!");
			System.exit(0); // Kết thúc trò chơi
		}
	}

	private boolean isKingCaptured(boolean isWhite) {
		Position kingPosition = getKingPosition(isWhite);
		return kingPosition == null || board.getPieceAt(kingPosition).isWhite() != isWhite; // Tướng đã bị ăn
	}

	private Position getKingPosition(boolean isWhite) {
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 9; col++) {
				Piece piece = board.getPieceAt(new Position(row, col));
				if (piece instanceof King && piece.isWhite() == isWhite) {
					return piece.getPosition();
				}
			}
		}
		return null; // Không tìm thấy Tướng
	}

	public boolean isCheck() {
		Position kingPosition = getKingPosition(isWhiteTurn);
		if (kingPosition == null) {
			return false;
		}

		// Kiểm tra xem có quân cờ nào của đối phương có thể đi đến vị trí của Tướng
		// không
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 9; col++) {
				Piece piece = board.getPieceAt(new Position(row, col));
				if (piece != null && piece.isWhite() != isWhiteTurn) {
					if (piece.isValidMove(kingPosition, board)) {
						return true; // Có quân cờ của đối phương chiếu Tướng
					}
				}
			}
		}
		return false; // Không có quân cờ nào của đối phương có thể chiếu Tướng
	}

	public boolean isCheckmate() {
		if (!isCheck()) {
			return false; // Không bị chiếu, nên không thể bị chiếu bí
		}

		// Duyệt qua tất cả các quân cờ của bên hiện tại
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 9; col++) {
				Piece piece = board.getPieceAt(new Position(row, col));
				if (piece != null && piece.isWhite() == isWhiteTurn) {
					// Duyệt qua tất cả các nước đi hợp lệ của quân cờ
					List<Position> validMoves = piece.getValidMoves(board);
					for (Position move : validMoves) {
						// Thử di chuyển quân cờ đến vị trí mới
						Position originalPosition = piece.getPosition();
						board.movePiece(originalPosition, move);

						// Kiểm tra nếu nước đi này thoát khỏi tình trạng chiếu
						boolean isCheckAfterMove = isCheck();

						// Hoàn tác di chuyển
						board.movePiece(move, originalPosition);

						// Nếu sau khi di chuyển, Tướng không còn bị chiếu, không bị chiếu bí
						if (!isCheckAfterMove) {
							return false;
						}
					}
				}
			}
		}

		// Không có nước đi nào có thể thoát khỏi chiếu, chiếu bí
		return true;
	}
}