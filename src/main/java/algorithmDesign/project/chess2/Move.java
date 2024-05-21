package algorithmDesign.project.chess2;

public class Move {

    private final int startIndex;
    private final int endIndex;
    private final String piece;
    private boolean isCapturing;
    private char promotionPiece;

    //------------------------------------------------------------------------------------------------------------------

    public Move(int startIndex, int endIndex, char piece) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.piece = Character.toString(piece);
    }

    //------------------------------------------------------------------------------------------------------------------

    public Move(int startIndex, int endIndex, String piece) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.piece = piece;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isKingSideCastle() {
        return piece.equalsIgnoreCase("o-o");
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isQueenSideCastle() {
        return piece.equalsIgnoreCase("o-o-o");
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isEnPassant() {
        return piece.equalsIgnoreCase("ePassant");
    }

    //------------------------------------------------------------------------------------------------------------------

    public String getPiece() {
        return piece;
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        if (piece.equalsIgnoreCase("o-o-o") || piece.equalsIgnoreCase("o-o")) {
//            return "Move{" +
//                    "piece=" + piece +
//                    "}";
            return piece;
        }

        int startFile = 'a' + startIndex % 8;
        int startRank = 8 - startIndex / 8;

        int endFile = 'a' + endIndex % 8;
        int endRank = 8 - endIndex / 8;


        String piece = this.piece;
        if (this.piece.equalsIgnoreCase("p")) {
            piece = "";
//            return "" + (char) endFile + (endRank);
        }

        return piece + (char) endFile + (endRank);

//        return "Move{" +
//                "startSquare=" + (char) startFile + (startRank) + ", " +
//                "endSquare=" + (char) endFile + (endRank) + ", " +
//                "piece=" + piece +
//                '}';
    }

    public int getTo() {
        return endIndex;
    }

    //------------------------------------------------------------------------------------------------------------------

    public int getFrom() {
        return startIndex;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isPawnDoublePush() {
        return piece.equalsIgnoreCase("p") && Math.abs(startIndex - endIndex) == 16;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isRookMove() {
        return piece.equalsIgnoreCase("r");
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isKingMove() {
        return piece.equalsIgnoreCase("k");
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isPawnMove() {
        return piece.equalsIgnoreCase("p");
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isCapturing() {
        return isCapturing;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void setCapturing(boolean isCapturing) {
        this.isCapturing = isCapturing;
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean isPromotion() {
        return piece.equalsIgnoreCase("p") && (endIndex < 8 || endIndex > 55);
    }

    //------------------------------------------------------------------------------------------------------------------

    public char getPromotionPiece() {
        return promotionPiece;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void setPromotionPiece(char promotionPiece) {
        this.promotionPiece = promotionPiece;
    }
}
