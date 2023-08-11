package mbc2;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // PrintUtils.printBitBoard(0b111111101111111011111110111111101111111011111110111111101111111L);
        // PrintUtils.printBitBoard(Config.OCCUPANCIES[2]);
        EngineInitMethods.initAll();

        // ArrayList<Integer> list1 = new ArrayList<>(List.of(3, 4,5));
        // ArrayList<Integer> list2 = new ArrayList<>(List.of(4, 3,5));
        // System.out.println(list1.equals(list2));
        // list1.sort(null);
        // list2.sort(null);
        // System.out.println(list1.equals(list2));
        ArrayList<Integer> list = new ArrayList<>();
        // MoveGenerator.getNonCapturePawnMoves(Config.BOARDSQUARES.get("a2"), -8, 0, 0, list);
        // System.out.println(list.size());
        // list.forEach((x) -> System.out.println(Long.toBinaryString(x)));
        // MoveGenerator.getAllPawnMoves(Config.PIECE_BITBOARDS[0], -8, 0, 0, list);
        MoveGenerator.getAllPawnMoves(Config.PIECE_BITBOARDS[6], 8, 1, 6, list);
        System.out.println(list.size());
        ArrayList<Integer> expectedList = new ArrayList<>(List.of(
            0b10000001000,
            0b1000000000011000001000,
            0b10001001001,
            0b1000000000011001001001,
            0b10010001010,
            0b1000000000011010001010,
            0b10011001011,
            0b1000000000011011001011,
            0b10100001100,
            0b1000000000011100001100,
            0b10101001101,
            0b1000000000011101001101,
            0b10110001110,
            0b1000000000011110001110,
            0b10111001111,
            0b1000000000011111001111
        ));
        expectedList.sort(null);
        list.sort(null);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(Long.toBinaryString(list.get(i)) + " : " + Long.toBinaryString(expectedList.get(i)));
        }
        // list.forEach((x) -> System.out.println(Long.toBinaryString(x)));
    }
}