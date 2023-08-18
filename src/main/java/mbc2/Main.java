package mbc2;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        // EngineInitMethods.initAll();
        // MoveUtils MoveUtils = new MoveUtils(config);
        // MoveGenerator MoveGenerator = new MoveGenerator(MoveUtils, config);
        // Perft Perft = new Perft(config, MoveUtils, MoveGenerator);
        
        BoardState boardState = new BoardState(config);
        Parsers parsers = new Parsers(config, boardState);
        parsers.parseFEN(Config.START_POSITION);
        // parsers.parseFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 b - - 0 10");

        PrintUtils.printBoard(config);
        // System.out.println(parsers.parseMove("c5f2"));

        // int move = MoveCoder.encodeMove(
        //     Config.BOARDSQUARES.get("g2"), 
        //     Config.BOARDSQUARES.get("g3"), 
        //     Config.PIECES.get('P'), 
        //     0, 0, 0, 0, 0);
        // boolean res = MoveUtils.makeMove(move, false);

    }
}