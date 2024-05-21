package algorithmDesign.project.chess2.pieces;

import algorithmDesign.project.chess2.Move;
import algorithmDesign.project.chess2.Piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Queen implements Piece {

    private final Color color;
    private final Piece rook;
    private final Piece bishop;
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

    public Queen(Color color) {
        this.color = color;
        this.symbol = color == Color.WHITE ? 'Q' : 'q';
        this.pieceValue = color == Color.WHITE ? 9 : -9;

        this.rook = new Rook(color);
        this.bishop = new Bishop(color);
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
        moves.addAll(rook.getValidMoves(board, visibleSquares, startSquare));
        moves.addAll(bishop.getValidMoves(board, visibleSquares, startSquare));
        return moves;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public List<Integer> getVisibleSquares(Piece[] board, int startSquare) {
        List<Integer> visibleSquares = new ArrayList<>();
        visibleSquares.addAll(rook.getVisibleSquares(board, startSquare));
        visibleSquares.addAll(bishop.getVisibleSquares(board, startSquare));
        return visibleSquares;
    }
}
