package mbc2;

public class EngineInitMethods {
    private static void initBoardSquares() {
        for (int idx = 0; idx < Config.SQUARES.length; idx++) {
            Config.BOARDSQUARES.put(Config.SQUARES[idx], idx);
        }
    }

    private static void initPieces() {
        for (int idx = 0; idx < 12; idx++) {
            Config.PIECES.put(Config.ASCII_PIECES[idx], idx);
        }
    }

    private static void initColours() {
        for (int idx = 0; idx < 2; idx++) {
            Config.COLOURS.put(Config.SIDES[idx], idx);
        }
    }

    private static void initCastling() {
        Config.CASTLING.put('q', 1);
        Config.CASTLING.put('k', 2);
        Config.CASTLING.put('Q', 4);
        Config.CASTLING.put('K', 8);
    }

    public static void initAll() {
        initBoardSquares();
        initPieces();
        initColours();
        initCastling();
    }

}
