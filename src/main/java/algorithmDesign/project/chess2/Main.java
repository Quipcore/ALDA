package algorithmDesign.project.chess2;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String debugFEN = "8/p5k1/3rQP1p/2b3p1/2b5/2pn1R2/P2K2B1/4R3 w - - 0 63";

    public static void main(String[] args) {

        playRandomGame();
    }

    public static void playRandomGame() {

        List<String> fens = new ArrayList<>();

        Board board = new Board();
        board.printBoard();
        System.out.println();
        for(int i = 0; i < 1000; i++){
            List<Move> moves = board.generateLegalMoves();
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
            fens.add(board.getFen());
        }
        System.out.println(board);

        //Pick out 10 random fens where halfmove clock is less than 5
        int displayCount = 0;
        List<String> fensDisplay = new ArrayList<>();
        while(displayCount < 10){
            String fen = fens.get((int) (Math.random() * fens.size()));
            int halfMoveClock = Integer.parseInt(fen.split(" ")[4]);
            if(halfMoveClock < 5 && !fensDisplay.contains(fen)){
                System.out.println(fen);
                fensDisplay.add(fen);
                displayCount++;
            }
        }
    }

}
