package algorithmDesign.project.chess.pieces;

import algorithmDesign.project.chess.Board;

import java.util.ArrayList;
import java.util.List;

public class Knight implements Piece{

    private boolean isWhite;

    public Knight(boolean isWhite) {
        this.isWhite = isWhite;
    }

    @Override
    public String getNotation() {
        return isWhite ? "N" : "n";
    }

    @Override
    public List<String> getValidMoves(Board board, int rank, int file) {
        return new ArrayList<>();
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
        return 3;
    }
}
