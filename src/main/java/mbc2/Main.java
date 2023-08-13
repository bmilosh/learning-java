package mbc2;

public class Main {

    public static void main(String[] args) {
        // PrintUtils.printBitBoard(0b111111101111111011111110111111101111111011111110111111101111111L);
        // PrintUtils.printBitBoard(Config.OCCUPANCIES[2]);
        EngineInitMethods.initAll();
        // BoardState boardState = new BoardState();

        // long startTime = TimeUtility.getTimeMs();

        Perft.perftTest(1);
        // Perft.perftDriver(4);

        // System.out.printf("Time taken is: %dms\n", (TimeUtility.getTimeMs() - startTime));
        // System.out.printf("Nodes checked: %d\n", Config.LEAF_NODES);

        // Scanner myScanner = new Scanner(System.in);

        // ArrayList<Integer> moveList = MoveGenerator.generateMoves();
        // System.out.println("***********We have " + moveList.size() + " moves to make.**************");
        // for (int move : moveList) {
        //     System.out.println(move);
        //     // copy board state
        //     boardState.copyBoardState();
        //     // make move
        //     if (!MoveUtils.makeMove(move, false)) {
        //         // skip move
        //         continue;
        //     }
        //     PrintUtils.printBitBoard(Config.OCCUPANCIES[2]);
            
        //     myScanner.nextLine();
        //     // restore board state
        //     boardState.restoreBoardState();
        //     myScanner.nextLine();
        // }
        // myScanner.close();
    }
}