package mbc2;

public class BitBoard {
    /*
     * Provides some of the bit manipulation methods we will be
     * needing in the engine. For the other methods, we will
     * rely on the builtins: 
     *      Long.bitCount() gives us the number of set bits in the bitboard
     *      Long.lowestOneBit() gives us 2**index, where 'index' is the index of the least significant bit
     */
    public static long setBitAtIndex(long bitboard, int square) {
        return bitboard | (1L << square);
    }

    public static int getBitAtIndex(long bitboard, int index) {
        return (bitboard & (1L << index)) != 0L ? 1 : 0;
    }

    // We might not need this anymore
    public static long popBitAtIndex(long bitboard, int index) {
        return bitboard & ~(1L << index);
    }

    public static int getLSBIndex(long bitboard) {
        if (bitboard == 0) return -1;
        return Long.numberOfTrailingZeros(bitboard);
        // Old method
        // return Long.toBinaryString(bitboard & -bitboard).length() - 1;
    }
}
