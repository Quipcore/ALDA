package algorithmDesign.project.chess.pieces;

import algorithmDesign.project.chess.Board;

import java.util.ArrayList;
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
    public List<String> getValidMoves(Board board, int rank, int file) {
        List<String> validMoves = new ArrayList<>();

        for(int offset = 1; rank + offset < 8 || board.getPieceAt(rank +offset, file) != null
                || board.getPieceAt(rank +offset, file).isWhite() != isWhite; offset++){
            if(isValidMove(board,rank,file,offset,0)){
                validMoves.add(Board.convertPositionToNotation(rank + offset, file));
            }
        }

        for(int offset = 1; rank - offset >= 0; offset++){

        }

        for(int offset = 1; file + offset < 8; offset++){

        }

        for(int offset = 1; file - offset >= 0; offset++){

        }

        return validMoves;
    }

    private boolean isValidMove(Board board, int rank, int file, int rankOffset, int fileOffset) {
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
