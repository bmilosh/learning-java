package mbc2;

public class Main {

    public static void main(String[] args) {
        // PrintUtils.printBitBoard(0b111111101111111011111110111111101111111011111110111111101111111L);
        // PrintUtils.printBitBoard(Config.OCCUPANCIES[2]);
        // EngineInitMethods.initAll();
        Main.testCopy(3);
    }

    private static void testCopy(int depth) {
        if (depth == 0) {
            return;
        }
        BoardState bState = new BoardState();
        System.out.println("Before copying:");
        for (int res : Config.numsarr1) {
            System.out.printf("%d -> ", res);
        }
        System.out.println();
        bState.copyBoardState();
        Config.numsarr1[2] = Config.numsarr1[2] * 2;
        System.out.println("After copying:");
        for (int res : Config.numsarr1) {
            System.out.printf("%d -> ", res);
        }
        System.out.println();
        testCopy(depth - 1);
        bState.restoreBoardState();
        System.out.println("After restoring:");
        for (int res : Config.numsarr1) {
            System.out.printf("%d -> ", res);
        }
        System.out.println();
    }
}