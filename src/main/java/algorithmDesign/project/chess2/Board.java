package algorithmDesign.project.chess2;

import algorithmDesign.project.chess2.pieces.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {

    public static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final int[][] NUM_SQUARES_TO_EDGE = preComputeMoveData();
    public static final int[] DIRECTION_OFFSETS = {
            8, -8, 1, -1, 7, -7, 9, -9
    };
    private static final String fenRegex = "^([rnbqkpRNBQKP1-8]+\\/){7}[rnbqkpRNBQKP1-8]+ [wb] [KQkq-]{1,4} (-|([a-h][1-8])) [0-9]+ [0-9]+$";
    private static final Random random = new Random();
    private static final Map<String, Integer> repetitionMap = new HashMap<>();

    private static final Map<Character, Character> pieceToSymbol = Map.ofEntries(
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

    /**
     * Creates a new board with the default starting position
     */
    public Board() {
        this(START_FEN);
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Creates a chess board from the given FEN string.
     *
     * @param fen The FEN of the board
     * @throws IllegalArgumentException If the FEN is invalid
     * @throws IllegalStateException    If the FEN results in an illegal board state
     */
    public Board(String fen) {

        if(fen == null){
            throw new IllegalArgumentException("FEN cannot be null");
        }
        if(fen.isBlank()){
            throw new IllegalArgumentException("FEN cannot be blank");
        }
        if(!fen.matches(fenRegex)){
            throw new IllegalArgumentException("Invalid FEN: " + fen);
        }

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

        this.colorToMove = splits[1].equals("w") ? Color.WHITE : Color.BLACK;
        this.castlingRights = splits[2];
        this.halfMoveClock = Integer.parseInt(splits[4]);
        this.fullMoveClock = Integer.parseInt(splits[5]);
        this.visibleSquares = generateVisibleSquares();

        if(isGameOver()){
            throw new IllegalStateException("Generated board is in an illegal state");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Computes the number of squares to the edge of the board in each direction. Where first index is the square and the second index is the direction.
     * Directions are as follows: N, S, W, E, NW, SE, NE, SW
     * if a piece is on square 5, the number of squares to the edge of the board in each direction is as follows:
     * [2, 5, 1, 2, 2, 2, 2, 1]. That means there are 2 squares to the north, 5 to the south, 1 to the west, 2 to the east, 2 to the northwest, 2 to the southeast, 2 to the northeast and 1 to the southwest.
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
            return true;
        }

        if(!hasBothKings()){
            return true;
        }

        if(halfMoveClock >= 2 * 50){
            return true;
        }

        if(hasInsufficientMaterial()){
            return true;
        }

        if(repetitionMap.getOrDefault(currentFen,0)>= 3){
            return true;
        }
        return false;
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
//        List<Integer> visibleSquares = new ArrayList<>();
//        for (int i = 0; i < board.length; i++) {
//            Piece piece = board[i];
//            if (piece != null && !piece.getColor().equals(colorToMove)) {
//                List<Integer> moves = piece.getVisibleSquares(board, i);
//                visibleSquares.addAll(moves);
//            }
//        }
//        return visibleSquares.stream().distinct().toList();
        return new ArrayList<>();
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Generates a list of pseudo legal moves for the current board state.
     * A pseudo legal move is a move that is valid but doesn't check for move constrains such as checks or pins.
     *
     * @return A list of possible pseudo legal moves that can be made on the current board
     */
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

    /**
     * Generates a list of legal moves for the current board state.
     * A legal move is a move that is valid according to the rules of chess.
     *
     * @return A list of possible legal moves that can be made on the current board
     */
    public List<Move> generateLegalMoves() {
//        List<Move> legalMoves = new ArrayList<>();
//        List<Move> pseudoLegalMoves = generatePseudoLegalMoves();
//        for (Move move : pseudoLegalMoves) {
//            Board newBoard = makeMove(move);
//            if (newBoard.hasBothKings()) {
//                legalMoves.add(move);
//            }
//        }
//        return legalMoves;
        return generatePseudoLegalMoves();
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

    /**
     * Makes a move on the board and returns the new board state.
     * !IMPORTANT! This method does not check if the move is legal, it is assumed that the move is legal.
     * If the move is not legal, the board state will be in an illegal state but no exception will be thrown.
     * @param move The move to make on the board
     * @return The new board state after the move has been made
     * @throws IllegalArgumentException If the move is null or if the target square of the move is outside the boards 64 square range
     * @throws InvalidMoveException If the move doesn't match the regex of a valid move
     */
    public Board makeMove(Move move) throws InvalidMoveException {
        if(move == null){
            throw new IllegalArgumentException("Move cannot be null");
        }

        if(move.getTo() < 0 || move.getTo() >= 64){
            throw new IllegalArgumentException("Target square of move outside boards 64 square range: " + move);
        }

        if(!move.toString().matches("^([rnbqkRNBQK]?[a-h][1-9]|o-o|o-o-o|O-O|O-O-O|enPassant)$")){
            throw new InvalidMoveException(move.toString());
        }

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

        try{
            return new Board(fen);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private String getFenFromBoard(Piece[] board, Move latestMove) {
        return generateFenPos(board) + " " +
                (this.colorToMove.equals(Color.WHITE) ? "b" : "w") + " " +
                generateFenCastlingRightsFromMove(latestMove) + " " +
                generateFenEnPassantSquare(latestMove) + " " +
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

    private String generateFenEnPassantSquare(Move latestMove) {
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

    private String generateFenCastlingRightsFromMove(Move latestMove) {
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

    /**
     * Prints the current board state to the system out.
     * Displays the board, using ascii characters and the FEN of the board.
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

    //------------------------------------------------------------------------------------------------------------------


    public void updateRepetitions() {
        repetitionMap.put(currentFen, repetitionMap.getOrDefault(currentFen, 0) + 1);
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * Returns the evaluation of the current board state.
     * @return The evaluation of the current board state
     */
    public double getEvaluation() {
        double eval = 0;
        for(int i = 0; i < board.length; i++){
            Piece piece = board[i];
            if(piece != null){
                double pieceValue = piece.getPieceValue(i);
                eval += pieceValue;
            }
        }

        return eval;
    }

    public boolean isMate() {
        return !hasBothKings();
    }
}
