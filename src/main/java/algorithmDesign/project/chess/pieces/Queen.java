package algorithmDesign.project.chess.pieces;

import algorithmDesign.project.chess.Board;

import java.util.ArrayList;
import java.util.List;

public class Queen implements Piece{

    private boolean isWhite;

    private Piece rook;
    private Piece bishop;

    public Queen(boolean isWhite) {
        this.isWhite = isWhite;
        this.rook = new Rook(isWhite);
        this.bishop = new Bishop(isWhite);
    }
    @Override
    public String getNotation() {
        return isWhite ? "Q" : "q";
    }

    @Override
    public List<String> getValidMoves(Board board, int rank, int file) {
        List<String> validMoves = new ArrayList<>();
        validMoves.addAll(rook.getValidMoves(board, rank, file));
        validMoves.addAll(bishop.getValidMoves(board, rank, file));
        return validMoves;
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
        return 9;
    }
}
