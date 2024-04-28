package algorithmDesign.project.chess2;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final int[][] numSquaresToEdge = preComputeMoveData();
    private static final int[] directionOffsets = {
            8, -8, 1, -1, 7, -7, 9, -9
    };
    private final String currentFen;
    private final String enPassant;

    private final int[] board;

    private final int colorToMove;

    private final boolean whiteKingSideCastlingRights;
    private final boolean whiteQueenSideCastlingRights;
    private final boolean blackKingSideCastlingRights;
    private final boolean blackQueenSideCastlingRights;


    public Board() {
        this(START_FEN);
    }


    public Board(String fen) {
        this.currentFen = fen;
        board = new int[64];

        String[] splits = fen.split(" ");

        setupBoard(this.board, splits[0]);
        this.colorToMove = splits[1].equals("w") ? Piece.WHITE : Piece.BLACK;

        this.enPassant = splits[3];

        String castlingRights = splits[2];
        this.whiteKingSideCastlingRights = castlingRights.contains("K");
        this.whiteQueenSideCastlingRights = castlingRights.contains("Q");
        this.blackKingSideCastlingRights = castlingRights.contains("k");
        this.blackQueenSideCastlingRights = castlingRights.contains("q");


    }

    /** Computes the number of squares to the edge of the board in each direction. Where first index is the square and the second index is the direction
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

    private void setupBoard(int[] board, String fenRanks) {
        String[] ranks = fenRanks.split("/");

        int currentRank = 0;
        for (String rank : ranks) {
            int currentFile = 0;
            for (char c : rank.toCharArray()) {
                if (Character.isDigit(c)) {
                    currentFile += Character.getNumericValue(c);
                } else {
                    int color = Character.isUpperCase(c) ? Piece.WHITE : Piece.BLACK;
                    int peice = Piece.SYMBOL_PEICE.get(Character.toUpperCase(c));
                    board[currentRank * 8 + currentFile] = peice | color;
                    currentFile++;
                }
            }
            currentRank++;
        }
    }

    public List<Move> generateMoves() {

        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            int piece = board[i];

            if (Piece.IsColor(piece, colorToMove)) {

                List<Move> pieceMoves = switch (piece & 0b00_111) {
                    case Piece.PAWN -> generatePawnMoves(i, piece);
                    case Piece.KNIGHT -> generateKnightMoves(i, piece);
                    case Piece.KING -> generateKingMoves(i, piece);
                    case Piece.ROOK, Piece.BISHOP, Piece.QUEEN -> generateSlidingMoves(i, piece);
                    default ->
                            throw new IllegalStateException("Unexpected value: " + (piece & 0b00_111) + ". Board probably in illegal state"); //Should never happen
                };
                moves.addAll(pieceMoves);
            }
        }
        return moves;
    }

    private List<Move> generateSlidingMoves(int startSquare, int piece) {

        List<Move> moves = new ArrayList<>();

        int friendlyColor = piece & 0b11_000;
        int enemyColor = friendlyColor == Piece.WHITE ? Piece.BLACK : Piece.WHITE;

        int startDirIndex = Piece.isType(piece, Piece.BISHOP) ? 4 : 0;
        int endDirIndex = Piece.isType(piece, Piece.ROOK) ? 4 : 8;

        for (int directionIndex = startDirIndex; directionIndex < endDirIndex; directionIndex++) {
            for (int n = 0; n < numSquaresToEdge[startSquare][directionIndex]; n++) {

                int targetSquare = startSquare + directionOffsets[directionIndex] * (n + 1);
                if (targetSquare < 0 || targetSquare >= 64) {
                    break;
                }
                int targetPiece = board[targetSquare];

                if (Piece.IsColor(targetPiece, friendlyColor)) {
                    break;
                }

                moves.add(new Move(startSquare, targetSquare, Piece.PEICE_SYMBOL.get(piece & 0b00_111)));

                if (Piece.IsColor(targetPiece, enemyColor)) {
                    break;
                }
            }
        }

        return moves;
    }

    private List<Move> generatePawnMoves(int startSquare, int piece) {
        List<Move> moves = new ArrayList<>();

        int friendlyColor = piece & 0b11_000;
        int enemyColor = friendlyColor == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        int forwardDirection = friendlyColor == Piece.WHITE ? -8 : 8;

        char pieceSymbol = Piece.PEICE_SYMBOL.get(piece & 0b00_111);

        //Pawn push
        int targetSquare = startSquare + forwardDirection;
        if (board[targetSquare] == Piece.EMPTY) {
            moves.add(new Move(startSquare, targetSquare, pieceSymbol));
        }

        //Pawn double push
        if (board[targetSquare + forwardDirection] == Piece.EMPTY) {
            if (forwardDirection > 0 && inRange(startSquare, 8, 16)) {
                moves.add(new Move(startSquare, targetSquare + forwardDirection, pieceSymbol));
            }

            if (forwardDirection < 0 && inRange(startSquare, 48, 56)) {
                moves.add(new Move(startSquare, targetSquare + forwardDirection, pieceSymbol));
            }
        }

        //Pawn capture
        int[] sideWaysCaptureOffsets = {
                startSquare + forwardDirection - 1,
                startSquare + forwardDirection + 1
        };
        for (int sideWaysCaptureOffset : sideWaysCaptureOffsets) {
            if (isValidSquare(sideWaysCaptureOffset, friendlyColor) && Piece.IsColor(board[sideWaysCaptureOffset], enemyColor)) {
                moves.add(new Move(startSquare, sideWaysCaptureOffset, pieceSymbol));
            }
        }

        //En passant
        int[] enPassantCaptureOffsets = {
                startSquare - 1,
                startSquare + 1
        };
        for (int enPassantDirection : enPassantCaptureOffsets) {
            int enPassantMoveSquare = enPassantDirection + forwardDirection;
            if (isValidSquare(enPassantMoveSquare, friendlyColor) && Piece.IsColor(board[enPassantDirection], enemyColor)) {
                if (enPassantCapture(enPassantMoveSquare)) {
                    moves.add(new Move(startSquare, enPassantMoveSquare, pieceSymbol));
                }
            }
        }

        return moves;
    }

    private boolean inRange(int startSquare, int lowerLimit, int upperLimit) {
        return lowerLimit <= startSquare && startSquare < upperLimit;
    }

    private boolean enPassantCapture(int targetSquare) {
        String targetNotation = (char) ('a' + targetSquare % 8) + "" + (8 - targetSquare / 8);
        return targetNotation.equals(enPassant);
    }

    private List<Move> generateKnightMoves(int startSquare, int piece) {
        List<Move> moves = new ArrayList<>();

        int friendlyColor = piece & 0b11_000;

        int[] possibleSquares = {
                startSquare + 17, //Increases in rank by 2 and file by 1
                startSquare + 15, //Increases in rank by 2 and file by -1
                startSquare + 10, //Increases in rank by 1 and file by 2
                startSquare + 6, //Increases in rank by -1 and file by 2
                startSquare - 17, //Decreases in rank by 2 and file by 1
                startSquare - 15, //Decreases in rank by 2 and file by -1
                startSquare - 10, //Decreases in rank by 1 and file by 2
                startSquare - 6 //Decreases in rank by -1 and file by 2
        };

        for (int targetSquare : possibleSquares) {
            if (isValidSquare(targetSquare, friendlyColor)) {

                //Make sure the knight is not moving an extra rank because of the board wrapping
                if (Math.abs(targetSquare % 8 - startSquare % 8) > 2) {
                    continue;
                }


                moves.add(new Move(startSquare, targetSquare, Piece.PEICE_SYMBOL.get(piece & 0b00_111)));
            }
        }

        return moves;
    }

    private boolean isValidSquare(int square, int friendlyColor) {
        if (square > 0 && square < 64) {
            return !Piece.IsColor(board[square], friendlyColor);
        }
        return false;
    }

    private List<Move> generateKingMoves(int startSquare, int piece) {
        List<Move> moves = new ArrayList<>();
        int friendlyColor = piece & 0b11_000;
        for (int directionOffset : directionOffsets) {
            int targetSquare = startSquare + directionOffset;
            if (isValidSquare(targetSquare, friendlyColor)) {
                moves.add(new Move(startSquare, targetSquare, Piece.PEICE_SYMBOL.get(piece & 0b00_111)));
            }
        }

        if (canCastleKingSide(friendlyColor)) {
            moves.add(new Move(startSquare, startSquare + 2, "O-O"));
        }

        if (canCastleQueenSide(friendlyColor)) {
            moves.add(new Move(startSquare, startSquare - 2, "O-O-O"));
        }


        return moves;
    }

    private boolean canCastleQueenSide(int friendlyColor) {

        boolean hasCastlingRights = friendlyColor == Piece.WHITE ? whiteQueenSideCastlingRights : blackQueenSideCastlingRights;
        if (!hasCastlingRights) {
            return false;
        }

        //Check if the squares between the king and rook are empty
        int[] squaresToCheck = {
                friendlyColor == Piece.WHITE ? 1 : 57, //b1, b8
                friendlyColor == Piece.WHITE ? 2 : 58, //c1, c8
                friendlyColor == Piece.WHITE ? 3 : 59  //d1, d8
        };

        for (int square : squaresToCheck) {
            if (board[square] != Piece.EMPTY) {
                return false;
            }
        }
        return true;
    }

    private boolean canCastleKingSide(int friendlyColor) {
        boolean hasCastlingRights = friendlyColor == Piece.WHITE ? whiteKingSideCastlingRights : blackKingSideCastlingRights;

        if(!hasCastlingRights){
            return false;
        }

        //Check if the squares between the king and rook are empty
        int[] squaresToCheck = {
                friendlyColor == Piece.WHITE ? 5 : 61,
                friendlyColor == Piece.WHITE ? 6 : 62
        };

        for (int square : squaresToCheck) {
            if (board[square] != Piece.EMPTY) {
                return false;
            }
        }

        return true;
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

            int color = (board[i] & 0b11_000);
            char piece = Piece.PEICE_SYMBOL.get(board[i] & 0b00_111);
            piece = color == Piece.WHITE ? Character.toUpperCase(piece) : Character.toLowerCase(piece);

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
