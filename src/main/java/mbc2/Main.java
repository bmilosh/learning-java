package mbc2;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        EngineInitMethods.initAll();
        MoveUtils MoveUtils = new MoveUtils(config);
        MoveGenerator MoveGenerator = new MoveGenerator(MoveUtils, config);
        // Perft perft = new Perft(config, MoveUtils, MoveGenerator);
        
        BoardState boardState = new BoardState(config);
        Parsers parsers = new Parsers(config, boardState, MoveGenerator, MoveUtils);
        // PrintUtils.printBoard(config);
        Evaluator evaluator = new Evaluator(config, MoveGenerator, MoveUtils);
        parsers.parseFEN(Config.TRICKY_POSITION);

        // int move = parsers.parseMove("e2a6");
        // MoveUtils.makeMove(move, false);
        // move = parsers.parseMove("c7c5");
        // MoveUtils.makeMove(move, false);
        // move = parsers.parseMove("d5c6");
        // MoveUtils.makeMove(move, false);

        PrintUtils.printBoard(config);
        // long s = TimeUtility.getTimeMs();
        config.ORIGINAL_DEPTH = 6;
        evaluator.searchPosition(config.ORIGINAL_DEPTH);
        // System.out.println("Time taken: " + (TimeUtility.getTimeMs() - s) + " ms");
        // perft.perftTest(3);
    }
}