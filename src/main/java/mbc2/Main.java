package mbc2;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // PrintUtils.printBitBoard(0b111111101111111011111110111111101111111011111110111111101111111L);
        // PrintUtils.printBitBoard(Config.OCCUPANCIES[2]);
        EngineInitMethods.initAll();

        ArrayList<Integer> list = new ArrayList<>();
        MoveGenerator.getNonCapturePawnMoves(Config.BOARDSQUARES.get("a2"), -8, 0, 0, list);
        System.out.println(list.size());
        list.forEach((x) -> System.out.println(Long.toBinaryString(x)));
    }
}