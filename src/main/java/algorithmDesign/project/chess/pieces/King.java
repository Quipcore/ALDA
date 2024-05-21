package algorithmDesign.project.chess.pieces;

import algorithmDesign.project.chess.Board;

import java.util.ArrayList;
import java.util.List;

public class King implements Piece{

    private boolean isWhite;

    public King(boolean isWhite) {
        this.isWhite = isWhite;
    }

    @Override
    public String getNotation() {
        return isWhite() ? "K" : "k";
    }

    @Override
    public List<String> getValidMoves(Board board, int rank, int file) {

        if(rank < 0 || rank > 7 || file < 0 || file > 7){
            throw new IllegalArgumentException("Invalid position");
        }

        List<String> validMoves = new ArrayList<>();
        for (int rankOffset = -1; rankOffset <= 1; rankOffset++){
            for(int fileOffset = -1; fileOffset <= 1; fileOffset++){
                if(isValidMove(board,rank, file, rankOffset, fileOffset)){
                    validMoves.add(Board.convertPositionToNotation(rank + rankOffset, file + fileOffset));
                }
            }
        }


        return validMoves;
    }

    //Ignores line of sight check for now
    private boolean isValidMove(Board board, int rank, int file, int rankOffset, int fileOffset) {

        if(rankOffset == 0 && fileOffset == 0){
            return false;
        }

        if(rank + rankOffset < 0 || rank + rankOffset > 7 || file + fileOffset < 0 || file + fileOffset > 7){
            return false;
        }

        Piece piece = board.getPieceAt(rank + rankOffset, file + fileOffset);

        return piece == null || piece.isWhite() != isWhite;
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
        return Integer.MAX_VALUE;
    }
}
