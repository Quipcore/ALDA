package algorithmDesign.project.chess2;

import java.util.List;

public class Main {

    private static final String ONLY_QUEENS_FEN = "8/8/8/8/4Q3/8/8/8 w - - 0 1";

    public static void main(String[] args) {
        Board board = new Board(ONLY_QUEENS_FEN);
        board.printBoard();
        List<Move> moves = board.generateMoves();
        System.out.println(moves.size());
    }
}
