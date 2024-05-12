package algorithmDesign.project.chess2;

import java.util.List;

public class Main {

    private static final String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String debugFEN = "8/p5k1/3rQP1p/2b3p1/2b5/2pn1R2/P2K2B1/4R3 w - - 0 63";

    public static void main(String[] args) {

        playRandomGame();
    }

    public static void playRandomGame() {
        Board board = new Board();
        board.printBoard();
        System.out.println();
        for(int i = 0; i < 1000; i++){
            List<Move> moves = board.generateMoves();
            if (moves.isEmpty()) {
                System.out.println("Moves are empty");
                break;

            }
            Move move = moves.get((int) (Math.random() * moves.size()));
            Board newBoard = board.makeMove(move);
            if(newBoard.isGameOver()){
                System.out.println("Game over");
                break;
            }
            board = board.makeMove(move);
            board.printBoard();
            System.out.println();
        }
        System.out.println(board);
    }

}
