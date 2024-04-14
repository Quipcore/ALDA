package algorithmDesign.project.chess2;

public class Move {
    private int startIndex;
    private int endIndex;
    private char piece;

    public Move(int startIndex, int endIndex, char piece) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.piece = piece;
    }

    public char getPiece() {
        return piece;
    }

    @Override
    public String toString() {

        int startFile = 'A' + startIndex % 8;
        int startRank = 8- startIndex / 8;

        int endFile = 'A' + endIndex % 8;
        int endRank = 8- endIndex / 8;


        return "Move{" +
                "startSquare=" + (char)startFile + (startRank) + ", " +
                "endSquare=" + (char)endFile + (endRank) + ", " +
                "piece=" + piece +
                '}';
//        String move = ((char)startFile + "" + (startRank) + (char)endFile + (endRank)).toLowerCase();
//        if(piece == 'P' || piece == 'p'){
//            return move;
//        }
//        return (move + piece).toLowerCase();
    }
}
