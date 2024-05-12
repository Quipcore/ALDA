package algorithmDesign.project.chess2.pieces;

import algorithmDesign.project.chess2.Piece;
import algorithmDesign.project.chess2.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn implements Piece {

    private final Color color;
    private final char symbol;
    private final String enPassant;


    public Pawn(Color color, String enPassant) {
        this.color = color;
        this.symbol = color == Color.WHITE ? 'P' : 'p';
        this.enPassant = enPassant;
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
    public List<Move> getMoves(Piece[] board, int startSquare) {
        List<Move> moves = new ArrayList<>();

        Color enemyColor = color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        int forwardDirection = color.equals(Color.WHITE) ? -8 : 8;

        //Pawn push
        int targetSquare = startSquare + forwardDirection;
        if (board[targetSquare] == null) {
            moves.add(new Move(startSquare, targetSquare, symbol));
        }

        //Pawn double push
        if (board[targetSquare + forwardDirection] == null) {
            if (forwardDirection > 0 && inRange(startSquare, 8, 16)) {
                moves.add(new Move(startSquare, targetSquare + forwardDirection, symbol));
            }

            if (forwardDirection < 0 && inRange(startSquare, 48, 56)) {
                moves.add(new Move(startSquare, targetSquare + forwardDirection, symbol));
            }
        }

        int[] sideWaysCaptureOffsets = {
                startSquare + forwardDirection - 1,
                startSquare + forwardDirection + 1
        };
        for (int sideWaysCaptureSquare : sideWaysCaptureOffsets) {
            if (inRange(sideWaysCaptureSquare, 0, 64)) {
                if (board[sideWaysCaptureSquare] != null && board[sideWaysCaptureSquare].getColor().equals(enemyColor)) {
                    moves.add(new Move(startSquare, sideWaysCaptureSquare, symbol));
                }
            }
        }

        int[] enPassantCaptureOffsets = {
                startSquare - 1,
                startSquare + 1
        };

        for (int enPassantCaptureSquare : enPassantCaptureOffsets) {
            if (inRange(enPassantCaptureSquare + forwardDirection, 0, 64)) {
                if (enPassantCapture(enPassantCaptureSquare)) {
                    moves.add(new Move(startSquare, enPassantCaptureSquare + forwardDirection, symbol));
                }
            }
        }

        return moves;
    }

    private boolean inRange(int startSquare, int lowerLimit, int upperLimit) {
        return lowerLimit <= startSquare && startSquare < upperLimit;
    }

    private boolean enPassantCapture(int targetSquare) {
        String targetNotation = (char) ('a' + targetSquare % 8) + "" + (9 - targetSquare / 8);
        return targetNotation.equals(this.enPassant);
    }
}
