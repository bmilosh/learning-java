import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import mbc2.Config;
import mbc2.EngineInitMethods;

public class TestEngineInitMethods {
    @BeforeAll
    static void setup() {
        EngineInitMethods.initAll();
    }
    @Test
    void testInitBoardSquares() {
        assertEquals(0, Config.BOARDSQUARES.get("a8"));
        assertEquals(7, Config.BOARDSQUARES.get("h8"));
        assertEquals(44, Config.BOARDSQUARES.get("e3"));
        assertEquals(64, Config.BOARDSQUARES.get("no_square"));
    }
    @Test
    void testInitPieces() {
        assertEquals(0, Config.PIECES.get('P'));
        assertEquals(11, Config.PIECES.get('k'));
    }
    @Test
    void testInitColours() {
        assertEquals(0, Config.COLOURS.get('w'));
        assertEquals(1, Config.COLOURS.get('b'));
    }
    @Test
    void testInitCastling() {
        assertEquals(8, Config.CASTLING.get('K'));
        assertEquals(1, Config.CASTLING.get('q'));
    }
    @Test
    void testInitLeaperPiecesAttacks() {
        // Test Pawn Attacks
        assertEquals(0, Config.PAWN_ATTACKS[Config.COLOURS.get('w')][2]);
        assertEquals(5, Config.PAWN_ATTACKS[Config.COLOURS.get('w')][9]);
        assertEquals(-6917529027641081856L, Config.PAWN_ATTACKS[Config.COLOURS.get('b')][54]);
        assertEquals(0b10000000000000000000000L, Config.PAWN_ATTACKS[Config.COLOURS.get('b')][15]);
        // Test Knight Attacks
        assertEquals(0b1010000100010000000000010001000010100000000000000000000L, Config.KNIGHT_ATTACKS[37]);
        assertEquals(0b10000000010000000000000L, Config.KNIGHT_ATTACKS[7]);
        // Test King Attacks
        assertEquals(0b1000000011000000000000000000000000000000000000000000000000L, Config.KING_ATTACKS[56]);
        assertEquals(0b111000001010000011100000000000000000000L, Config.KING_ATTACKS[Config.BOARDSQUARES.get("f5")]);
    }
}
