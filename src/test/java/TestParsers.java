import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mbc2.BoardState;
import mbc2.Config;
import mbc2.EngineInitMethods;
import mbc2.Parsers;

public class TestParsers {

    private static Config config;
    private static Parsers Parsers;
    private static BoardState BoardState;
    @BeforeEach
    void setup() {
        config = new Config();
        BoardState = new BoardState(config);
        Parsers = new Parsers(config, BoardState);
        // EngineInitMethods EngineInitMethods = new EngineInitMethods(Config);
        EngineInitMethods.initAll();
    }

    @Test
    void testParseFEN() {
        // "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 "
        long whitePawns = (7L << 53) | (7L << 48) | (1L << 36) | (1L << 27);
        long whiteKnights = (1L << 42) | (1L << 28);
        long whiteBishops = (3L << 51);
        long whiteRooks = (1L << 56) | (1L << 63);
        long whiteQueen = 1L << 45;
        long whiteKing = 1L << 60;

        long blackPawns = (1L << 8) | (3L << 10) | (1L << 13) | (5L << 20) | (1L << 33) | (1L << 47);
        long blackKnights = (17L << 17);
        long blackBishops = (5L << 14);
        long blackRooks = 1L | (1L << 7);
        long blackQueen = 1L << 12;
        long blackKing = (1L << 4);

        long whitePieces = whitePawns | whiteKnights | whiteBishops | whiteRooks | whiteQueen | whiteKing;
        long blackPieces = blackPawns | blackKnights | blackBishops | blackRooks | blackQueen | blackKing;
        long allPieces = whitePieces | blackPieces;

        int castlingRights = 15;

        Parsers.parseFEN(Config.TRICKY_POSITION);
        
        Assertions.assertEquals(castlingRights, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whiteBishops, config.PIECE_BITBOARDS[2]);
        Assertions.assertEquals(blackPawns, config.PIECE_BITBOARDS[6]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("no_square", config.ENPASSANT_SQUARE);
    }

    @Test
    void testParseFEN2() {
        // "rnbqkb1r/pp1p1pPp/8/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1"
        long whitePawns = (21L << 48) | (17L << 43) | (1L << 33) | (1L << 29) | (1L << 14);
        long whiteKnights = (1L << 62) | (1L << 57);
        long whiteBishops = (9L << 58);
        long whiteRooks = (1L << 56) | (1L << 63);
        long whiteQueen = 1L << 59;
        long whiteKing = 1L << 60;

        long blackPawns = (11L << 8) | (5L << 13) | (5L << 26);
        long blackKnights = (1L << 1);
        long blackBishops = (1L << 2) | (1L << 5);
        long blackRooks = 1L | (1L << 7);
        long blackQueen = 1L << 3;
        long blackKing = (1L << 4);

        long whitePieces = whitePawns | whiteKnights | whiteBishops | whiteRooks | whiteQueen | whiteKing;
        long blackPieces = blackPawns | blackKnights | blackBishops | blackRooks | blackQueen | blackKing;
        long allPieces = whitePieces | blackPieces;

        int castlingRights = 15;

        Parsers.parseFEN(Config.KILLER_POSITION);
        
        Assertions.assertEquals(castlingRights, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whitePawns, config.PIECE_BITBOARDS[0]);
        Assertions.assertEquals(blackRooks, config.PIECE_BITBOARDS[9]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("e6", config.ENPASSANT_SQUARE);
    }

    @Test
    void testParseFEN3() {
        long whitePieces = config.OCCUPANCIES[0];
        long blackPieces = config.OCCUPANCIES[1];
        long allPieces = config.OCCUPANCIES[2];
        long whiteKnights = config.PIECE_BITBOARDS[1];
        long blackPawns = config.PIECE_BITBOARDS[6];
        // "rnbqkb1r/pp1p1pPp/8/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1"

        // Invalid FEN: Contains invalid character 'T'
        Parsers.parseFEN("rnbqkT1r/pp1p1pPp/8/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1");
        
        Assertions.assertEquals(0, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whiteKnights, config.PIECE_BITBOARDS[1]);
        Assertions.assertEquals(blackPawns, config.PIECE_BITBOARDS[6]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("no_square", config.ENPASSANT_SQUARE);

        // Invalid FEN: Squares don't add up to 64
        Parsers.parseFEN("rnbqkb1r/pp1p1pPp/5/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1");
        
        Assertions.assertEquals(0, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whiteKnights, config.PIECE_BITBOARDS[1]);
        Assertions.assertEquals(blackPawns, config.PIECE_BITBOARDS[6]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("no_square", config.ENPASSANT_SQUARE);

        // Invalid FEN: Squares don't add up to 64
        Parsers.parseFEN("rnbqkb1r/pp1p1pPp/9/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1");
        
        Assertions.assertEquals(0, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whiteKnights, config.PIECE_BITBOARDS[1]);
        Assertions.assertEquals(blackPawns, config.PIECE_BITBOARDS[6]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("no_square", config.ENPASSANT_SQUARE);

        // Invalid FEN: Castling rights are incorrect 'KKkq'
        Parsers.parseFEN("rnbqkb1r/pp1p1pPp/9/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KKkq e6 0 1");
        
        Assertions.assertEquals(0, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whiteKnights, config.PIECE_BITBOARDS[1]);
        Assertions.assertEquals(blackPawns, config.PIECE_BITBOARDS[6]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("no_square", config.ENPASSANT_SQUARE);
    }
}
