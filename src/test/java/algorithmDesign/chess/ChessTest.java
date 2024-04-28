package algorithmDesign.chess;

import algorithmDesign.project.chess2.Board;
import algorithmDesign.project.chess2.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChessTest {

    private static final String START_FEN = Board.START_FEN;

    @Test
    public void testInitialMoveAmountMatches20() {
        Board board = new Board();
        assertEquals(20, board.generateMoves().size());
    }

    @Test
    public void testDefaultConstructorGivesSTARTFENasToString() {
        Board board = new Board();
        Assertions.assertTrue(board.toString().contains(START_FEN));
    }

    @Test
    public void testMoveAmountOnEmptyBoard() {
        Board board = new Board("8/8/8/8/8/8/8/8 w - - 0 1");
        assertEquals(0, board.generateMoves().size());
    }

    @Test
    public void testMoveAmountOnDefaultStart() {
        Board board = new Board();

        List<Move> moves = board.generateMoves();
        assertEquals(20, moves.size());
    }

    @Test
    public void testPawnMovesFromStart() {
        Board board = new Board("8/8/8/8/8/8/P7/8 w - - 0 1");
        assertEquals(2, board.generateMoves().size());
    }

    @Test
    public void testRookMovesInCorner() {
        Board board = new Board("8/8/8/8/8/8/8/R7 w - - 0 1");
        assertEquals(14, board.generateMoves().size());
    }

    @Test
    public void testBishopMovesInCorner() {
        Board board = new Board("8/8/8/8/8/8/8/B7 w - - 0 1");
        assertEquals(7, board.generateMoves().size());
    }

    @Test
    public void testQueenMovesInCorner() {
        Board board = new Board("8/8/8/8/8/8/8/Q7 w - - 0 1");
        assertEquals(21, board.generateMoves().size());
    }

    @Test
    public void testMoveAmountOnSingleQueenBoard() {
        Board board = new Board("8/8/8/8/4Q3/8/8/8 w - - 0 1");
        assertEquals(27, board.generateMoves().size());
    }

    @Test
    public void testMoveAmountOnSingleKingBoard() {
        Board board = new Board("8/8/8/8/4K3/8/8/8 w - - 0 1");
        assertEquals(8, board.generateMoves().size());
    }

    @Test
    public void testMoveAmountOnSinglePawnBoard() {
        Board board = new Board("8/8/8/8/4P3/8/8/8 w - - 0 1");
        assertEquals(1, board.generateMoves().size());
    }

    @Test
    public void testMoveAmountOnSingleKnightBoard() {
        Board board = new Board("8/8/8/8/4N3/8/8/8 w - - 0 1");
        assertEquals(8, board.generateMoves().size());
    }

    @Test
    public void testMoveAmountOnSingleRookBoard() {
        Board board = new Board("8/8/8/8/4R3/8/8/8 w - - 0 1");
        assertEquals(14, board.generateMoves().size());
    }

    @Test
    public void testMoveAmountOnSingleBishopBoard() {
        Board board = new Board("8/8/8/8/4B3/8/8/8 w - - 0 1");
        assertEquals(13, board.generateMoves().size());
    }

    @Test
    public void testEnPassantWestCaptureInclusionInMoveList(){
        String fen = "8/8/8/4pP2/8/8/8/8 w - e6 0 1";
        Board board = new Board(fen);
        List<Move> moves = board.generateMoves();
        assertEquals(2, moves.size());
    }

    @Test
    public void testEnPassantEastCaptureInclusionInMoveList(){
        String fen = "8/8/8/4Pp2/8/8/8/8 w - f6 0 1";
        Board board = new Board(fen);
        List<Move> moves = board.generateMoves();
        assertEquals(2, moves.size());
    }

    @Test
    public void testFullWhiteCastlingRights(){
        String fen = "8/8/8/8/8/8/8/4K3 w KQkq - 0 1";
        Board board = new Board(fen);
        List<Move> moves = board.generateMoves();
        assertEquals(7, moves.size());
    }

    @Test
    public void testFullRightCastlingRights(){
        String fen = "4k3/8/8/8/8/8/8/8 b KQkq - 0 1";
        Board board = new Board(fen);
        List<Move> moves = board.generateMoves();
        assertEquals(7, moves.size());
    }

    @Test
    public void testWhiteKingSideCastlingRight(){
        String fen = "8/8/8/8/8/8/8/4K3 w Kkq - 0 1";
        Board board = new Board(fen);
        List<Move> moves = board.generateMoves();
        assertEquals(6, moves.size());
    }

    @Test
    public void testWhiteQueenSideCastlingRight(){
        String fen = "8/8/8/8/8/8/8/4K3 w Qkq - 0 1";
        Board board = new Board(fen);
        List<Move> moves = board.generateMoves();
        assertEquals(6, moves.size());
    }

    @Test
    public void testLackOfCastlingRights(){
        String fen = "8/8/8/8/8/8/8/4K3 w - - 0 1";
        Board board = new Board(fen);
        List<Move> moves = board.generateMoves();
        assertEquals(5, moves.size());
    }

    @Test
    public void testPrintingBoard(){
        Board board = new Board();
        board.printBoard();
    }

    @Test
    public void testPieceBlockingQueenSideCastling(){
        String fen = "8/8/8/1q6/8/8/8/R3K2R w KQkq - 0 1";
        Board board = new Board(fen);
        board.printBoard();
        List<Move> moves = board.generateMoves();
        boolean doesntContains = moves.stream().noneMatch(move -> move.toString().equalsIgnoreCase("O-O-O"));
        assertTrue(doesntContains);
    }
}
