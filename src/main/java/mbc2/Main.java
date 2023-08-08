package mbc2;

public class Main {

    public static void main(String[] args) {
        // PrintUtils.printBitBoard(0b111111101111111011111110111111101111111011111110111111101111111L);
        // PrintUtils.printBitBoard(Config.OCCUPANCIES[2]);
        // EngineInitMethods.initAll();
        // PrintUtils.printBitBoard(Config.KNIGHT_ATTACKS[37]);
        long attackMask = 1L << 63;
        PrintUtils.printBitBoard(attackMask);
        System.out.println(Long.lowestOneBit(attackMask));
    }
}