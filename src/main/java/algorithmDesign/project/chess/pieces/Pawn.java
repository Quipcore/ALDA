package algorithmDesign.project.chess.pieces;

import algorithmDesign.project.chess.Board;

import java.util.ArrayList;
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
    public List<String> getValidMoves(Board board, int rank, int file) {

        if(rank == 1){

        }

        return new ArrayList<>();
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
