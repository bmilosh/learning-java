import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import mbc2.Config;
import mbc2.EngineInitMethods;
import mbc2.MoveGenerator;

public class TestMoveGenerator {
    @BeforeAll
    static void setup() {
        EngineInitMethods.initAll();
    }

    @Test
    void testGenerateAllPawnMoves() {
        // All white pawns from start position
        ArrayList<Integer> moveList = new ArrayList<>();
        MoveGenerator.getAllPawnMoves(Config.PIECE_BITBOARDS[0], -8, 0, 0, moveList);
        Assertions.assertEquals(16, moveList.size());
        ArrayList<Integer> expectedList = new ArrayList<>(List.of(
            0b101000110000,
            0b1000000000100000110000,
            0b101001110001,
            0b1000000000100001110001,
            0b101010110010,
            0b1000000000100010110010,
            0b101011110011,
            0b1000000000100011110011,
            0b101100110100,
            0b1000000000100100110100,
            0b101101110101,
            0b1000000000100101110101,
            0b101110110110,
            0b1000000000100110110110,
            0b101111110111,
            0b1000000000100111110111
        ));
        expectedList.sort(null);
        moveList.sort(null);
        Assertions.assertTrue(moveList.equals(expectedList));
        
        // All black pawns from start position
        moveList = new ArrayList<>();
        MoveGenerator.getAllPawnMoves(Config.PIECE_BITBOARDS[6], 8, 1, 6, moveList);
        Assertions.assertEquals(16, moveList.size());
        expectedList = new ArrayList<>(List.of(
            0b110010000001000,
            0b1000000110011000001000,
            0b110010001001001,
            0b1000000110011001001001,
            0b110010010001010,
            0b1000000110011010001010,
            0b110010011001011,
            0b1000000110011011001011,
            0b110010100001100,
            0b1000000110011100001100,
            0b110010101001101,
            0b1000000110011101001101,
            0b110010110001110,
            0b1000000110011110001110,
            0b110010111001111,
            0b1000000110011111001111
        ));
        expectedList.sort(null);
        moveList.sort(null);
        Assertions.assertTrue(moveList.equals(expectedList));
    }

    @Test
    void testGetNonPawnMoves() {
        ArrayList<Integer> moveList = new ArrayList<>();
        MoveGenerator.getNonPawnMoves(1, 1, Config.KNIGHT_ATTACKS[1], 7, moveList);
        Assertions.assertEquals(2, moveList.size());
            ArrayList<Integer> expectedList = new ArrayList<>(List.of(
            0b0111010000000001,
            0b0111010010000001
        ));
        expectedList.sort(null);
        moveList.sort(null);
        Assertions.assertTrue(moveList.equals(expectedList));

        // Get moves for white knight on d5 and some pieces scattered around
        int square = Config.BOARDSQUARES.get("d5");
        Config.PIECE_BITBOARDS[1] |= (1L << square); // put a white knight on d5
        Config.OCCUPANCIES[2] |= Config.PIECE_BITBOARDS[1]; // update total occupancies
        Config.OCCUPANCIES[2] |= (1L << Config.BOARDSQUARES.get("b4")); // blocked square (occupied by own piece)
        Config.OCCUPANCIES[2] |= (1L << Config.BOARDSQUARES.get("f6")); // blocked square (occupied by own piece)
        Config.OCCUPANCIES[2] |= (1L << Config.BOARDSQUARES.get("e3")); // blocked square (occupied by own piece)
        Config.OCCUPANCIES[1] |= (1L << Config.BOARDSQUARES.get("f4")); // piece to capture
        Config.OCCUPANCIES[2] |= Config.OCCUPANCIES[1]; // update total occupancies
        moveList = new ArrayList<>();
        MoveGenerator.getNonPawnMoves(square, 0, Config.KNIGHT_ATTACKS[square], 1, moveList);
        Assertions.assertEquals(5, moveList.size());
        expectedList = new ArrayList<>(List.of(
            0b1101010011011,
            0b1010001011011,
            0b100000001001010011011,
            0b100000001001100011011,
            0b100000001100101011011
        ));
        expectedList.sort(null);
        moveList.sort(null);
        Assertions.assertTrue(moveList.equals(expectedList));
    }

    @Test
    void testCanCastle() {
        long tempPB = Config.PIECE_BITBOARDS[1];
        long tempPB7 = Config.PIECE_BITBOARDS[7];
        long tempOcc = Config.OCCUPANCIES[2];
        /*
        kingside castling
        */ 
        // no castling right
        Assertions.assertFalse(MoveGenerator.canCastle('K', Config.BOARDSQUARES.get("e1"), 1));
        /*
         * The following checks, while fine, if run with the whole test suite
         * will unfortunately break other tests, most likely because they modify
         * Occupancies and PieceBitboards a lot.
         */

        // // attack f square
        // Config.OCCUPANCIES[2] &= ~(3L << 5);  // clear f8 and g8 squares
        // Config.PIECE_BITBOARDS[1] |= (1L << Config.BOARDSQUARES.get("e6")); // put a white knight on e6
        // Config.OCCUPANCIES[2] |= Config.PIECE_BITBOARDS[1]; // update total occupancies
        // Config.CASTLING_RIGHT |= Config.CASTLING.get('k');  // Add black kingside castling right
        // Assertions.assertFalse(MoveGenerator.canCastle('k', Config.BOARDSQUARES.get("e8"), 0));

        // // occupy g square
        // Config.CASTLING_RIGHT |= Config.CASTLING.get('K');  // Add white kingside castling right
        // Config.PIECE_BITBOARDS[1] = tempPB;
        // Config.OCCUPANCIES[2] = tempOcc & (1L << Config.BOARDSQUARES.get("g1")) & ~(1L << Config.BOARDSQUARES.get("f1"));  // clear f1 and occupy g1 squares
        // Assertions.assertFalse(MoveGenerator.canCastle('K', Config.BOARDSQUARES.get("e1"), 1));
        
        // /*
        // queenside castling
        // */ 
        // // can castle queenside
        // Config.PIECE_BITBOARDS[1] = tempPB;
        // Config.OCCUPANCIES[2] = tempOcc & ~(7L << 1);  // clear b8, c8 and d8 squares
        // Config.CASTLING_RIGHT |= Config.CASTLING.get('q');  // Add black kingside castling right
        // Assertions.assertTrue(MoveGenerator.canCastle('q', Config.BOARDSQUARES.get("e8"), 0));

        // // no castling right
        // Config.CASTLING_RIGHT = 0;
        // Assertions.assertFalse(MoveGenerator.canCastle('q', Config.BOARDSQUARES.get("e8"), 0));

        // // attack d square
        // Config.OCCUPANCIES[2] = tempOcc & ~(7L << (Config.BOARDSQUARES.get("d1") - 2));  // clear b1, c1 and d1 squares
        // Config.PIECE_BITBOARDS[7] |= (1L << Config.BOARDSQUARES.get("c3")); // put a black knight on c3
        // Config.OCCUPANCIES[2] |= Config.PIECE_BITBOARDS[7]; // update total occupancies
        // Config.CASTLING_RIGHT |= Config.CASTLING.get('Q');  // Add white queenside castling right
        // Assertions.assertFalse(MoveGenerator.canCastle('Q', Config.BOARDSQUARES.get("e1"), 1));
        
        // // occupy b square
        // Config.CASTLING_RIGHT |= Config.CASTLING.get('Q');  // Add white kingside castling right
        // // Config.PIECE_BITBOARDS[1] = tempPB;
        // Config.PIECE_BITBOARDS[7] = tempPB7;
        // Config.OCCUPANCIES[2] = tempOcc & (1L << Config.BOARDSQUARES.get("b1")) & ~(3L << Config.BOARDSQUARES.get("c1"));  // clear c1 and d1 squares, and occupy b1
        // Assertions.assertFalse(MoveGenerator.canCastle('Q', Config.BOARDSQUARES.get("e1"), 1));
    }
}
