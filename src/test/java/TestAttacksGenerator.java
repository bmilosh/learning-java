import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mbc2.AttacksGenerator;
import mbc2.Config;
import mbc2.EngineInitMethods;

public class TestAttacksGenerator {
    // private static Config Config;
    @BeforeEach
    void setup() {
        // Config = new Config();
        // EngineInitMethods EngineInitMethods = new EngineInitMethods(Config);
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
    @Test
    void testRookAttacksOnTheFly() {
        assertEquals(1157443718891966464L, AttacksGenerator.getRookAttacksOnTheFly(Config.BOARDSQUARES.get("e4"), 8589938688L));
        assertEquals(33686141L, AttacksGenerator.getRookAttacksOnTheFly(Config.BOARDSQUARES.get("b8"), 34078784L));
    }
    @Test
    void testBishopAttacksOnTheFly() {
        assertEquals(9139695683588L, AttacksGenerator.getBishopAttacksOnTheFly(Config.BOARDSQUARES.get("f5"), 9070970961920L));
        assertEquals(562949953421312L, AttacksGenerator.getBishopAttacksOnTheFly(Config.BOARDSQUARES.get("a1"), 563224831361024L));
    }
    @Test
    void testQueenAttacksOnTheFly() {
        long expected = 0b1000000010000000100100001010000011000000011111001100000000100000L;
        long bitboard = 0b1000000000000000000100000000000000000000000001001000000000100000L;
        assertEquals(expected, AttacksGenerator.getQueenAttacksOnTheFly(Config.BOARDSQUARES.get("h6"), bitboard));
        assertEquals(292663135995L, AttacksGenerator.getQueenAttacksOnTheFly(Config.BOARDSQUARES.get("c8"), 1095216660480L));
    }
    @Test
    void testRookAttacks() {
        assertEquals(1157443718891966464L, AttacksGenerator.getRookAttacks(Config.BOARDSQUARES.get("e4"), 8589938688L));
        assertEquals(33686141L, AttacksGenerator.getRookAttacks(Config.BOARDSQUARES.get("b8"), 34078784L));
    }
    @Test
    void testBishopAttacks() {
        assertEquals(9139695683588L, AttacksGenerator.getBishopAttacks(Config.BOARDSQUARES.get("f5"), 9070970961920L));
        assertEquals(562949953421312L, AttacksGenerator.getBishopAttacks(Config.BOARDSQUARES.get("a1"), 563224831361024L));
    }
    @Test
    void testQueenAttacks() {
        long expected = 0b1000000010000000100100001010000011000000011111001100000000100000L;
        long bitboard = 0b1000000000000000000100000000000000000000000001001000000000100000L;
        assertEquals(expected, AttacksGenerator.getQueenAttacks(Config.BOARDSQUARES.get("h6"), bitboard));
        assertEquals(292663135995L, AttacksGenerator.getQueenAttacks(Config.BOARDSQUARES.get("c8"), 1095216660480L));
    }

}
