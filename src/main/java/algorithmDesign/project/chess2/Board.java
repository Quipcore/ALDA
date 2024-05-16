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
    private static Map<Character, Character> pieceToSymbol = Map.ofEntries(
            Map.entry('P', '♟'),
            Map.entry('N', '♞'),
            Map.entry('B', '♝'),
            Map.entry('R', '♜'),
            Map.entry('Q', '♛'),
            Map.entry('K', '♚'),
            Map.entry('p', '♙'),
            Map.entry('n', '♘'),
            Map.entry('b', '♗'),
            Map.entry('r', '♖'),
            Map.entry('q', '♕'),
            Map.entry('k', '♔'),
            Map.entry(' ', ' ')
    );

    private final Map<Character, Piece> pieceMap;
    private final List<Integer> visibleSquares;
    private final Piece[] board;
    private final String currentFen;
    private final String enPassantSquare;
    private final String castlingRights;
    private final Color colorToMove;
    private final int halfMoveClock;
    private final int fullMoveClock;


    //------------------------------------------------------------------------------------------------------------------

    public Board() {
        this(START_FEN);
    }

    //------------------------------------------------------------------------------------------------------------------

    public Board(String fen) {
        String[] splits = fen.split(" ");
        this.enPassantSquare = splits[3];
        pieceMap = new HashMap<>() {
            {
                put('P', new Pawn(Color.WHITE, enPassantSquare));
                put('N', new Knight(Color.WHITE));
                put('B', new Bishop(Color.WHITE));
                put('R', new Rook(Color.WHITE));
                put('Q', new Queen(Color.WHITE));
                put('K', new King(Color.WHITE, fen));

                put('p', new Pawn(Color.BLACK, enPassantSquare));
                put('n', new Knight(Color.BLACK));
                put('b', new Bishop(Color.BLACK));
                put('r', new Rook(Color.BLACK));
                put('q', new Queen(Color.BLACK));
                put('k', new King(Color.BLACK, fen));
            }
        };

        this.currentFen = fen;
        board = new Piece[64];

        setupBoard(this.board, splits[0]);
        this.colorToMove = fen.contains("w") ? Color.WHITE : Color.BLACK;
        this.castlingRights = splits[2];
        this.halfMoveClock = Integer.parseInt(splits[4]);
        this.fullMoveClock = Integer.parseInt(splits[5]);
        this.visibleSquares = generateVisibleSquares();
    }

    //------------------------------------------------------------------------------------------------------------------

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
                int numEast = 7 - file;

                numSquaresToEdge[square][0] = numNorth; //N
                numSquaresToEdge[square][1] = rank; //S
                numSquaresToEdge[square][2] = file; //W
                numSquaresToEdge[square][3] = numEast; //E
                numSquaresToEdge[square][4] = Math.min(numNorth, file); //NW
                numSquaresToEdge[square][5] = Math.min(rank, numEast); //SE
                numSquaresToEdge[square][6] = Math.min(numNorth, numEast); //NE
                numSquaresToEdge[square][7] = Math.min(rank, file); //SW
            }
        }

        return numSquaresToEdge;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isGameOver() {

        if(generateLegalMoves().isEmpty()){
            System.out.println("No moves left");
            return true;
        }

        if(!hasBothKings()){
            System.out.println("One of the kings is missing");
            return true;
        }

        if(halfMoveClock >= 2 * 50){
            System.out.println("Half move clock exceeded");
            return true;
        }

        if(hasInsufficientMaterial()){
            System.out.println("Insufficient material");
            return true;
        }
        return false;
//        return generateLegalMoves().isEmpty() || !hasBothKings() || halfMoveClock >= 2 * 50 || hasInsufficientMaterial();
    }

    //------------------------------------------------------------------------------------------------------------------

    private boolean hasInsufficientMaterial() {
        Map<Character, Integer> amountOfPieces = new HashMap<>();
        for (Piece piece : board) {
            if (piece != null) {
                amountOfPieces.put(piece.getSymbol(), amountOfPieces.getOrDefault(piece.getSymbol(), 0) + 1);
            }
        }

        //Only kings left
        if(amountOfPieces.size() == 2){
            return true;
        }

        int totalPieces = amountOfPieces.values().stream().mapToInt(Integer::intValue).sum();
        if(totalPieces == 3){
            return amountOfPieces.containsKey('B') || amountOfPieces.containsKey('b');
        }

        return false;
    }

    //------------------------------------------------------------------------------------------------------------------

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

    //------------------------------------------------------------------------------------------------------------------

    private List<Integer> generateVisibleSquares() {
        List<Integer> visibleSquares = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            Piece piece = board[i];
            if (piece != null && !piece.getColor().equals(colorToMove)) {
                List<Integer> moves = piece.getVisibleSquares(board, i);
                visibleSquares.addAll(moves);
            }
        }
        return visibleSquares.stream().distinct().toList();
    }

    //------------------------------------------------------------------------------------------------------------------

    public List<Move> generatePseudoLegalMoves() {
        List<Move> pseudoLegalMoves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            Piece piece = board[i];
            if (piece != null && piece.getColor().equals(colorToMove)) {
                pseudoLegalMoves.addAll(piece.getValidMoves(board, visibleSquares, i));
            }
        }
        return pseudoLegalMoves.stream().distinct().toList();
    }

    //------------------------------------------------------------------------------------------------------------------

    public List<Move> generateLegalMoves() {
        List<Move> legalMoves = new ArrayList<>();
        List<Move> pseudoLegalMoves = generatePseudoLegalMoves();
        for (Move move : pseudoLegalMoves) {
            Board newBoard = makeMove(move);
            if (newBoard.hasBothKings()) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    //------------------------------------------------------------------------------------------------------------------

    private boolean hasBothKings() {
        boolean hasWhiteKing = false;
        boolean hasBlackKing = false;
        for (Piece piece : board) {
            if (piece instanceof King) {
                if (piece.getColor().equals(Color.WHITE)) {
                    hasWhiteKing = true;
                } else {
                    hasBlackKing = true;
                }
            }
        }
        return hasWhiteKing && hasBlackKing;
    }

    //------------------------------------------------------------------------------------------------------------------

    public Board makeMove(Move move) {
        Piece[] newBoard = board.clone();

        if (move.isKingSideCastle()) {
            //Move the king
            int kingFrom = colorToMove.equals(Color.WHITE) ? 60 : 4;
            int kingTo = colorToMove.equals(Color.WHITE) ? 62 : 6;
            newBoard[kingTo] = newBoard[kingFrom];
            newBoard[kingFrom] = null;

            //Move the rook
            int rookFrom = colorToMove.equals(Color.WHITE) ? 63 : 7;
            int rookTo = colorToMove.equals(Color.WHITE) ? 61 : 5;
            newBoard[rookTo] = newBoard[rookFrom];
            newBoard[rookFrom] = null;

        } else if (move.isQueenSideCastle()) {
            //Move the king
            int kingFrom = colorToMove.equals(Color.WHITE) ? 60 : 4;
            int kingTo = colorToMove.equals(Color.WHITE) ? 58 : 2;
            newBoard[kingTo] = newBoard[kingFrom];
            newBoard[kingFrom] = null;

            //Move the rook
            int rookFrom = colorToMove.equals(Color.WHITE) ? 56 : 0;
            int rookTo = colorToMove.equals(Color.WHITE) ? 59 : 3;
            newBoard[rookTo] = newBoard[rookFrom];
            newBoard[rookFrom] = null;
        } else if (move.isEnPassant()) {
            newBoard[move.getTo()] = newBoard[move.getFrom()];
            newBoard[move.getFrom()] = null;
            newBoard[move.getTo() + (colorToMove.equals(Color.WHITE) ? -8 : 8)] = null;
            move.setCapturing(true);
        } else if (move.isPromotion()) {
            move.setCapturing(newBoard[move.getTo()] != null);
            newBoard[move.getTo()] = pieceMap.get(move.getPromotionPiece());
            newBoard[move.getFrom()] = null;

        } else {
            move.setCapturing(newBoard[move.getTo()] != null);
            newBoard[move.getTo()] = newBoard[move.getFrom()];
            newBoard[move.getFrom()] = null;
        }
        String fen = getFenFromBoard(newBoard, move);
        return new Board(fen);
    }

    //------------------------------------------------------------------------------------------------------------------

    private String getFenFromBoard(Piece[] board, Move latestMove) {
        return generateFenPos(board) + " " +
                (this.colorToMove.equals(Color.WHITE) ? "b" : "w") + " " +
                getCastlingRightsFromMove(latestMove) + " " +
                generateEnPassantSquare(latestMove) + " " +
                generateFenHalfMoveClock(latestMove) + " " +
                generateFenFullMoveClock();
    }

    //------------------------------------------------------------------------------------------------------------------

    private int generateFenFullMoveClock() {
        int fullMoveClock = this.fullMoveClock;
        if (this.colorToMove.equals(Color.WHITE)) {
            fullMoveClock++;
        }
        return fullMoveClock;
    }

    //------------------------------------------------------------------------------------------------------------------

    private int generateFenHalfMoveClock(Move latestMove) {
        int halfMoveClock = this.halfMoveClock;
        if (latestMove.isCapturing() || latestMove.isPawnMove()) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }
        return halfMoveClock;
    }

    //------------------------------------------------------------------------------------------------------------------

    private String generateEnPassantSquare(Move latestMove) {
        if (latestMove.isPawnDoublePush()) {
            char startFile = (char) ('a' + latestMove.getTo() % 8);
            int startRank = 8 - latestMove.getTo() / 8;
            return "" + startFile + startRank;
        } else {
            return "-";
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private String generateFenPos(Piece[] board) {

        StringBuilder fen = new StringBuilder();
        int emptySquares = 0;

        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0 && i != 0) { // at the start of each rank except the first one
                if (emptySquares != 0) {
                    fen.append(emptySquares);
                    emptySquares = 0;
                }
                fen.append('/');
            }

            if (board[i] == null) {
                emptySquares++;
            } else {
                if (emptySquares != 0) {
                    fen.append(emptySquares);
                    emptySquares = 0;
                }
                fen.append(board[i].getSymbol());
            }
        }

        if (emptySquares != 0) {
            fen.append(emptySquares);
        }

        return fen.toString();
    }

    //------------------------------------------------------------------------------------------------------------------

    private String getCastlingRightsFromMove(Move latestMove) {
        String castlingRights = this.castlingRights;
        if (latestMove.isKingMove() || latestMove.isKingSideCastle() || latestMove.isQueenSideCastle()) {
            String queenCastlingToRemove = colorToMove.equals(Color.WHITE) ? "Q" : "q";
            castlingRights = castlingRights.replace(queenCastlingToRemove, "");
            String kingCastlingToRemove = colorToMove.equals(Color.WHITE) ? "K" : "k";
            castlingRights = castlingRights.replace(kingCastlingToRemove, "");
        } else if (latestMove.isRookMove()) {
            int rookStart = latestMove.getFrom();
            char rookSymbol = colorToMove.equals(Color.WHITE) ? 'R' : 'r';
            if (rookStart == 0 && board[rookStart].getSymbol() == rookSymbol) {
                castlingRights = castlingRights.replace("q", "");
            } else if (rookStart == 7 && board[rookStart].getSymbol() == rookSymbol) {
                castlingRights = castlingRights.replace("k", "");
            } else if (rookStart == 56 && board[rookStart].getSymbol() == rookSymbol) {
                castlingRights = castlingRights.replace("Q", "");
            } else if (rookStart == 63 && board[rookStart].getSymbol() == rookSymbol) {
                castlingRights = castlingRights.replace("K", "");
            }
        }

        if (castlingRights.isBlank()) {
            castlingRights = "-";
        }
        return castlingRights;
    }

    //------------------------------------------------------------------------------------------------------------------
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
                System.out.printf("%d  | ", (8 - i / 8));
            }

            char piece = board[i] == null ? ' ' : board[i].getSymbol();
            piece = pieceToSymbol.get(piece);
            System.out.printf("%c | ", piece);
            if (i % 8 == 7) {
                System.out.println();
            }
        }
        System.out.println("   +---+---+---+---+---+---+---+---+");
        System.out.println("     A   B   C   D   E   F   G   H  ");
        System.out.println("FEN: " + currentFen);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Board{" +
                "currentFen='" + currentFen + '\'' +
                '}';
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getFen() {
        return currentFen;
    }
}
