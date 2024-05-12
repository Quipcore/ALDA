package algorithmDesign.project.chess2.pieces;

import algorithmDesign.project.chess2.Piece;
import algorithmDesign.project.chess2.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static algorithmDesign.project.chess2.Board.DIRECTION_OFFSETS;

public class King implements Piece {

    private final Color color;
    private final char symbol;
    private final boolean whiteKingSideCastlingRights;
    private final boolean whiteQueenSideCastlingRights;
    private final boolean blackKingSideCastlingRights;
    private final boolean blackQueenSideCastlingRights;

    public King(Color color, String fen) {
        this.color = color;
        this.symbol = color.equals(Color.WHITE) ? 'K' : 'k';

        String castlingRights = fen.split(" ")[2];
        this.whiteKingSideCastlingRights = castlingRights.contains("K");
        this.whiteQueenSideCastlingRights = castlingRights.contains("Q");
        this.blackKingSideCastlingRights = castlingRights.contains("k");
        this.blackQueenSideCastlingRights = castlingRights.contains("q");
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
        Color friendlyColor = this.color;

        for (int directionOffset : DIRECTION_OFFSETS) {
            int targetSquare = startSquare + directionOffset;
            if (isValidSquare(targetSquare, board)) {
                moves.add(new Move(startSquare, targetSquare, symbol));
            }
        }

        if (canCastleKingSide(board,friendlyColor)) {
            moves.add(new Move(startSquare, startSquare + 2, "O-O"));
        }

        if (canCastleQueenSide(board,friendlyColor)) {
            moves.add(new Move(startSquare, startSquare - 2, "O-O-O"));
        }


        return moves;
    }

    private boolean canCastleQueenSide(Piece[] board, Color friendlyColor) {

        boolean hasCastlingRights = friendlyColor.equals(Color.WHITE) ? whiteQueenSideCastlingRights : blackQueenSideCastlingRights;
        if (!hasCastlingRights) {
            return false;
        }

        //Check if the squares between the king and rook are empty
        int[] squaresToCheck = {
                friendlyColor.equals(Color.WHITE) ? 1 : 57, //b1, b8
                friendlyColor.equals(Color.WHITE)  ? 2 : 58, //c1, c8
                friendlyColor.equals(Color.WHITE)  ? 3 : 59  //d1, d8
        };

        for (int square : squaresToCheck) {
            if (board[square] != null) {
                return false;
            }
        }
        return true;
    }

    private boolean canCastleKingSide(Piece board[], Color friendlyColor) {
        boolean hasCastlingRights = friendlyColor.equals(Color.WHITE) ? whiteKingSideCastlingRights : blackKingSideCastlingRights;

        if (!hasCastlingRights) {
            return false;
        }

        //Check if the squares between the king and rook are empty
        int[] squaresToCheck = {
                friendlyColor.equals(Color.WHITE) ? 5 : 61,
                friendlyColor.equals(Color.WHITE) ? 6 : 62
        };

        for (int square : squaresToCheck) {
            if (board[square] != null) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidSquare(int targetSquare, Piece[] board) {
        if(targetSquare < 0 || targetSquare >= 64) {
            return false;
        }

        if (board[targetSquare] == null) {
            return true;
        }

        return !board[targetSquare].getColor().equals(this.color);
    }
}
