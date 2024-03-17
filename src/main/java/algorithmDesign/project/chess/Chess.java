package algorithmDesign.project.chess;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Chess {

    private static final Color WHITE = new Color(0xf0d9b5);
    private static final Color BLACK = new Color(0xb58863);
    
    private static final Map<Integer, String> peiceStartIndex = new HashMap<>();

    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;


    public static void main(String[] args) {
        Chess chess = new Chess();
        chess.run();
    }

    private void run() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);


        JPanel chessBoard = generateChessBoard();
        frame.add(chessBoard);

        frame.pack();
        frame.setVisible(true);
    }

    private JPanel generateChessBoard() {
        JPanel chessBoard = new JPanel();
        chessBoard.setLayout(new GridLayout(BOARD_WIDTH, BOARD_HEIGHT));

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                JPanel square = new JPanel();

                int squareIndex = i * BOARD_WIDTH + j;

                if((squareIndex / 8) % 2 == 0) {
                    square.setBackground(squareIndex % 2 == 0 ? WHITE : BLACK);
                } else {
                    square.setBackground(squareIndex % 2 == 0 ? BLACK : WHITE);
                }
                
                square.add(new JLabel(peiceStartIndex.get(squareIndex)));
                chessBoard.add(square);
            }
        }
        return chessBoard;
    }
}
