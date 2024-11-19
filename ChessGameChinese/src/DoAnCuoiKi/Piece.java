package DoAnCuoiKi;

import java.util.List;

public abstract class Piece {
	protected boolean isWhite;
	protected Position position;

	public Piece(boolean isWhite, Position position) {
		this.isWhite = isWhite;
		this.position = position;
	}

	public boolean isWhite() {
		return isWhite;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
    public void undoMove(Position previousPosition) {
        setPosition(previousPosition); 
    }
    
	public abstract boolean isValidMove(Position newPosition, ChessBoard board);

	public abstract String getSymbol();

	public abstract String getName();

	protected boolean outOfBounds(Position pos) {
		int row = pos.getRow();
		int col = pos.getCol();
		return row < 0 || row >= 10 || col < 0 || col >= 9;
	}

	protected abstract void printValidMoves(ChessBoard board);

	protected abstract List<Position> getValidMoves(ChessBoard board);

	protected abstract int getIndex();
}
