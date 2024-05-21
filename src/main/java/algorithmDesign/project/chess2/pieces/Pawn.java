package algorithmDesign.project.chess2.pieces;

import algorithmDesign.project.chess2.Move;
import algorithmDesign.project.chess2.Piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn implements Piece {

    private final Color color;
    private final char symbol;
    private final String enPassant;
    private final int pieceValue;
    private final int[] bonusEval = {
            100, 100, 100, 100, 100, 100, 100, 100,
            70, 70, 70, 70, 70, 70, 70, 70,
            10, 60, 60, 60, 60, 60, 60, 10,
            10, 20, 40, 50, 50, 40, 20, 10,
            10, 20, 40, 50, 50, 40, 20, 10,
            10, 20, 40, 40, 40, 40, 20, 10,
            10, 20, 20, 20, 20, 20, 20, 10,
            10, 10, 10, 10, 10, 10, 10, 10
    };
    //------------------------------------------------------------------------------------------------------------------

    public Pawn(Color color, String enPassant) {
        this.color = color;
        this.symbol = color == Color.WHITE ? 'P' : 'p';
        this.enPassant = enPassant;
        this.pieceValue = color == Color.WHITE ? 1 : -1;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public int getPieceValue(int square) {
        return this.pieceValue * bonusEval[square];
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public Color getColor() {
        return color;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public char getSymbol() {
        return symbol;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public List<Move> getValidMoves(Piece[] board, List<Integer> visibleSquares, int startSquare) {
        List<Move> moves = new ArrayList<>();

        Color enemyColor = color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        int forwardDirection = color.equals(Color.WHITE) ? -8 : 8;


        int targetSquare = startSquare + forwardDirection;

        if (targetSquare < 0 || targetSquare >= 64) {
            return moves;
        }

        //Pawn push
        if (board[targetSquare] == null) {
            if(color.equals(Color.WHITE) && targetSquare < 8 || color.equals(Color.BLACK) && targetSquare >= 56){
                Move move = new Move(startSquare, targetSquare, symbol);
                move.setPromotionPiece(color.equals(Color.WHITE) ? 'Q' : 'q');
                moves.add(move);

                move = new Move(startSquare, targetSquare, symbol);
                move.setPromotionPiece(color.equals(Color.WHITE) ? 'R' : 'r');
                moves.add(move);

                move = new Move(startSquare, targetSquare, symbol);
                move.setPromotionPiece(color.equals(Color.WHITE) ? 'B' : 'b');
                moves.add(move);

                move = new Move(startSquare, targetSquare, symbol);
                move.setPromotionPiece(color.equals(Color.WHITE) ? 'N' : 'n');
                moves.add(move);
            }
            moves.add(new Move(startSquare, targetSquare, symbol));
        }

        int forwardPosition = startSquare + forwardDirection;
        if (inRange(forwardPosition, 0, 64) && board[targetSquare] == null && board[forwardPosition] == null) {
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
                    moves.add(new Move(startSquare, enPassantCaptureSquare + forwardDirection, "enPassant"));
                }
            }
        }

        return moves;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public List<Integer> getVisibleSquares(Piece[] board, int startSquare) {

        List<Integer> visibleSquares = new ArrayList<>();
        Color enemyColor = color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        int forwardDirection = color.equals(Color.WHITE) ? -8 : 8;

        int[] sideWaysCaptureOffsets = {
                startSquare + forwardDirection - 1,
                startSquare + forwardDirection + 1
        };

        for (int sideWaysCaptureSquare : sideWaysCaptureOffsets) {
            if (inRange(sideWaysCaptureSquare, 0, 64)) {
                visibleSquares.add(sideWaysCaptureSquare);
            }
        }

        int[] enPassantCaptureOffsets = {
                startSquare - 1,
                startSquare + 1
        };

        for (int enPassantCaptureSquare : enPassantCaptureOffsets) {
            if (inRange(enPassantCaptureSquare + forwardDirection, 0, 64)) {
                if (enPassantCapture(enPassantCaptureSquare)) {
                    visibleSquares.add(enPassantCaptureSquare + forwardDirection);
                }
            }
        }

        return visibleSquares;

    }

    //------------------------------------------------------------------------------------------------------------------

    private boolean inRange(int startSquare, int lowerLimit, int upperLimit) {
        return lowerLimit <= startSquare && startSquare < upperLimit;
    }

    //------------------------------------------------------------------------------------------------------------------

    private boolean enPassantCapture(int targetSquare) {
        String targetNotation = (char) ('a' + targetSquare % 8) + "" + (9 - targetSquare / 8);
        return targetNotation.equals(this.enPassant);
    }
}
