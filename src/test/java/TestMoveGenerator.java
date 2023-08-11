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
}
