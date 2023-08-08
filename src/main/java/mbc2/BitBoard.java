package mbc2;

public class BitBoard {
    /*
     * Provides some of the bit manipulation methods we will be
     * needing in the engine. For the other methods, we will
     * rely on the builtins: 
     *      Long.bitCount() gives us the number of set bits in the bitboard
     *      Long.lowestOneBit() gives us the index of the least significant bit
     */
    public static long setBitAtIndex(long bitboard, int square) {
        return bitboard | (1L << square);
    }

    public static int getBitAtIndex(long bitboard, int index) {
        return (bitboard & (1L << index)) != 0L ? 1 : 0;
    }

    public static long popBitAtIndex(long bitboard, int index) {
        return bitboard & ~(1L << index);
    }
}
