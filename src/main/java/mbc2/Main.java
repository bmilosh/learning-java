package mbc2;

public class Main {

    public static void main(String[] args) {
        // PrintUtils.printBitBoard(0b111111101111111011111110111111101111111011111110111111101111111L);
        // PrintUtils.printBitBoard((1L << 8) | (3L << 10) | (1L << 13) | (5L << 20) | (1L << 33) | (1L << 47));
        // PrintUtils.printBitBoard((1L << 62) | (1L << 59));

        Config config = new Config();
        EngineInitMethods.initAll();
        MoveUtils MoveUtils = new MoveUtils(config);
        MoveGenerator MoveGenerator = new MoveGenerator(MoveUtils, config);
        Perft Perft = new Perft(config, MoveUtils, MoveGenerator);
        
        BoardState boardState = new BoardState(config);
        Parsers parsers = new Parsers(config, boardState);
        parsers.parseFEN(Config.PERFT_POSITION_6);
        // parsers.parseFEN(Config.START_POSITION);
        // parsers.parseFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - ");
        PrintUtils.printBoard(config);

        // int move = MoveCoder.encodeMove(
        //     Config.BOARDSQUARES.get("g2"), 
        //     Config.BOARDSQUARES.get("g3"), 
        //     Config.PIECES.get('P'), 
        //     0, 0, 0, 0, 0);
        // boolean res = MoveUtils.makeMove(move, false);

        // move = MoveCoder.encodeMove(
        //     Config.BOARDSQUARES.get("g4"), 
        //     Config.BOARDSQUARES.get("f3"), 
        //     Config.PIECES.get('b'), 
        //     0, 1, 0, 0, 0);
        // res = MoveUtils.makeMove(move, false);

        // move = MoveCoder.encodeMove(
        //     Config.BOARDSQUARES.get("g1"), 
        //     Config.BOARDSQUARES.get("h1"), 
        //     Config.PIECES.get('K'), 
        //     0, 0, 0, 0, 0);
        // res = MoveUtils.makeMove(move, false);

        // move = MoveCoder.encodeMove(
        //     Config.BOARDSQUARES.get("e8"), 
        //     Config.BOARDSQUARES.get("f8"), 
        //     Config.PIECES.get('k'), 
        //     0, 0, 0, 0, 0);
        // res = MoveUtils.makeMove(move, false);
        // "r4k1r/p1ppqpb1/b3pnp1/3PN3/1pn1P3/2N2Q1p/PPPBBPPP/R4R1K w - - 0 1 "

        // move = MoveCoder.encodeMove(
        //     Config.BOARDSQUARES.get("h1"), 
        //     Config.BOARDSQUARES.get("d1"), 
        //     Config.PIECES.get('K'), 
        //     0, 0, 0, 0, 0);
        // res = MoveUtils.makeMove(move, false);

        // boardState.copyBoardState();
        // Scanner mScanner = new Scanner(System.in);
        // ArrayList<Integer> moveList = MoveGenerator.generateMoves();
        // for (Integer move1 : moveList) {
        //     boardState.copyBoardState();
        //     res  = MoveUtils.makeMove(move1, false);

        //     PrintUtils.printBoard(config);
        //     System.out.println(PrintUtils.moveToString(move1));
            
        //     boardState.restoreBoardState();
        //     mScanner.nextLine();
        // }
        // mScanner.close();
                
        PrintUtils.printBoard(config);
        Perft.perftTest(5);
    }
}