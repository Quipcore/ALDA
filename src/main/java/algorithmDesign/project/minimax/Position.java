package algorithmDesign.project.minimax;

import algorithmDesign.project.chess2.Board;
import algorithmDesign.project.chess2.Move;

import java.util.Collection;
import java.util.HashSet;

public class Position {

    private static final double ALFA = Double.NEGATIVE_INFINITY;
    private static final double BETA = Double.POSITIVE_INFINITY;
    private static final int INIT_SEARCH_DEPTH = 4;

    private final Player currentPlayer;
    private final double evaluation;
    private Collection<Position> childPositions;
    private Board board;
    private double potential;
    private Move move;

    //------------------------------------------------------------------------------------------------------------------

    public Position(Player player) {
        this.currentPlayer = player;
        board = new Board();
        evaluation = board.getEvaluation();
    }

    //------------------------------------------------------------------------------------------------------------------


    public Position(Player player, Board board, Move move) {
        this(player);
        this.board = board;
        this.move = move;
    }

    //------------------------------------------------------------------------------------------------------------------

    public Move getMove() {
        return move;
    }

    //------------------------------------------------------------------------------------------------------------------


    public Player nextPlayer() {
        return currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
    }

    //------------------------------------------------------------------------------------------------------------------


    public boolean isGameOver() {
        return board.isGameOver();
    }

    //------------------------------------------------------------------------------------------------------------------


    public Collection<Position> getChildren() {

        if (childPositions == null) {
            generateChildPositions();
        }

        return childPositions;
    }

    //------------------------------------------------------------------------------------------------------------------

    private void generateChildPositions() {
        Player opponent = nextPlayer();
        childPositions = new HashSet<>();
        for(Move move : board.generateLegalMoves()){
            Board childBoard = null;
            try {
                childBoard = board.makeMove(move);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(childBoard == null){
                continue;
            }

            Position childPosition = new Position(opponent,childBoard, move);
            childPositions.add(childPosition);
        }

        if(childPositions.contains(null)){
            System.out.println("Null child position");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    
    /**
     * Returns the static evaluation of the current position. King capture is considered mate and is given a value of  -10000 for white and 10000 for black
     *
     * @return the static evaluation of the current position
     */

    public double getEvaluation() {
        if(isMate()){
            return currentPlayer == Player.WHITE ? -10000 : 10000;
        }
        return evaluation;
    }

    //------------------------------------------------------------------------------------------------------------------


    private double getPotential() {
        return potential;
    }

    //------------------------------------------------------------------------------------------------------------------


    private void setPotential(double potential) {
        this.potential = potential;
    }


    /**
     * Preforms a search to find the most optimal next position for the current position and player
     *
     * @return the optimal position for the current player
     */
    public Position getOptimalPosition() {
        return mimimaxPosition(this, INIT_SEARCH_DEPTH, ALFA, BETA);
    }

    //------------------------------------------------------------------------------------------------------------------


    private Position mimimaxPosition(Position position, int depth, double alpha, double beta) {
        if (depth == 0 || position.isGameOver()) {
            position.setPotential(position.getEvaluation());
            return position;
        }

        if (position.currentPlayer == Player.WHITE) {
            return findMaxPosition(position, depth, alpha, beta);
        } else {
            return findMinPosition(position, depth, alpha, beta);
        }
    }

    private boolean isMate() {
        return board.isMate();
    }

    //------------------------------------------------------------------------------------------------------------------

    private Position findMaxPosition(Position position, int depth, double alpha, double beta) {
        double maxEval = Double.NEGATIVE_INFINITY;
        Position maxPosition = null;

        for (Position childPosition : position.getChildren()) {
            double eval = mimimaxPosition(childPosition, depth - 1, alpha, beta).getPotential();
            if (eval > maxEval) {
                maxEval = eval;
                maxPosition = childPosition;
            }

            alpha = Math.max(alpha, maxEval);
            if (beta <= alpha) {
                break;
            }
        }

        position.setPotential(maxEval);
        return maxPosition;
    }

    //------------------------------------------------------------------------------------------------------------------


    private Position findMinPosition(Position position, int depth, double alpha, double beta) {
        double minEval = Double.POSITIVE_INFINITY;
        Position minPosition = null;

        for (Position childPosition : position.getChildren()) {
            double eval = mimimaxPosition(childPosition, depth - 1, alpha, beta).getPotential();
            if (eval < minEval) {
                minPosition = childPosition;
                minEval = eval;
            }
            beta = Math.min(beta, minEval);
            if(beta <= alpha){
                break;
            }
        }
        position.setPotential(minEval);
        return minPosition;
    }

    //------------------------------------------------------------------------------------------------------------------


    @Override
    public String toString() {
        return "Position{" +
                "currentPlayer=" + currentPlayer +
                ", evaluation=" + evaluation +
                ", potential=" + potential +
                ", fen='" + board.getFen() + '\'' +
                '}';
    }

    public Board getBoard() {
        return board;
    }
}
