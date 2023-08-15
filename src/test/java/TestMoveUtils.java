import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mbc2.Config;
import mbc2.EngineInitMethods;
import mbc2.MoveUtils;


public class TestMoveUtils {
    private static Config config;
    private static MoveUtils MoveUtils;
    @BeforeEach
    void setup() {
        config = new Config();
        MoveUtils = new MoveUtils(config);
        // EngineInitMethods EngineInitMethods = new EngineInitMethods(Config);
        EngineInitMethods.initAll();
    }

    @Test
    void testIsSquareAttacked() {
        Assertions.assertTrue(MoveUtils.isSquareAttacked(18, 1));
        Assertions.assertTrue(MoveUtils.isSquareAttacked(40, 0));
        Assertions.assertFalse(MoveUtils.isSquareAttacked(32, 1));
        
        config.PIECE_BITBOARDS[6] |= (1L << 25);
        Assertions.assertTrue(MoveUtils.isSquareAttacked(32, 1));
        config.PIECE_BITBOARDS[7] |= (1L << 14);
        Assertions.assertTrue(MoveUtils.isSquareAttacked(Config.BOARDSQUARES.get("f5"), 1));
        Assertions.assertFalse(MoveUtils.isSquareAttacked(Config.BOARDSQUARES.get("f5"), 0));
        config.PIECE_BITBOARDS[6] |= (1L << 23);
        config.PIECE_BITBOARDS[3] |= (1L << 31);
        config.OCCUPANCIES[2] |= config.PIECE_BITBOARDS[6] | config.PIECE_BITBOARDS[3];
        Assertions.assertFalse(MoveUtils.isSquareAttacked(15, 0));
    }
    @Test
    void testIsKingUnderCheck() {
        config.PIECE_BITBOARDS[5] |= (1L << 16);
        Assertions.assertTrue(MoveUtils.isKingUnderCheck(16, 1));

        config.PIECE_BITBOARDS[4] |= (1L << Config.BOARDSQUARES.get("g4"));
        config.OCCUPANCIES[2] |= config.PIECE_BITBOARDS[4];
        Assertions.assertTrue(MoveUtils.isKingUnderCheck(Config.BOARDSQUARES.get("e6"), 0));
        Assertions.assertFalse(MoveUtils.isKingUnderCheck(Config.BOARDSQUARES.get("h8"), 0));
    }

    // @Test
    // void testMoveToString() {
    //     int move = 0b110010000001000;
    //     Assertions.assertEquals("a7a6", MoveUtils.moveToString(move));
        
    //     move = 0b10101111111110;
    //     Assertions.assertEquals("g1h3", MoveUtils.moveToString(move));
        
    //     move = 0b101000000000010001011;
    //     Assertions.assertEquals("d7c8q", MoveUtils.moveToString(move));
    // }
}
