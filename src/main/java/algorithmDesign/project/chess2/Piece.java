package algorithmDesign.project.chess2;

import java.awt.Color;
import java.util.List;

public interface Piece {
    Color getColor();
    char getSymbol();
    List<Move> getValidMoves(Piece[] board, List<Integer> visibleSquares, int startSquare);
    List<Integer> getVisibleSquares(Piece[] board, int startSquare);
    int getPieceValue(int square);
}
