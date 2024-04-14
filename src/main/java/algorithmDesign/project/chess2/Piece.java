package algorithmDesign.project.chess2;

import java.util.HashMap;
import java.util.Map;

public class Piece {
    public static final int EMPTY  = 0b00_000;
    public static final int PAWN   = 0b00_001;
    public static final int KNIGHT = 0b00_010;
    public static final int BISHOP = 0b00_011;
    public static final int ROOK   = 0b00_100;
    public static final int QUEEN  = 0b00_101;
    public static final int KING   = 0b00_110;
    public static final int WHITE  = 0b01_000;
    public static final int BLACK  = 0b10_000;

    private static final char SPACE_CHAR = 0x20;

    //Might want to hid these maps in a getter method
    public static Map<Integer, Character> PEICE_SYMBOL = new HashMap<>(
            Map.of(
                    EMPTY, SPACE_CHAR,
                    PAWN, 'P',
                    KNIGHT, 'N',
                    BISHOP, 'B',
                    ROOK, 'R',
                    QUEEN, 'Q',
                    KING, 'K'
            )
    );

    public static Map<Character, Integer> SYMBOL_PEICE = new HashMap<>(
            Map.of(
                    SPACE_CHAR, EMPTY,
                    'P', PAWN,
                    'N', KNIGHT,
                    'B', BISHOP,
                    'R', ROOK,
                    'Q', QUEEN,
                    'K', KING
            )
    );

    public static boolean IsColor(int peice, int color){
        return (peice & 0b11_000) == color;
    }


    public static boolean isType(int peice, int type){
        return (peice & 0b00_111) == type;
    }
}
