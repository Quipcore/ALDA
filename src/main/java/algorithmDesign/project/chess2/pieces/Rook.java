package algorithmDesign.project.chess2.pieces;

import algorithmDesign.project.chess2.Board;
import algorithmDesign.project.chess2.Piece;
import algorithmDesign.project.chess2.Move;

import java.awt.*;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.List;

public class Rook implements Piece {

    private final Color color;
    private final char symbol;
    private final int pieceValue;
    private final int[] bonusEval = {
            1, 30, 30, 30, 30, 30, 30, 1,
            30, 40, 50, 50, 50, 50, 40, 30,
            30, 50, 70, 70, 70, 70, 50, 30,
            30, 50, 70, 80, 80, 70, 50, 30,
            30, 50, 70, 80, 80, 70, 50, 30,
            30, 50, 70, 70, 70, 70, 50, 30,
            30, 40, 50, 50, 50, 50, 40, 30,
            1, 30, 30, 30, 30, 30, 30, 1,
    };

    //------------------------------------------------------------------------------------------------------------------

    public Rook(Color color) {
        this.color = color;
        this.symbol = color == Color.WHITE ? 'R' : 'r';
        this.pieceValue = color == Color.WHITE ? 5 : -5;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public int getPieceValue(int square) {
        return this.pieceValue;// * bonusEval[square];
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

        Color enemyColor = color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;

        int startDirIndex = 0;
        int endDirIndex = 4;

        for (int directionIndex = startDirIndex; directionIndex < endDirIndex; directionIndex++) {
            for (int n = 0; n < Board.NUM_SQUARES_TO_EDGE[startSquare][directionIndex]; n++) {

                int targetSquare = startSquare + Board.DIRECTION_OFFSETS[directionIndex] * (n + 1);
                if (targetSquare < 0 || targetSquare >= 64) {
                    break;
                }

                Piece piece = board[targetSquare];

                if(piece != null && piece.getColor().equals(color)){
                    break;
                }

                moves.add(new Move(startSquare, targetSquare,this.symbol));

                if(piece != null && piece.getColor().equals(enemyColor)){
                    break;
                }
            }
        }

        return moves;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public List<Integer> getVisibleSquares(Piece[] board, int startSquare) {
        List<Integer> visibleSquares = new ArrayList<>();

        int startDirIndex = 0;
        int endDirIndex = 4;

        for (int directionIndex = startDirIndex; directionIndex < endDirIndex; directionIndex++) {
            for (int n = 0; n < Board.NUM_SQUARES_TO_EDGE[startSquare][directionIndex]; n++) {

                int targetSquare = startSquare + Board.DIRECTION_OFFSETS[directionIndex] * (n + 1);
                if (targetSquare < 0 || targetSquare >= 64) {
                    break;
                }

                Piece piece = board[targetSquare];

                visibleSquares.add(targetSquare);

                if(piece != null){
                    break;
                }
            }
        }

        return visibleSquares;
    }
}
