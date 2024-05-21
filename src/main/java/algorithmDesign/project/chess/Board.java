package algorithmDesign.project.chess;

import algorithmDesign.project.chess.pieces.Piece;
import algorithmDesign.project.chess.pieces.PieceFactory;

import java.util.*;

public class Board {

    private Piece[][] board;
    private String fen;
    private boolean whiteToMove;
    private String castlingRights;
    private String enPassantSquare;
    private int halfMoveClock;
    private int fullMoveNumber;


    public Board(String fen) {
        board = new Piece[8][8];
        this.fen = fen;

        String[] parts = fen.split(" ");

        fillBoard(parts[0]);
        this.whiteToMove = parts[1].equals("w");

        this.castlingRights = parts[2];
        this.enPassantSquare = parts[3];

        this.halfMoveClock = Integer.parseInt(parts[4]);
        this.fullMoveNumber = Integer.parseInt(parts[5]);
    }

    public static String convertPositionToNotation(int rank, int file){
        char fileChar = (char)('a' + file);
        int rankInt = rank + 1;
        return "" + fileChar + rankInt;
    }

    private void fillBoard(String part) {
        String[] ranks = part.split("/");
        for(int rankIndex = 0; rankIndex < ranks.length; rankIndex++){
            String rank = ranks[rankIndex];
            int file = 0;
            for(char c : rank.toCharArray()){
                if(Character.isDigit(c)){
                    file += Character.getNumericValue(c);
                } else {
                    board[rankIndex][file] = PieceFactory.createPiece(String.valueOf(c));
                    file++;
                }
            }
        }
    }

    public String getFen() {
        return fen;
    }

    /**
     * Returns the most basic evaluation of the board using peice values
     * @return the evaluation score
     */
    public int evaluate() {

        int score = 0;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Piece piece = board[i][j];
                if(piece == null){
                    continue;
                }
                score += piece.getPoints();
            }
        }

        return score;
    }

    //Skips en passant and castling
    public List<Board> getPossibleGameStates() {

        List<Board> possibleBoardStates = new ArrayList<>();

        for(int rank = 0; rank < board.length; rank++){
            for(int file = 0; file < board[rank].length; file++){
                Piece piece = board[rank][file];

                if(piece == null){
                    continue;
                }

                if(whiteToMove && piece.isWhite()){
                    List<Board> boards = getPossibleMovesFromPiece(piece, rank, file);
                    possibleBoardStates.addAll(boards);

                } else if (!whiteToMove && piece.isBlack()) {
                    List<Board> boards = getPossibleMovesFromPiece(piece, rank, file);
                    possibleBoardStates.addAll(boards);
                }
            }
        }


        return possibleBoardStates.stream().distinct().toList();
    }

    private List<Board> getPossibleMovesFromPiece(Piece piece, int rank, int file) {
        List<String> moves = piece.getValidMoves(this, rank, file);

        List<Board> boards = new ArrayList<>();
        for(String move : moves){
            Board tempBoard = getBoardAfterMove(move, rank, file, piece);
            boards.add(tempBoard);
        }
        return boards;
    }

    private Board getBoardAfterMove(String move, int rank, int file, Piece piece) {
        Board tempBoard = new Board(fen);
        tempBoard.board[rank][file] = null;
        tempBoard.board[move.charAt(0) - '0'][move.charAt(1) - '0'] = piece;

        tempBoard.whiteToMove = !whiteToMove;

        if(!piece.getNotation().equalsIgnoreCase("P") && !move.contains("x")){
            tempBoard.halfMoveClock++;
        } else {
            tempBoard.halfMoveClock = 0;
        }

        tempBoard.recalculateFen();
        return tempBoard;
    }

    private void recalculateFen() {
        StringBuilder sb = new StringBuilder();
        for(int rank = 0; rank < board.length; rank++){
            int empty = 0;
            for(int file = 0; file < board[rank].length; file++){
                if(board[rank][file] == null){
                    empty++;
                } else {
                    if(empty != 0){
                        sb.append(empty);
                        empty = 0;
                    }
                    sb.append(board[rank][file].getNotation());
                }
            }
            if(empty != 0){
                sb.append(empty);
            }
            if(rank != 7){
                sb.append("/");
            }
        }
        sb.append(" ");
        sb.append(whiteToMove ? "w" : "b");
        sb.append(" ");
        sb.append(castlingRights);
        sb.append(" ");
        sb.append(enPassantSquare);
        sb.append(" ");
        sb.append(halfMoveClock);
        sb.append(" ");
        sb.append(fullMoveNumber);

        fen = sb.toString();
    }


    @Override
    public String toString() {
        return fen;
    }

    public void printPosition() {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Board board1 = (Board) object;

        return this.fen.equals(board1.fen);
    }

    @Override
    public int hashCode() {
        return 31 * (fen != null ? fen.hashCode() : 0);
    }

    public Piece getPieceAt(int rank, int file) {
        return board[rank][file];
    }
}
