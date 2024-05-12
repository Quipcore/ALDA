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
    public List<Move> getMoves(Piece[] board,List<Integer> visibleSquares, int startSquare) {
        List<Move> moves = new ArrayList<>();

        for (int directionOffset : DIRECTION_OFFSETS) {
            int targetSquare = startSquare + directionOffset;
            if (isValidSquare(targetSquare, board, visibleSquares)) {
                moves.add(new Move(startSquare, targetSquare, symbol));
            }
        }

        char rookSymbol = color.equals(Color.WHITE) ? 'R' : 'r';

        int[] rookSquare = {
                color.equals(Color.WHITE) ? 63 : 7,
                color.equals(Color.WHITE) ? 56 : 0
        };

        int[] squaresToCheck = {
                color.equals(Color.WHITE) ? 5 : 61,
                color.equals(Color.WHITE) ? 6 : 62
        };
        boolean hasKingSideCastlingRights = color.equals(Color.WHITE) ? whiteKingSideCastlingRights : blackKingSideCastlingRights;
        Piece kingSideRook = board[rookSquare[1]];
        if(kingSideRook != null && kingSideRook.getSymbol() == rookSymbol && canCastle(board,visibleSquares,squaresToCheck,hasKingSideCastlingRights)) {
            moves.add(new Move(startSquare, startSquare + 2, "O-O"));
        }

        squaresToCheck = new int[]{
                color.equals(Color.WHITE) ? 1 : 57, //b1, b8
                color.equals(Color.WHITE) ? 2 : 58, //c1, c8
                color.equals(Color.WHITE) ? 3 : 59  //d1, d8
        };
        boolean hasQueenSideCastlingRights = color.equals(Color.WHITE) ? whiteQueenSideCastlingRights : blackQueenSideCastlingRights;
        Piece queenSidePiece = board[rookSquare[0]];
        if(queenSidePiece != null && queenSidePiece.getSymbol() == rookSymbol && canCastle(board,visibleSquares,squaresToCheck,hasQueenSideCastlingRights)) {
            moves.add(new Move(startSquare, startSquare - 2, "O-O-O"));
        }


        return moves;
    }

    @Override
    public List<Integer> getVisibleSquares(Piece[] board, int startSquare) {
        List<Integer> visibleSquares = new ArrayList<>();
        for (int directionOffset : DIRECTION_OFFSETS) {
            int targetSquare = startSquare + directionOffset;
            if (isValidSquare(targetSquare, board, visibleSquares)) {
                visibleSquares.add(targetSquare);
            }
        }
        return visibleSquares;
    }

    private boolean canCastle(Piece[] board, List<Integer> visibleSquares,int[] squaresToCheck, boolean hasCastlingRights) {
        if (!hasCastlingRights) {
            return false;
        }

        for (int square : squaresToCheck) {
            if (board[square] != null || visibleSquares.contains(square) ) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidSquare(int targetSquare, Piece[] board, List<Integer> visibleSquares) {
        if(targetSquare < 0 || targetSquare >= 64) {
            return false;
        }

        if (board[targetSquare] == null && !visibleSquares.contains(targetSquare)) {
            return true;//;
        }

        return board[targetSquare] != null && !board[targetSquare].getColor().equals(this.color) && !visibleSquares.contains(targetSquare);
    }
}
