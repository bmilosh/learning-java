package mbc2;

public class Main {

    public static void main(String[] args) {
        // PrintUtils.printBitBoard(0b111111101111111011111110111111101111111011111110111111101111111L);
        // PrintUtils.printBitBoard(Config.OCCUPANCIES[2]);
        // EngineInitMethods.initAll();
        // for (int sq = 0; sq < 64; sq++) {
        //     long magic = MagicNumberGenerator.generateMagicNumber(sq, Config.ROOK_RELEVANCY_OCC_COUNT[sq], false);
        //     System.out.println("0x" + Long.toHexString(magic));
        // }
        // System.out.println("\n\n\n\n");
        for (int sq = 0; sq < 64; sq++) {
            long magic = MagicNumberGenerator.generateMagicNumber(sq, Config.BISHOP_RELEVANCY_OCC_COUNT[sq], true);
            System.out.println("0x" + Long.toHexString(magic));
        }
    }
}