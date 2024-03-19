package algorithmDesign.project.chess.pieces;

import algorithmDesign.project.chess.Board;

import java.util.List;

public interface Piece {
    String getNotation();
    List<String> getValidMoves(Board board, int fromX, int fromY);
    boolean isWhite();
    boolean isBlack();

    int getPoints();
}
