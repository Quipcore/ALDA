package algorithmDesign.project.chess2.pieces;

import algorithmDesign.project.chess2.Board;
import algorithmDesign.project.chess2.Piece;
import algorithmDesign.project.chess2.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bishop implements Piece {

    private Color color;
    private char symbol;

    public Bishop(Color color) {
        this.color = color;
        this.symbol = color == Color.WHITE ? 'B' : 'b';
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    @Override
    public List<Move> getMoves(Piece[] board,List<Integer> visibleSquares, int startSquare) {
        List<Move> moves = new ArrayList<>();
        Color enemyColor = color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;

        int startDirIndex = 4;
        int endDirIndex = 8;

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

                if(piece != null && !piece.getColor().equals(enemyColor)){
                    break;
                }
            }
        }

        return moves;
    }
}
