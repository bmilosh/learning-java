import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mbc2.BoardState;
import mbc2.Config;
import mbc2.EngineInitMethods;
import mbc2.MoveCoder;
import mbc2.MoveGenerator;
import mbc2.MoveUtils;
import mbc2.Parsers;

public class TestParsers {

    private static Config config;
    private static Parsers Parsers;
    private static BoardState BoardState;
    private static MoveUtils moveUtils;
    private static MoveGenerator moveGenerator;
    @BeforeEach
    void setup() {
        config = new Config();
        BoardState = new BoardState(config);
        moveGenerator = new MoveGenerator(moveUtils, config);
        Parsers = new Parsers(config, BoardState, moveGenerator, moveUtils);
        EngineInitMethods.initAll();
    }

    /*
     *  ################################################
        ##                                            ##
        ##                Parse FEN                   ##
        ##                                            ##
        ################################################
     */
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

        // Invalid FEN: Missing items
        // Parsers.parseFEN("rnbqkT1r/pp1p1pPp/8/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR KQkq e6 ");
        // // Invalid FEN: Contains invalid character 'T'
        // Parsers.parseFEN("rnbqkT1r/pp1p1pPp/8/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1");
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Invalid FEN: Missing items
                        Parsers.parseFEN("rnbqkT1r/pp1p1pPp/8/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR KQkq e6 ");
                    }, 
                    "Invalid move: d2d5");
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Invalid FEN: Contains invalid character 'T'
                        Parsers.parseFEN("rnbqkT1r/pp1p1pPp/8/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1");
                    }, 
                    "Invalid move: d2d5");
        Assertions.assertEquals(0, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whiteKnights, config.PIECE_BITBOARDS[1]);
        Assertions.assertEquals(blackPawns, config.PIECE_BITBOARDS[6]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("no_square", config.ENPASSANT_SQUARE);

        // Invalid FEN: Squares don't add up to 64
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Invalid FEN: Squares don't add up to 64
                        Parsers.parseFEN("rnbqkb1r/pp1p1pPp/5/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1");
                    }, 
                    "Invalid move: d2d5");
        
        Assertions.assertEquals(0, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whiteKnights, config.PIECE_BITBOARDS[1]);
        Assertions.assertEquals(blackPawns, config.PIECE_BITBOARDS[6]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("no_square", config.ENPASSANT_SQUARE);

        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Invalid FEN: Squares don't add up to 64
                        Parsers.parseFEN("rnbqkb1r/pp1p1pPp/9/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1");
                    }, 
                    "Invalid move: d2d5");
        
        Assertions.assertEquals(0, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whiteKnights, config.PIECE_BITBOARDS[1]);
        Assertions.assertEquals(blackPawns, config.PIECE_BITBOARDS[6]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("no_square", config.ENPASSANT_SQUARE);

        // Invalid FEN: Castling rights are incorrect 'KKkq'
        // Parsers.parseFEN("rnbqkb1r/pp1p1pPp/9/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KKkq e6 0 1");
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Invalid FEN: Castling rights are incorrect 'KKkq'
                        Parsers.parseFEN("rnbqkb1r/pp1p1pPp/9/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KKkq e6 0 1");
                    }, 
                    "Invalid move: d2d5");
        
        Assertions.assertEquals(0, config.CASTLING_RIGHT);
        Assertions.assertEquals(whitePieces, config.OCCUPANCIES[0]);
        Assertions.assertEquals(blackPieces, config.OCCUPANCIES[1]);
        Assertions.assertEquals(allPieces, config.OCCUPANCIES[2]);
        Assertions.assertEquals(whiteKnights, config.PIECE_BITBOARDS[1]);
        Assertions.assertEquals(blackPawns, config.PIECE_BITBOARDS[6]);
        Assertions.assertEquals('w', config.SIDE_TO_MOVE);
        Assertions.assertEquals("no_square", config.ENPASSANT_SQUARE);
    }

    /*
     *  ################################################
        ##                                            ##
        ##                Parse Moves                 ##
        ##                                            ##
        ################################################
     */
    @Test
    void testParseCorrectMove() {
        int move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("e2"),
            Config.BOARDSQUARES.get("e4"),
            Config.PIECES.get('P'),
            0, 0, 1, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("e2e4"));

        move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("g1"),
            Config.BOARDSQUARES.get("h3"),
            Config.PIECES.get('N'),
            0, 0, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("g1h3"));

        move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("a2"),
            Config.BOARDSQUARES.get("a3"),
            Config.PIECES.get('P'),
            0, 0, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("a2a3"));

    }

    @Test
    void testParseMoveFromCustomPosition() {
        /*
         * Parse move from position different from start position
         */
        // r4rk1/1pp1qppp/p1np1n2/2b1N1B1/2B1P1b1/P1NP4/1PP1QPPP/R4RK1 b - - 0 11
        Parsers.parseFEN("r4rk1/1pp1qppp/p1np1n2/2b1N1B1/2B1P1b1/P1NP4/1PP1QPPP/R4RK1 b - - 0 11");
        int move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("g4"),
            Config.BOARDSQUARES.get("e2"),
            Config.PIECES.get('b'),
            0, 1, 0, 0, 0
        );

        // r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P3/P1NP1b2/1PP1QP1P/R4RK1 w - - 0 10
        Parsers.parseFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P3/P1NP1b2/1PP1QP1P/R4RK1 w - - 0 10");
        move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("e2"),
            Config.BOARDSQUARES.get("f3"),
            Config.PIECES.get('Q'),
            0, 1, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("e2f3"));

        // Test promotions
        Parsers.parseFEN(Config.KILLER_POSITION);
        move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("g7"),
            Config.BOARDSQUARES.get("g8"),
            Config.PIECES.get('P'),
            Config.PIECES.get('B'), 0, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("g7g8b"));
        
        move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("g7"),
            Config.BOARDSQUARES.get("h8"),
            Config.PIECES.get('P'),
            Config.PIECES.get('Q'), 1, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("g7h8q"));
        
        // Test en passant
        move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("f5"),
            Config.BOARDSQUARES.get("e6"),
            Config.PIECES.get('P'),
            0, 1, 0, 1, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("f5e6"));
    }

    @Test
    void testParseIncorrectMovesRightSideInvalidTargetSquare() {
        // Invalid moves should throw IllegalArgumentException
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Right side, invalid square
                        Parsers.parseMove("d2d5");
                    }, 
                    "Invalid move: d2d5");
    }

    @Test
    void testParseIncorrectMovesWrongSide() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Wrong side to move
                        Parsers.parseMove("h7h6");
                    }, 
                    "Invalid move: h7h6");
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Wrong side to move
                        Parsers.parseMove("b8a6");
                    }, 
                    "Invalid move: b8a6");
    }

    @Test
    void testParseIncorrectMoveWrongFormat() {
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Invalid move
                        Parsers.parseMove("gh1");
                    }, 
                    "Invalid move: gh1");
        
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Invalid promotion piece
                        Parsers.parseMove("a7a8u");
                    }, 
                    "Invalid move: a7a8u");
    }

    @Test
    void testParseIncorrectMoveEmptySquare() {        
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Moving from empty square
                        Parsers.parseMove("a5a6");
                    }, 
                    "Invalid move: a5a6");

    }

    @Test
    void testParsePawnMoveCapture() {
        Parsers.parseFEN(Config.TRICKY_POSITION);
        int move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("d5"),
            Config.BOARDSQUARES.get("e6"),
            Config.PIECES.get('P'),
            0, 1, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("d5e6"));
    }

    @Test
    void testParseIncorrectKnightMove() {
        Parsers.parseFEN("rnbqkbnr/ppppp1pp/8/8/8/5p1P/PPPPPPP1/RNBQKBNR w KQkq - 0 1 ");
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Occupied by friendly piece
                        Parsers.parseMove("g1h3");
                    }, 
                    "Invalid move: g1h3");
         Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Not a move a knight can make
                        Parsers.parseMove("b1d3");
                    }, 
                    "Invalid move: b1d3");
   }

    @Test
    void testParseCorrectKnightMove() {
        Parsers.parseFEN("rnbqkbnr/ppppp1pp/8/8/8/5p1P/PPPPPPP1/RNBQKBNR w KQkq - 0 1 ");
        int move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("g1"),
            Config.BOARDSQUARES.get("f3"),
            Config.PIECES.get('N'),
            0, 1, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("g1f3"));

   }

    @Test
    void testParseIncorrectBishopMove() {
        Parsers.parseFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 b - - 0 10");
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Occupied by friendly piece
                        Parsers.parseMove("c5d6");
                    }, 
                    "Invalid move: c5d6");
         Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Not a move a bishop can make
                        Parsers.parseMove("c5a5");
                    }, 
                    "Invalid move: c5a5");
   }

    @Test
    void testParseCorrectBishopMove() {
        Parsers.parseFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 b - - 0 10");
        int move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("c5"),
            Config.BOARDSQUARES.get("f2"),
            Config.PIECES.get('b'),
            0, 1, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("c5f2"));

   }

    @Test
    void testParseIncorrectRookMove() {
        Parsers.parseFEN(Config.PERFT_POSITION_6);
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Occupied by friendly piece
                        Parsers.parseMove("f1f2");
                    }, 
                    "Invalid move: f1f2");
         Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Not a move a rook can make
                        Parsers.parseMove("f1h3");
                    }, 
                    "Invalid move: f1h3");
   }

    @Test
    void testParseCorrectRookMove() {
        Parsers.parseFEN(Config.PERFT_POSITION_6);
        int move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("a1"),
            Config.BOARDSQUARES.get("d1"),
            Config.PIECES.get('R'),
            0, 0, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("a1d1"));
   }

    @Test
    void testParseIncorrectQueenMove() {
        Parsers.parseFEN(Config.PERFT_POSITION_6);
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Occupied by friendly piece
                        Parsers.parseMove("e2c2");
                    }, 
                    "Invalid move: e2c2");
         Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Not a move a queen can make
                        Parsers.parseMove("e2d6");
                    }, 
                    "Invalid move: e2d6");
   }

    @Test
    void testParseCorrectQueenMove() {
        Parsers.parseFEN(Config.PERFT_POSITION_6);
        int move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("e2"),
            Config.BOARDSQUARES.get("d1"),
            Config.PIECES.get('Q'),
            0, 0, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("e2d1"));
   }

    @Test
    void testParseIncorrectKingMove() {
        Parsers.parseFEN(Config.PERFT_POSITION_6);
        Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Occupied by friendly piece
                        Parsers.parseMove("g1g2");
                    }, 
                    "Invalid move: g1g2");
         Assertions.assertThrowsExactly(IllegalArgumentException.class, 
                    () -> {
                        // Not a move a king can make
                        Parsers.parseMove("g1f6");
                    }, 
                    "Invalid move: g1f6");
   }

    @Test
    void testParseCorrectKingMove() {
        Parsers.parseFEN(Config.PERFT_POSITION_6);
        int move = MoveCoder.encodeMove(
            Config.BOARDSQUARES.get("g1"),
            Config.BOARDSQUARES.get("h1"),
            Config.PIECES.get('K'),
            0, 0, 0, 0, 0
        );
        Assertions.assertEquals(move, Parsers.parseMove("g1h1"));
   }
    /*
     *  ################################################
        ##                                            ##
        ##              Parse Position                ##
        ##                                            ##
        ################################################
     */

    @Test
    void testParsePosition() {
        long expOcc1 = config.OCCUPANCIES[1];
        long expOcc2 = config.OCCUPANCIES[2];
        long expPB3 = config.PIECE_BITBOARDS[3];
        // Since the default position is the start position
        // we screw it up a bit by first loading a random FEN.
        Parsers.parseFEN(Config.CMK_POSITION);
        Parsers.parsePosition("position startpos");
        Assertions.assertEquals(expOcc1, config.OCCUPANCIES[1]);
        Assertions.assertEquals(expOcc2, config.OCCUPANCIES[2]);
        Assertions.assertEquals(expPB3, config.PIECE_BITBOARDS[3]);
    }

    @Test
    void testParseInvalidPositionCommand() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // position has to be followed by another command
            Parsers.parsePosition("position");
        }, "Invalid command");
    }

    @Test
    void testParseInvalidPositionCommand2() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // position has to be followed by another command
            Parsers.parsePosition("position startpos moves e2e5");
        }, "Invalid command");
    }

    @Test
    void testParseInvalidPosition2ndCommandNotFEN() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // position has to be followed by another command
            Parsers.parsePosition("position fien");
        }, "Invalid command");
    }

    @Test
    void testParsePositionInvalidCommandNotMoves() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // 'moves' spelt wrongly
            Parsers.parsePosition("position startpos mooves");
        }, "Invalid command");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // missing 'moves' command before moves are passes
            Parsers.parsePosition("position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 e2a6 e8g8");
        }, "Invalid command");
    }

    // @Test
    // void testParseFENPosition() {}



    // @Test
    // void testParseIncorrectMoveKingIntoCheck() {
            // Not necessary. We enforce this when making the move, not when parsing.
    //     // r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P3/P1NP1b2/1PP1QP1P/R4RK1 w - - 0 10
    //     Parsers.parseFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P3/P1NP1b2/1PP1QP1P/R4RK1 w - - 0 10");
       
    //     // Invalid moves should throw IllegalArgumentException
    //     Assertions.assertThrowsExactly(IllegalArgumentException.class, 
    //                 () -> {
    //                     // Move king to attacked square (moving into check)
    //                     Parsers.parseMove("g1h1");
    //                 }, 
    //                 "Invalid move: g1h1");

    //     Parsers.parseFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P3/P1NP4/1PP1QP1P/R4RK1 w - - 0 10");
    //     Assertions.assertThrowsExactly(IllegalArgumentException.class, 
    //                 () -> {
    //                     // Move pawn protecting king from bishop attack (moving into check)
    //                     Parsers.parseMove("f2f3");
    //                 }, 
    //                 "Invalid move: f2f3");
    // }
}
