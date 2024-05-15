package algorithmDesign.project.chess2.pieces;

import algorithmDesign.project.chess2.Piece;
import algorithmDesign.project.chess2.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Queen implements Piece {

    private final Color color;
    private final Piece rook;
    private final Piece bishop;
    private final char symbol;

    public Queen(Color color) {
        this.color = color;
        this.symbol = color == Color.WHITE ? 'Q' : 'q';

        this.rook = new Rook(color);
        this.bishop = new Bishop(color);
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
    public List<Move> getValidMoves(Piece[] board, List<Integer> visibleSquares, int startSquare) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(rook.getValidMoves(board,visibleSquares, startSquare));
        moves.addAll(bishop.getValidMoves(board,visibleSquares, startSquare));
        return moves;
    }

    @Override
    public List<Integer> getVisibleSquares(Piece[] board, int startSquare) {
        List<Integer> visibleSquares = new ArrayList<>();
        visibleSquares.addAll(rook.getVisibleSquares(board, startSquare));
        visibleSquares.addAll(bishop.getVisibleSquares(board, startSquare));
        return visibleSquares;
    }
}
