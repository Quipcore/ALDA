package algorithmDesign.project.minimax;

import java.util.Random;

/**
 * Represents the game state of a chosen game
 */
public class Position {

    private static final Random random = new Random();
    private static final double ALFA = Double.NEGATIVE_INFINITY;
    private static final double BETA = Double.POSITIVE_INFINITY;
    private static final int INIT_SEARCH_DEPTH = 20;

    private final Player currentPlayer;
    private final double evaluation;
    private Position[] childPositions;

    private double potential;

    public Position(Player player) {
        this.currentPlayer = player;
        evaluation = random.nextInt(-100, 100);
    }

    public Player nextPlayer() {
        return currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
    }

    public boolean isGameOver() {
        return false;
    }

    public Position[] getChildren() {

        if (childPositions == null) {
            generateChildPositions();
        }

        return childPositions;
    }

    private void generateChildPositions() {

        Player opponent = nextPlayer();

        childPositions = new Position[2];
        childPositions[0] = new Position(opponent);
        childPositions[1] = new Position(opponent);
    }

    public Player currentPlayer() {
        return currentPlayer;
    }

    public double getEvaluation() {
        return evaluation;
    }

    private double getPotential() {
        return potential;
    }

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


    //TODO IMPLEMENT A-B-Pruning
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

        //TODO IMPLEMENT A-B-Pruning
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
}
