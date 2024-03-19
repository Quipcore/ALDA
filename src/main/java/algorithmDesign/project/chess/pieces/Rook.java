package algorithmDesign.project.chess.pieces;

import algorithmDesign.project.chess.Board;

import java.util.List;

public class Rook implements Piece{

    private boolean isWhite;

    public Rook(boolean isWhite) {
        this.isWhite = isWhite;
    }
    @Override
    public String getNotation() {
        return isWhite() ? "R" : "r";
    }

    @Override
    public List<String> getValidMoves(Board board, int fromX, int fromY) {
        return null;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public boolean isBlack() {
        return !isWhite;
    }

    @Override
    public int getPoints() {
        return 5;
    }
}
