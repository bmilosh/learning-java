package mbc2;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        EngineInitMethods.initAll();
        // ZobristHashing zBH = new ZobristHashing(config);
        MoveUtils MoveUtils = new MoveUtils(config);
        MoveGenerator MoveGenerator = new MoveGenerator(MoveUtils, config);
        // Perft perft = new Perft(config, MoveUtils, MoveGenerator);
        
        BoardState boardState = new BoardState(config);
        Parsers parsers = new Parsers(config, boardState, MoveGenerator, MoveUtils);
        Evaluator evaluator = new Evaluator(config, MoveGenerator, MoveUtils);
        // parsers.parseFEN("4k3/Q7/8/4K3/8/8/8/8 w - - ");
        parsers.parseFEN(Config.START_POSITION);
        PrintUtils.printBoard(config);
        // // System.out.println("hash key: 0x" + Long.toHexString(zBH.generateHashKey()));

        // int move = parsers.parseMove("e5e6");
        // MoveUtils.makeMove(move, false);
        // move = parsers.parseMove("c7c5");
        // MoveUtils.makeMove(move, false);

        // PrintUtils.printBoard(config);
        // // long s = TimeUtility.getTimeMs();
        evaluator.ORIGINAL_DEPTH = 10;
        evaluator.searchPosition(evaluator.ORIGINAL_DEPTH);
        MoveUtils.makeMove(evaluator.PV_TABLE[0][0], false);
        evaluator.searchPosition(evaluator.ORIGINAL_DEPTH);
        // // System.out.println("Time taken: " + (TimeUtility.getTimeMs() - s) + " ms");
        // // perft.perftTest(3);

        // long n = 1L << 63;
        // long nrl = n >> 8; // returns a negative number; not what we want
        // long nrr = n >>> 8; 
        // System.out.println(n);
        // System.out.println(nrl);
        // System.out.println(nrr);

    }

}