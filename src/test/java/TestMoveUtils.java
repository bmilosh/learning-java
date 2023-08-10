import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import mbc2.Config;
import mbc2.EngineInitMethods;
import mbc2.MoveUtils;


public class TestMoveUtils {
    @BeforeAll
    static void setup(){
        EngineInitMethods.initAll();
    }
    @Test
    void testIsSquareAttacked() {
        Assertions.assertTrue(MoveUtils.isSquareAttacked(18, 1));
        Assertions.assertTrue(MoveUtils.isSquareAttacked(40, 0));
        Assertions.assertFalse(MoveUtils.isSquareAttacked(32, 1));
        
        Config.PIECE_BITBOARDS[6] |= (1L << 25);
        Assertions.assertTrue(MoveUtils.isSquareAttacked(32, 1));
        Config.PIECE_BITBOARDS[7] |= (1L << 14);
        Assertions.assertTrue(MoveUtils.isSquareAttacked(Config.BOARDSQUARES.get("f5"), 1));
        Assertions.assertFalse(MoveUtils.isSquareAttacked(Config.BOARDSQUARES.get("f5"), 0));
        Config.PIECE_BITBOARDS[6] |= (1L << 23);
        Config.PIECE_BITBOARDS[3] |= (1L << 31);
        Config.OCCUPANCIES[2] |= Config.PIECE_BITBOARDS[6] | Config.PIECE_BITBOARDS[3];
        Assertions.assertFalse(MoveUtils.isSquareAttacked(15, 0));
    }
}
