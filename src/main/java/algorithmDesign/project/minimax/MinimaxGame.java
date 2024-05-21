package algorithmDesign.project.minimax;

public class MinimaxGame {

    public static void main(String[] args) {
        MinimaxGame minimaxGame = new MinimaxGame();
        minimaxGame.run();
    }

    private void run() {
        Position startPosition = new Position(Player.WHITE);
        Position nextPosition = startPosition.getOptimalPosition();
    }
}
