import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import mbc2.AttacksGenerator;
import mbc2.Config;
import mbc2.EngineInitMethods;

public class TestAttacksGenerator {
    @BeforeAll
    public static void setup() {
        EngineInitMethods.initAll();
    }
    @Test
    void testGeneratePawnAttacks() {
        assertEquals(0L, AttacksGenerator.generatePawnAttacks('w', Config.BOARDSQUARES.get("h8")));
        assertEquals(4611686018427387904L, AttacksGenerator.generatePawnAttacks('b', Config.BOARDSQUARES.get("h2")));
        assertEquals(720575940379279360L, AttacksGenerator.generatePawnAttacks('b', Config.BOARDSQUARES.get("c2")));
        assertEquals(10995116277760L, AttacksGenerator.generatePawnAttacks('w', Config.BOARDSQUARES.get("c2")));
        assertEquals(2621440L, AttacksGenerator.generatePawnAttacks('w', Config.BOARDSQUARES.get("e5")));
    }
    @Test
    void testGenerateKingAttacks() {
        assertEquals(241192927232L, AttacksGenerator.generateKingAttacks(Config.BOARDSQUARES.get("e5")));
        assertEquals(49216L, AttacksGenerator.generateKingAttacks(Config.BOARDSQUARES.get("h8")));
        assertEquals(0b101000001110000000000000000000000000000000000000000000000000000L, AttacksGenerator.generateKingAttacks(Config.BOARDSQUARES.get("f1")));
        assertEquals(197123L, AttacksGenerator.generateKingAttacks(Config.BOARDSQUARES.get("a7")));
    }
    @Test
    void testGenerateKnightAttacks() {
        assertEquals(11333767002587136L, AttacksGenerator.generateKnightAttacks(Config.BOARDSQUARES.get("e4")));
        assertEquals(43234889994L, AttacksGenerator.generateKnightAttacks(Config.BOARDSQUARES.get("c6")));
        assertEquals(0b1010000000010000000000000001000010100000000000000000000000000000L, AttacksGenerator.generateKnightAttacks(Config.BOARDSQUARES.get("g3")));
        assertEquals(33816580L, AttacksGenerator.generateKnightAttacks(Config.BOARDSQUARES.get("a7")));
    }
    @Test
    void testGenerateRookAttacks() {
        assertEquals(9115426935197958144L, AttacksGenerator.generateRookAttacks(Config.BOARDSQUARES.get("h1")));
        assertEquals(8505056726876686336L, AttacksGenerator.generateRookAttacks(Config.BOARDSQUARES.get("d1")));
        assertEquals(4521260802403840L, AttacksGenerator.generateRookAttacks(Config.BOARDSQUARES.get("e7")));
        assertEquals(565157600297596L, AttacksGenerator.generateRookAttacks(Config.BOARDSQUARES.get("b8")));
    }
    @Test
    void testGenerateBishopAttacks() {
        assertEquals(22526811443298304L, AttacksGenerator.generateBishopAttacks(Config.BOARDSQUARES.get("f1")));
        assertEquals(275449643008L, AttacksGenerator.generateBishopAttacks(Config.BOARDSQUARES.get("d7")));
        assertEquals(4514594912477184L, AttacksGenerator.generateBishopAttacks(Config.BOARDSQUARES.get("c4")));
        assertEquals(18049651735527936L, AttacksGenerator.generateBishopAttacks(Config.BOARDSQUARES.get("a8")));
    }
}
