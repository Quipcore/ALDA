package algorithmDesign.project.chess;

import java.util.List;

import java.util.HashMap;
import java.util.Map;

import java.awt.*;
import java.util.stream.Collectors;


public class Chess {

    private static final Color WHITE = new Color(0xf0d9b5);
    private static final Color BLACK = new Color(0xb58863);

    private static final String START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String E4_FEN = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    private static final String C5_FEN = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";

    private static final Map<Integer, String> peiceStartIndex = new HashMap<>();

    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;


    public static void main(String[] args) {
        Chess chess = new Chess();
        chess.run();
    }

    private void run() {
        List<Board> gameStates = generateFens(START_FEN);




    }

    private List<Board> generateFens(String fen) {
        return new Board(fen).getPossibleGameStates();
    }
}
