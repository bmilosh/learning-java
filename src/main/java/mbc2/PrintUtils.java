package mbc2;

public class PrintUtils {
    public static void printBitBoard(long bitboard) {
        System.out.println();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (file == 0) {
                    System.out.printf("%d   ", 8 - rank);
                }
                int square = rank * 8 + file;
                long value = (BitBoard.getBitAtIndex(bitboard, square) != 0) ? 1 : 0;
                if (file < 7) {
                    System.out.printf(" %d ", value);
                } else {
                    System.out.printf(" %d \n", value);
                }
            }
        }
        System.out.println("\n     a  b  c  d  e  f  g  h\n");
        System.out.println("        Bitboard: " + bitboard);
    }
}
