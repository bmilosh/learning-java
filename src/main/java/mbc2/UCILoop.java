package mbc2;

import java.util.Scanner;

public class UCILoop {
    // private Config config;
    // private BoardState boardState;
    // private MoveGenerator moveGen;
    // private MoveUtils moveUtils;
    // private Parsers parsers;
    private static String ENGINE_INFO = "id name MBC2\nid author Mike Bams Chess\nuciok";

    // public UCILoop(Parsers parsers) {
    //     this.parsers = parsers;
    // }
    // public UciLoop(Config Config, BoardState boardState, MoveGenerator moveGen, MoveUtils moveUtils) {
    //     this.config = Config;
    //     this.boardState = boardState;
    //     this.moveGen = moveGen;
    //     this.moveUtils = moveUtils;
    // }

    public static void main(String[] args) {
        Config config = new Config();
        EngineInitMethods.initAll();
        MoveUtils moveUtils = new MoveUtils(config);
        MoveGenerator moveGenerator = new MoveGenerator(moveUtils, config);
        BoardState boardState = new BoardState(config);
        Parsers parsers = new Parsers(config, boardState, moveGenerator, moveUtils);
        Evaluator evaluator = new Evaluator(config, moveGenerator, moveUtils);

        run(parsers, evaluator, config);
    }

    public static void run(Parsers parsers, Evaluator evaluator, Config config) {
        // init scanner
        Scanner uciScanner = new Scanner(System.in);
        // Start the main loop
        while (true) {
            String command = uciScanner.nextLine();
            String[] commandList = command.split(" ");

            // if (commandList[0].toLowerCase().equals("isready")) System.out.println("readyok");
            switch (commandList[0].toLowerCase()) {
                case "isready":
                    System.out.println("readyok");
                    break;
                case "position":
                    parsers.parsePosition(command);
                    break;
                case "go":
                    parsers.parseGo(command);
                    break;
                case "ucinewgame":
                    parsers.parsePosition("position startpos");
                    config.HASH_TABLE.clear();
                    break;
                case "uci":
                    System.out.println(ENGINE_INFO);
                    break;
                case "quit":
                    uciScanner.close();
                    return;
                case "evaluate":
                    System.out.println("Evaluation is: " + evaluator.evaluatePosition());
                    break;
            
                // default: 
                //     uciScanner.close();
                //     throw new IllegalArgumentException("Invalid UCI command passed: " + command);
            }
        }
    }

}
