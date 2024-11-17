package DoAnCuoiKi;

public class ChessBoard {
    private Piece[][] board;

    public ChessBoard() {
        board = new Piece[10][9]; // Kích thước bàn cờ 10x9
        initializeBoard();
    }

    private void initializeBoard() {
        System.out.println("                                       ==============< Khởi tạo bàn cờ >==============");
        // Khởi tạo Tướng
        board[0][4] = new King(true, new Position(0, 4)); // Tướng trắng
        board[9][4] = new King(false, new Position(9, 4)); // Tướng đen

        // Khởi tạo Sĩ
        board[0][3] = new Advisor(true, new Position(0, 3)); // Sĩ trắng
        board[0][5] = new Advisor(true, new Position(0, 5));
        board[9][3] = new Advisor(false, new Position(9, 3)); // Sĩ đen
        board[9][5] = new Advisor(false, new Position(9, 5));

        // Khởi tạo Tượng
        board[0][2] = new Elephant(true, new Position(0, 2)); // Tượng trắng
        board[0][6] = new Elephant(true, new Position(0, 6));
        board[9][2] = new Elephant(false, new Position(9, 2)); // Tượng đen
        board[9][6] = new Elephant(false, new Position(9, 6));

        // Khởi tạo Mã
        board[0][1] = new Horse(true, new Position(0, 1)); // Mã trắng
        board[0][7] = new Horse(true, new Position(0, 7));
        board[9][1] = new Horse(false, new Position(9, 1)); // Mã đen
        board[9][7] = new Horse(false, new Position(9, 7));

        // Khởi tạo Xe
        board[0][0] = new Chariot(true, new Position(0, 0)); // Xe trắng
        board[0][8] = new Chariot(true, new Position(0, 8));
        board[9][0] = new Chariot(false, new Position(9, 0)); // Xe đen
        board[9][8] = new Chariot(false, new Position(9, 8));

        // Khởi tạo Pháo
        board[2][1] = new Cannon(true, new Position(2, 1)); // Pháo trắng
        board[2][7] = new Cannon(true, new Position(2, 7));
        board[7][1] = new Cannon(false, new Position(7, 1)); // Pháo đen
        board[7][7] = new Cannon(false, new Position(7, 7));

        // Khởi tạo Tốt
        board[3][0] = new Soldier(true, new Position(3, 0)); // Tốt trắng
        board[3][2] = new Soldier(true, new Position(3, 2));
        board[3][4] = new Soldier(true, new Position(3, 4));
        board[3][6] = new Soldier(true, new Position(3, 6));
        board[3][8] = new Soldier(true, new Position(3, 8));
        board[6][0] = new Soldier(false, new Position(6, 0)); // Tốt đen
        board[6][2] = new Soldier(false, new Position(6, 2));
        board[6][4] = new Soldier(false, new Position(6, 4));
        board[6][6] = new Soldier(false, new Position(6, 6));
        board[6][8] = new Soldier(false, new Position(6, 8));
    }

    public Piece getPieceAt(Position position) {
        return board[position.getRow()][position.getCol()];
    }

    public void setPieceAt(Position position, Piece piece) {
        board[position.getRow()][position.getCol()] = piece;
    }

    public void movePiece(Position from, Position to) {
        Piece piece = getPieceAt(from);
        if (piece != null) {
            piece.setPosition(to);
            board[to.getRow()][to.getCol()] = piece;
            board[from.getRow()][from.getCol()] = null;
        }
    }

    public void printBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 9; col++) {
                Piece piece = board[row][col];
                if (piece != null) {
                    System.out.print("[" + piece.getSymbol() + "] "); // In ra ký hiệu quân cờ
                } else {
                    System.out.print("[ ] "); // Ô trống
                }
            }
            System.out.println();
        }
    }

    public void undoMove(Position from, Position to, Piece movedPiece, Piece capturedPiece) {
        if (movedPiece != null) {
            setPieceAt(from, movedPiece); 
            movedPiece.setPosition(from); 
        }

        if (capturedPiece != null) {
            setPieceAt(to, capturedPiece); 
        } else {
            board[to.getRow()][to.getCol()] = null; 
        }
    }

    public boolean capturePieceAt(Position position, Piece attacker) {
        Piece targetPiece = getPieceAt(position);
        if (targetPiece != null && targetPiece.isWhite() != attacker.isWhite()) {
//             Xóa quân cờ bị ăn
        	removePiece(position);
            return true;
        }
        return false;
    }

    public void removePiece(Position position) {
        // Xóa quân cờ tại vị trí đã cho
        if (position.getRow() >= 0 && position.getRow() < 10 && 
            position.getCol() >= 0 && position.getCol() < 9) {
            board[position.getRow()][position.getCol()] = null; // Đặt ô trống
        }
    }
}
