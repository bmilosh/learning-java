import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import mbc2.BitBoard;

public class TestBitBoard {

    private static long bitboard = 16L;
    private static long bitboard1 = 1073741840L;

    @Test
    void testSetBitAtIndex() {
        long position = BitBoard.setBitAtIndex(bitboard, 3);
        assertEquals(24L, position);

        position = BitBoard.setBitAtIndex(bitboard, 30);
        assertEquals(1073741840L, position);

        position = BitBoard.setBitAtIndex(TestBitBoard.bitboard1, 52);
        assertEquals(4503600701112336L, position);

    }

    @Test
    void testGetBitAtIndex() {        
        long position = BitBoard.getBitAtIndex(TestBitBoard.bitboard1, 52);
        assertEquals(0L, position);

        long position2 = BitBoard.getBitAtIndex(TestBitBoard.bitboard1, 4);
        assertEquals(16, position2);
    }

    @Test
    void testPopBitAtIndex() {        
        long position = BitBoard.popBitAtIndex(TestBitBoard.bitboard1, 4);
        assertEquals(1073741824L, position);

        long position2 = BitBoard.popBitAtIndex(TestBitBoard.bitboard1, 30);
        assertEquals(16, position2);

        long position3 = BitBoard.popBitAtIndex(TestBitBoard.bitboard1, 5);
        assertEquals(TestBitBoard.bitboard1, position3);
    }
}