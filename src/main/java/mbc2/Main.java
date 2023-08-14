package mbc2;

public class Main {

    public static void main(String[] args) {
        // PrintUtils.printBitBoard(0b111111101111111011111110111111101111111011111110111111101111111L);
        // PrintUtils.printBitBoard(Config.OCCUPANCIES[2]);
        EngineInitMethods.initAll();
        // BoardState boardState = new BoardState();

        // long startTime = TimeUtility.getTimeMs();

        // Perft.perftTest(1);
        // Perft.perftDriver(4);
        PrintUtils.printBoard();

    }
}