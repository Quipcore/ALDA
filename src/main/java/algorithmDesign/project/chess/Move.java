package algorithmDesign.project.chess;

import algorithmDesign.project.chess.pieces.Piece;

public class Move {

    private int fromX;
    private int fromY;
    private int toX;
    private int toY;

    public Move(int fromX, int fromY, int toX, int toY, Piece piece){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }



}
