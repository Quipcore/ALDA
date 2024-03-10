package algorithmDesign.project;

import java.util.Random;

/**
 * Represents the game state of a chosen game
 */
public class Position {

    private static final Random random = new Random();

    private static final int INIT_SEARCH_DEPTH = 5;
    private static final double ALFA = Double.NEGATIVE_INFINITY;
    private static final double BETA = Double.POSITIVE_INFINITY;

    private final Player currentPlayer;
    private final double evaluation;
    private Position[] childPositions;

    private double potential;
    private double alfa;
    private double beta;

    public Position(Player player) {
        this.currentPlayer = player;
        evaluation = random.nextInt(-100, 100);
        this.alfa = ALFA;
        this.beta = BETA;
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
        return mimimaxPosition(this, INIT_SEARCH_DEPTH);
    }

    private Position mimimaxPosition(Position position, int depth) {
        if (depth == 0 || position.isGameOver()) {
            position.setPotential(position.getEvaluation());
            return position;
        }

        //If whites turn to move MAXIMIZE Position eval
        if (position.currentPlayer == Player.WHITE) {
            return findMaxPosition(position, depth);
        }
        //If blacks turn to move MINIMIZE Position eval
        else {
            return findMinPosition(position, depth);
        }
    }

    //TODO IMPLEMENT A-B-Pruning
    private Position findMaxPosition(Position position, int depth) {
        double maxEval = Double.NEGATIVE_INFINITY;
        Position maxPosition = null;

        for (Position childPosition : position.getChildren()) {
            double eval = mimimaxPosition(childPosition, depth - 1).getPotential();
            if (eval > maxEval) {
                maxEval = eval;
                maxPosition = childPosition;
            }

//            alpha = Math.max(alpha, maxEval);
//            if(beta <= alpha){
//                break;
//            }
        }
        position.setPotential(maxEval);
        return maxPosition;
    }

    //TODO IMPLEMENT A-B-Pruning
    private Position findMinPosition(Position position, int depth) {
        double minEval = Double.POSITIVE_INFINITY;
        Position minPosition = null;

        for (Position childPosition : position.getChildren()) {
            double eval = mimimaxPosition(childPosition, depth - 1).getPotential();
            if (eval < minEval) {
                minPosition = childPosition;
                minEval = eval;
            }
//            beta = Math.min(beta, eval);
//            if (beta <= alpha) {
//                break;
//            }
        }
        position.setPotential(minEval);
        return minPosition;
    }
}
