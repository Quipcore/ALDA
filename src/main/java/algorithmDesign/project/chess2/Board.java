package algorithmDesign.project.chess2;

import algorithmDesign.project.chess2.pieces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    public static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final int[][] NUM_SQUARES_TO_EDGE = preComputeMoveData();
    public static final int[] DIRECTION_OFFSETS = {
            8, -8, 1, -1, 7, -7, 9, -9
    };

    public final Map<Character, Piece> pieceMap;

    private final String currentFen;
    private final String enPassant;

    private final Piece[] pieceBoard;

    private final Color colorToMoveColor;


    public Board() {
        this(START_FEN);
    }


    public Board(String fen) {
        String[] splits = fen.split(" ");
        this.enPassant = splits[3];

        pieceMap = new HashMap<>() {
            {
                put('P', new Pawn(Color.WHITE, enPassant));
                put('N', new Knight(Color.WHITE));
                put('B', new Bishop(Color.WHITE));
                put('R', new Rook(Color.WHITE));
                put('Q', new Queen(Color.WHITE));
                put('K', new King(Color.WHITE,fen));

                put('p', new Pawn(Color.BLACK, enPassant));
                put('n', new Knight(Color.BLACK));
                put('b', new Bishop(Color.BLACK));
                put('r', new Rook(Color.BLACK));
                put('q', new Queen(Color.BLACK));
                put('k', new King(Color.BLACK,fen));
            }
        };

        this.currentFen = fen;
        pieceBoard = new Piece[64];

        setupBoard(this.pieceBoard, splits[0]);

        this.colorToMoveColor = fen.contains("w") ? Color.WHITE : Color.BLACK;
    }


    /**
     * Computes the number of squares to the edge of the board in each direction. Where first index is the square and the second index is the direction
     *
     * @return A 2D array with the number of squares to the edge of the board in each direction
     */
    private static int[][] preComputeMoveData() {

        int[][] numSquaresToEdge = new int[64][8];

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                int square = rank * 8 + file;

                int numNorth = 7 - rank;
                int numSouth = rank;
                int numWest = file;
                int numEast = 7 - file;

                numSquaresToEdge[square][0] = numNorth; //N
                numSquaresToEdge[square][1] = numSouth; //S
                numSquaresToEdge[square][2] = numWest; //W
                numSquaresToEdge[square][3] = numEast; //E
                numSquaresToEdge[square][4] = Math.min(numNorth, numWest); //NW
                numSquaresToEdge[square][5] = Math.min(numSouth, numEast); //SE
                numSquaresToEdge[square][6] = Math.min(numNorth, numEast); //NE
                numSquaresToEdge[square][7] = Math.min(numSouth, numWest); //SW
            }
        }

        return numSquaresToEdge;
    }

    private void setupBoard(Piece[] pieceBoard, String split) {
        String[] ranks = split.split("/");
        int currentRank = 0;
        for (String rank : ranks) {
            int currentFile = 0;
            for (char c : rank.toCharArray()) {
                if (Character.isDigit(c)) {
                    currentFile += Character.getNumericValue(c);
                } else {
                    pieceBoard[currentRank * 8 + currentFile] = pieceMap.get(c);
                    currentFile++;
                }
            }
            currentRank++;
        }
    }

    public List<Move> generateMoves() {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < pieceBoard.length; i++) {
            Piece piece = pieceBoard[i];
            if (piece != null && piece.getColor().equals(colorToMoveColor)) {
                moves.addAll(piece.getMoves(pieceBoard, i));
            }
        }
        return moves.stream().distinct().toList();
    }


    //
    /*
     +---+---+---+---+---+---+---+---+
  8  | r | n | b | q | k | b | n | r |
     +---+---+---+---+---+---+---+---+
  7  | p | p | p | p | p | p | p | p |
     +---+---+---+---+---+---+---+---+
  6  |   |   |   |   |   |   |   |   |
     +---+---+---+---+---+---+---+---+
  5  |   |   |   |   |   |   |   |   |
     +---+---+---+---+---+---+---+---+
  4  |   |   |   |   |   |   |   |   |
     +---+---+---+---+---+---+---+---+
  3  |   |   |   |   |   |   |   |   |
     +---+---+---+---+---+---+---+---+
  2  | P | P | P | P | P | P | P | P |
     +---+---+---+---+---+---+---+---+
  1  | R | N | B | Q | K | B | N | R |
     +---+---+---+---+---+---+---+---+
       A   B   C   D   E   F   G   H
     */

    public void printBoard() {
        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0) {
                System.out.println("   +---+---+---+---+---+---+---+---+");
            }
            if (i % 8 == 0) {
                System.out.print((8 - i / 8) + "  | ");
            }

            char piece = pieceBoard[i] == null ? ' ' : pieceBoard[i].getSymbol();

            System.out.print(piece + " | ");
            if (i % 8 == 7) {
                System.out.println();
            }
        }
        System.out.println("   +---+---+---+---+---+---+---+---+");
        System.out.println("     A   B   C   D   E   F   G   H  ");
        System.out.println("FEN: " + currentFen);
    }

    @Override
    public String toString() {
        return "Board{" +
                "currentFen='" + currentFen + '\'' +
                '}';
    }
}
