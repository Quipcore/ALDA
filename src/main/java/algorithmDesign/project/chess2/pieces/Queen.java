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
        this.symbol = color == Color.WHITE ? 'P' : 'p';

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
    public List<Move> getMoves(Piece[] board,List<Integer> visibleSquares, int startSquare) {
        List<Move> moves = new ArrayList<>();
        moves.addAll(rook.getMoves(board,visibleSquares, startSquare));
        moves.addAll(bishop.getMoves(board,visibleSquares, startSquare));
        return moves;
    }
}
