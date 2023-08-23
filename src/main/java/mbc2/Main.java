package mbc2;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        EngineInitMethods.initAll();
        MoveUtils MoveUtils = new MoveUtils(config);
        MoveGenerator MoveGenerator = new MoveGenerator(MoveUtils, config);
        // Perft Perft = new Perft(config, MoveUtils, MoveGenerator);
        
        BoardState boardState = new BoardState(config);
        Parsers parsers = new Parsers(config, boardState, MoveGenerator, MoveUtils);
        // PrintUtils.printBoard(config);
        Evaluator evaluator = new Evaluator(config, MoveGenerator, MoveUtils);
        PrintUtils.printBoard(config);
        evaluator.searchPosition(2);
    }
}