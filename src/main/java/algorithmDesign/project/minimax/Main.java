package algorithmDesign.project.minimax;

import java.util.Random;

/**
 * Mini-max with alfa-beta pruning
 */

public class Main {

    Random random = new Random();

    public static void main(String[] args) {
        Main main = new Main();
        main.run();

    }

    public void run() {
        mPosition startPos = new mPosition();
        double eval = minimax(startPos, 3, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
        System.out.println(eval);
    }

    private double minimax(mPosition mPosition, int depth, double alpha, double beta, boolean maximizingPlayer) {
        System.out.printf("Height %d, Alpha %f, Beta %f, MaximizingPlayer %b\n",depth,alpha,beta,maximizingPlayer);
        if (depth == 0 || mPosition.isGameOver()) {
            return mPosition.evaluate();
        }

        if (maximizingPlayer) {
            return evaluateMax(mPosition, depth, alpha, beta);
        } else {
            return evaluateMin(mPosition, depth, alpha, beta);
        }
    }

    private double evaluateMax(mPosition mPosition, int depth, double alpha, double beta) {
        double maxEval = Double.NEGATIVE_INFINITY;
        for (mPosition childMPosition : mPosition.getChildren()) {
            double eval = minimax(childMPosition, depth - 1, alpha, beta, false);
            maxEval = Math.max(maxEval, eval);
            alpha = Math.max(alpha, eval);
            if (beta <= alpha) {
                break;
            }
        }
        return maxEval;
    }

    private double evaluateMin(mPosition mPosition, int depth, double alpha, double beta) {
        double minEval = Double.POSITIVE_INFINITY;
        for (mPosition childMPosition : mPosition.getChildren()) {
            double eval = minimax(childMPosition, depth - 1, alpha, beta, true);
            minEval = Math.min(minEval, eval);
            beta = Math.min(beta, eval);
            if (beta <= alpha) {
                break;
            }
        }
        return minEval;
    }


    class mPosition {
        private double eval;
        private mPosition[] children;

        public mPosition() {
            eval = random.nextInt(-100, 100);
            children = new mPosition[2];
        }

        public double evaluate() {
            return eval;
        }

        public boolean isGameOver() {
            return false;
        }

        public mPosition[] getChildren() {
            children[0] = new mPosition();
            children[1] = new mPosition();
            return children;
        }
    }
}
