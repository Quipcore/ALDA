package algorithmDesign.project.chess2;

public class Move {

    private int startIndex;
    private int endIndex;
    private String piece;

//    private PieceToMove pieceToMove;



    public Move(int startIndex, int endIndex, char piece) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.piece = Character.toString(piece);
    }

    public Move(int startIndex, int endIndex, String piece) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.piece = piece;
    }

    public String getPiece() {
        return piece;
    }

    @Override
    public String toString() {

        if(piece.equalsIgnoreCase("o-o-o") || piece.equalsIgnoreCase("o-o")){
            return piece;
        }

        int startFile = 'a' + startIndex % 8;
        int startRank = 8 - startIndex / 8;

        int endFile = 'a' + endIndex % 8;
        int endRank = 8 - endIndex / 8;

        if(piece.equalsIgnoreCase("p")) {
            return "" + (char) endFile + (endRank);
        }

        return "Move{" +
                "startSquare=" + (char) startFile + (startRank) + ", " +
                "endSquare=" + (char) endFile + (endRank) + ", " +
                "piece=" + piece +
                '}';
    }
}
