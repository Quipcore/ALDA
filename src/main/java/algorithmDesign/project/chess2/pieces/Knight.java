package algorithmDesign.project.chess2.pieces;

import algorithmDesign.project.chess2.Piece;
import algorithmDesign.project.chess2.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Knight implements Piece {

    private final Color color;
    private final char symbol;

    //------------------------------------------------------------------------------------------------------------------

    public Knight(Color color) {
        this.color = color;
        this.symbol = color.equals(Color.WHITE) ? 'N' : 'n';
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Color getColor() {
        return this.color;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public char getSymbol() {
        return this.symbol;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public List<Move> getValidMoves(Piece[] board, List<Integer> visibleSquares, int startSquare) {
        List<Move> moves = new ArrayList<>();
        for (int directionOffset : new int[]{-17, -15, -10, -6, 6, 10, 15, 17}) {
            int targetSquare = startSquare + directionOffset;
            if (isValidSquare(board, startSquare,targetSquare)) {
                moves.add(new Move(startSquare, targetSquare, symbol));
            }
        }
        return moves;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public List<Integer> getVisibleSquares(Piece[] board, int startSquare) {
        List<Integer> visibleSquares = new ArrayList<>();
        for (int directionOffset : new int[]{-17, -15, -10, -6, 6, 10, 15, 17}) {
            int targetSquare = startSquare + directionOffset;
            if (isValidSquare(board,startSquare, targetSquare)) {
                visibleSquares.add(targetSquare);
            }
        }
        return visibleSquares;
    }

    //------------------------------------------------------------------------------------------------------------------

    private boolean isValidSquare(Piece[] board,int startSquare, int square) {
            int currentRank = square / 8;
            int currentFile = square % 8;

            int startRank = startSquare / 8;
            int startFile = startSquare % 8;

            int rankDifference = Math.abs(currentRank - startRank);
            int fileDifference = Math.abs(currentFile - startFile);

            boolean isKnightMove = (rankDifference == 2 && fileDifference == 1) || (rankDifference == 1 && fileDifference == 2);
            return square >= 0 && square < 64 && isKnightMove && (board[square] == null || board[square].getColor() != this.color);
        }
}
