package algorithmDesign.project.chess.pieces;

import algorithmDesign.project.chess.Board;

import java.util.List;

public class Pawn implements Piece{

    boolean isWhite;

    public Pawn(boolean isWhite) {
        this.isWhite = isWhite;
    }

    @Override
    public String getNotation() {
        return isWhite ? "P" : "p";
    }

    @Override
    public List<String> getValidMoves(Board board, int fromX, int fromY) {
        return null;
    }


    public boolean isWhite() {
        return isWhite;
    }

    public boolean isBlack() {
        return !isWhite;
    }

    @Override
    public int getPoints() {
        return 1;
    }
}
