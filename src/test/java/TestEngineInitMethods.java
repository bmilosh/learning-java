import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mbc2.AttacksGenerator;
import mbc2.Config;
import mbc2.EngineInitMethods;
import mbc2.OccupancySetter;

public class TestEngineInitMethods {
    // private static Config Config;
    @BeforeEach
    void setup() {
        // Config = new Config();
        // EngineInitMethods EngineInitMethods = new EngineInitMethods(Config);
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
        assertEquals(0, AttacksGenerator.PAWN_ATTACKS[Config.COLOURS.get('w')][2]);
        assertEquals(5, AttacksGenerator.PAWN_ATTACKS[Config.COLOURS.get('w')][9]);
        assertEquals(-6917529027641081856L, AttacksGenerator.PAWN_ATTACKS[Config.COLOURS.get('b')][54]);
        assertEquals(0b10000000000000000000000L, AttacksGenerator.PAWN_ATTACKS[Config.COLOURS.get('b')][15]);
        // Test Knight Attacks
        assertEquals(0b1010000100010000000000010001000010100000000000000000000L, AttacksGenerator.KNIGHT_ATTACKS[37]);
        assertEquals(0b10000000010000000000000L, AttacksGenerator.KNIGHT_ATTACKS[7]);
        // Test King Attacks
        assertEquals(0b1000000011000000000000000000000000000000000000000000000000L, AttacksGenerator.KING_ATTACKS[56]);
        assertEquals(0b111000001010000011100000000000000000000L, AttacksGenerator.KING_ATTACKS[Config.BOARDSQUARES.get("f5")]);
    }
    @Test
    void testInitSliderPiecesAttacks() {
        /*
         * 1. Get attack mask by doing generatePieceAttack(square)
         * 2. Get magic number: Config.piece_magic_number[square]
         * 3. Set relevant_bit_count to be number of set bits in attack mask
         * 4. Set occupancy_index to be 1L << relevant_bit_count
         * 5. Pick index in range(occupancy_index) 
         * 6. Set occupancy to be OccupancySetter(index, relevant_bit_count, attack_mask)
         * 7. Generate magic_index: (int) ((((occupancy * magicCandidate) & 0xFFFFFFFFFFFFFFFFL) >>> (64 - numberOfSetBits)) & 0xFFFFFFFFL)
         * 8. Attack for the piece on that square and with that magic_index should be piece_attack_on_the_fly(square, occupancy)
         */
        
        // Test Rook Attacks
        int square = Config.BOARDSQUARES.get("f5");
        long attackMask = AttacksGenerator.generateRookAttacks(square);
        long magicNumber = AttacksGenerator.ROOK_MAGIC_NUMBERS[square];
        int numberOfSetBits = Long.bitCount(attackMask);
        int index = 33;
        long occupancy = OccupancySetter.run(index, numberOfSetBits, attackMask);
        int magicIndex = (int) ((((occupancy * magicNumber) & 0xFFFFFFFFFFFFFFFFL) >>> (64 - numberOfSetBits)) & 0xFFFFFFFFL);
        long expected = AttacksGenerator.getRookAttacksOnTheFly(square, occupancy);
        assertEquals(expected, AttacksGenerator.ROOK_ATTACKS[square][magicIndex]);

        index = 41;
        occupancy = OccupancySetter.run(index, numberOfSetBits, attackMask);
        magicIndex = (int) ((((occupancy * magicNumber) & 0xFFFFFFFFFFFFFFFFL) >>> (64 - numberOfSetBits)) & 0xFFFFFFFFL);
        expected = AttacksGenerator.getRookAttacksOnTheFly(square, occupancy);
        assertEquals(expected, AttacksGenerator.ROOK_ATTACKS[square][magicIndex]);

        // Test Bishop Attacks
        square = Config.BOARDSQUARES.get("b2");
        attackMask = AttacksGenerator.generateBishopAttacks(square);
        magicNumber = AttacksGenerator.BISHOP_MAGIC_NUMBERS[square];
        numberOfSetBits = Long.bitCount(attackMask);
        index = 9;
        occupancy = OccupancySetter.run(index, numberOfSetBits, attackMask);
        magicIndex = (int) ((((occupancy * magicNumber) & 0xFFFFFFFFFFFFFFFFL) >>> (64 - numberOfSetBits)) & 0xFFFFFFFFL);
        expected = AttacksGenerator.getBishopAttacksOnTheFly(square, occupancy);
        assertEquals(expected, AttacksGenerator.BISHOP_ATTACKS[square][magicIndex]);
    }
}
