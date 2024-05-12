package algorithmDesign.project.chess2.pieces;

import algorithmDesign.project.chess2.Piece;
import algorithmDesign.project.chess2.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Knight implements Piece {

    private final Color color;
    private final char symbol;

    public Knight(Color color) {
        this.color = color;
        this.symbol = color.equals(Color.WHITE) ? 'N' : 'n';
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public char getSymbol() {
        return this.symbol;
    }


    @Override
    public List<Move> getMoves(Piece[] board,List<Integer> visibleSquares, int startSquare) {
        List<Move> moves = new ArrayList<>();

        int[] possibleSquares = {
                startSquare + 17, //Increases in rank by 2 and file by 1
                startSquare + 15, //Increases in rank by 2 and file by -1
                startSquare + 10, //Increases in rank by 1 and file by 2
                startSquare + 6, //Increases in rank by -1 and file by 2
                startSquare - 17, //Decreases in rank by 2 and file by 1
                startSquare - 15, //Decreases in rank by 2 and file by -1
                startSquare - 10, //Decreases in rank by 1 and file by 2
                startSquare - 6 //Decreases in rank by -1 and file by 2
        };

        for (int targetSquare : possibleSquares) {
            if (isValidSquare(board, targetSquare)) {

                //Make sure the knight is not moving an extra rank because of the board wrapping
                if (Math.abs(targetSquare % 8 - startSquare % 8) > 2) {
                    continue;
                }


                moves.add(new Move(startSquare, targetSquare, this.symbol));
            }
        }

        return moves;
    }

    @Override
    public List<Integer> getVisibleSquares(Piece[] board, int startSquare) {
        List<Integer> visibleSquares = new ArrayList<>();
        int[] possibleSquares = {
                startSquare + 17, //Increases in rank by 2 and file by 1
                startSquare + 15, //Increases in rank by 2 and file by -1
                startSquare + 10, //Increases in rank by 1 and file by 2
                startSquare + 6, //Increases in rank by -1 and file by 2
                startSquare - 17, //Decreases in rank by 2 and file by 1
                startSquare - 15, //Decreases in rank by 2 and file by -1
                startSquare - 10, //Decreases in rank by 1 and file by 2
                startSquare - 6 //Decreases in rank by -1 and file by 2
        };

        for (int targetSquare : possibleSquares) {
            if (isValidSquare(board, targetSquare)) {

                //Make sure the knight is not moving an extra rank because of the board wrapping
                if (Math.abs(targetSquare % 8 - startSquare % 8) > 2) {
                    continue;
                }
                visibleSquares.add(targetSquare);
            }
        }
        return visibleSquares;
    }

    private boolean isValidSquare(Piece[] board, int square) {
        if(square < 0 || square >= 64) {
            return false;
        }

        if (board[square] == null) {
            return true;
        }

        return !board[square].getColor().equals(this.color);
    }
}
