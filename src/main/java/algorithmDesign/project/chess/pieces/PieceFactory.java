package algorithmDesign.project.chess.pieces;

public class PieceFactory {

    public static Piece createPiece(String piece){
        return switch (piece) {
            case "P" -> new Pawn(true);
            case "N" -> new Knight(true);
            case "B" -> new Bishop(true);
            case "R" -> new Rook(true);
            case "Q" -> new Queen(true);
            case "K" -> new King(true);
            case "p" -> new Pawn(false);
            case "n" -> new Knight(false);
            case "b" -> new Bishop(false);
            case "r" -> new Rook(false);
            case "q" -> new Queen(false);
            case "k" -> new King(false);
            default -> null;
        };
    }
}
