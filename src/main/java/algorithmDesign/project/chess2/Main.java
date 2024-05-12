package algorithmDesign.project.chess2;

import java.util.List;

public class Main {

    private static final String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static void main(String[] args) {

        playRandomGame();
    }

    public static void playRandomGame() {
        Board board = new Board(FEN);
        board.printBoard();
        System.out.println();
        for(int i = 0; i < 100; i++) {
            List<Move> moves = board.generateMoves();
            if (moves.isEmpty()) {
                break;
            }
            Move move = moves.get((int) (Math.random() * moves.size()));
            board = board.makeMove(move);
            board.printBoard();
            System.out.println();
        }
        System.out.println(board);
    }

}
