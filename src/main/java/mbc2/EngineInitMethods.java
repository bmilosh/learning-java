package mbc2;

public class EngineInitMethods {
    // private  Config config;
    // public EngineInitMethods (Config Config) {
    //     Config = Config;
    // }
    private static void initBoardSquares() {
        for (int idx = 0; idx < Config.SQUARES.length; idx++) {
            Config.BOARDSQUARES.put(Config.SQUARES[idx], idx);
        }
    }

    private static void initPieces() {
        for (int idx = 0; idx < 12; idx++) {
            Config.PIECES.put(Config.ASCII_PIECES[idx], idx);
        }
    }

    private static void initColours() {
        for (int idx = 0; idx < 2; idx++) {
            Config.COLOURS.put(Config.SIDES[idx], idx);
        }
    }

    private static void initCastling() {
        Config.CASTLING.put('q', 1);
        Config.CASTLING.put('k', 2);
        Config.CASTLING.put('Q', 4);
        Config.CASTLING.put('K', 8);
    }

    private static void initLeaperPiecesAttacks() {
        for (int square = 0; square < 64; square++) {
            // Initialise Pawn Attacks
            AttacksGenerator.PAWN_ATTACKS[Config.COLOURS.get('w')][square] = AttacksGenerator.generatePawnAttacks('w', square);
            AttacksGenerator.PAWN_ATTACKS[Config.COLOURS.get('b')][square] = AttacksGenerator.generatePawnAttacks('b', square);
            // Initialise Knight Attacks
            AttacksGenerator.KNIGHT_ATTACKS[square] = AttacksGenerator.generateKnightAttacks(square);
            // Initialise King Attacks
            AttacksGenerator.KING_ATTACKS[square] = AttacksGenerator.generateKingAttacks(square);
        }
    }

    private static void initSliderPiecesAttacks(boolean isBishop) {
        long attackMask, magicNumber;
        for (int square = 0; square < 64; square++) {
            // First, we initialise the attack mask for this square
            if (isBishop) {
                AttacksGenerator.BISHOP_MASKS[square] = AttacksGenerator.generateBishopAttacks(square);
                attackMask =  AttacksGenerator.BISHOP_MASKS[square];
                magicNumber = AttacksGenerator.BISHOP_MAGIC_NUMBERS[square];
            } else {
                AttacksGenerator.ROOK_MASKS[square] = AttacksGenerator.generateRookAttacks(square);
                attackMask =  AttacksGenerator.ROOK_MASKS[square];
                magicNumber = AttacksGenerator.ROOK_MAGIC_NUMBERS[square];
            }
            int numberOfSetBits = Long.bitCount(attackMask);
            int occupancyIndices = 1 << numberOfSetBits;

            for (int index = 0; index < occupancyIndices; index++) {
                long occupancy = OccupancySetter.run(index, numberOfSetBits, attackMask);
                int magicIndex = (int) ((((occupancy * magicNumber) & 0xFFFFFFFFFFFFFFFFL) >> (64 - numberOfSetBits)) & 0xFFFFFFFFL);
                if (isBishop) {
                    magicIndex = magicIndex < 0 ? 512 + magicIndex : magicIndex; 
                    // Finally
                    AttacksGenerator.BISHOP_ATTACKS[square][magicIndex] = AttacksGenerator.getBishopAttacksOnTheFly(square, occupancy);
                } else {
                    magicIndex = magicIndex < 0 ? 4096 + magicIndex : magicIndex; 
                    AttacksGenerator.ROOK_ATTACKS[square][magicIndex] = AttacksGenerator.getRookAttacksOnTheFly(square, occupancy);
                }
            }
        }
    }

    private static void initPawnMovesMask() {
        for (int square = 8; square < 56; square++) {
            // set double pawn push for second and seventh ranks
            if (square >= 8 && square < 16) {
                AttacksGenerator.PAWN_MOVES_MASKS[Config.COLOURS.get('b')][square] = (1L << (square + 16));
            } else if (square >= 48 && square < 56) {
                AttacksGenerator.PAWN_MOVES_MASKS[Config.COLOURS.get('w')][square] = (1L << (square - 16));
            } 

            // regular pawn push
            AttacksGenerator.PAWN_MOVES_MASKS[Config.COLOURS.get('w')][square] |= (1L << (square - 8));
            AttacksGenerator.PAWN_MOVES_MASKS[Config.COLOURS.get('b')][square] |= (1L << (square + 8));
        }
    }

    public static void initHashKeys() {
        Config config = new Config();
        config.PSEUDORANDOM_NUMBER_STATE = 1804289383L;
        
        // Start with piece keys
        for (int piece = 0; piece < 12; piece++) {
            for (int square = 0; square < 64; square++) {
                Config.PIECE_KEYS[piece][square] = PseudorandomGenerator.generateRandom64bitNumber(config);
            }
        }

        // enpassant keys
        for (int square = 0; square < 64; square++) {
            Config.ENPASSANT_KEYS[square] = PseudorandomGenerator.generateRandom64bitNumber(config);
        }

        // castling keys
        for (int num = 0; num < 16; num++) {
            Config.CASTLING_KEYS[num] = PseudorandomGenerator.generateRandom64bitNumber(config);
        }

        // side key
        Config.SIDE_KEY = PseudorandomGenerator.generateRandom64bitNumber(config);
    }

    public static void initAll() {
        initBoardSquares();
        initPieces();
        initColours();
        initCastling();
        initLeaperPiecesAttacks();
        initSliderPiecesAttacks(true);
        initSliderPiecesAttacks(false);
        initPawnMovesMask();
        initHashKeys();
    }

}
