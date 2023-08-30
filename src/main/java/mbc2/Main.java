package mbc2;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        EngineInitMethods.initAll();
        ZobristHashing zBH = new ZobristHashing(config);
        MoveUtils MoveUtils = new MoveUtils(config);
        MoveGenerator MoveGenerator = new MoveGenerator(MoveUtils, config);
        // Perft perft = new Perft(config, MoveUtils, MoveGenerator);
        
        BoardState boardState = new BoardState(config);
        Parsers parsers = new Parsers(config, boardState, MoveGenerator, MoveUtils);
        // Evaluator evaluator = new Evaluator(config, MoveGenerator, MoveUtils);
        parsers.parseFEN(Config.TRICKY_POSITION);
        PrintUtils.printBoard(config);
        System.out.println("hash key: 0x" + Long.toHexString(zBH.generateHashKey()));

        // // int move = parsers.parseMove("e2a6");
        // // MoveUtils.makeMove(move, false);
        // // move = parsers.parseMove("c7c5");
        // // MoveUtils.makeMove(move, false);
        // // move = parsers.parseMove("d5c6");
        // // MoveUtils.makeMove(move, false);

        // PrintUtils.printBoard(config);
        // // long s = TimeUtility.getTimeMs();
        // evaluator.ORIGINAL_DEPTH = 7;
        // evaluator.searchPosition(evaluator.ORIGINAL_DEPTH);
        // // System.out.println("Time taken: " + (TimeUtility.getTimeMs() - s) + " ms");
        // // perft.perftTest(3);
    }
}