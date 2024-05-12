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

    public boolean isKingSideCastle(){
        return piece.equalsIgnoreCase("o-o");
    }

    public boolean isQueenSideCastle(){
        return piece.equalsIgnoreCase("o-o-o");
    }

    public boolean isEnPassant() {
        return piece.equalsIgnoreCase("ePassant");
    }

    public String getPiece() {
        return piece;
    }

    @Override
    public String toString() {

        if(piece.equalsIgnoreCase("o-o-o") || piece.equalsIgnoreCase("o-o")){
            return "Move{" +
                    "piece=" + piece +
                    "}";
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

    public int getTo() {
        return endIndex;
    }

    public int getFrom() {
        return startIndex;
    }


    public boolean isPawnDoublePush() {
        return piece.equalsIgnoreCase("p") && Math.abs(startIndex - endIndex) == 16;
    }

    public boolean isRookMove() {
        return piece.equalsIgnoreCase("r");
    }

    public boolean isKingMove() {
        return piece.equalsIgnoreCase("k");
    }
}
