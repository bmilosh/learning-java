package mbc2;

import java.util.HashMap;

public class Config {
    /*
     * This file hosts the config/global variables for the chess engine.
     * 
     * All HashMaps used here will be populated the first time the Main class runs.
     */

    /*
     *  ################################################
        ##                                            ##
        ##           FEN debug positions              ##
        ##                                            ##
        ################################################
     */
    public static String EMPTY_BOARD = "8/8/8/8/8/8/8/8 b - - 0 1 ";
    public static String START_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ";
    public static String TRICKY_POSITION = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 ";
    public static String KILLER_POSITION = "rnbqkb1r/pp1p1pPp/8/2p1pP2/1P1P4/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1";
    public static String CMK_POSITION = "r2q1rk1/ppp2ppp/2n1bn2/2b1p3/3pP3/3P1NPP/PPP1NPB1/R1BQ1RK1 b - - 0 9 ";
    public static String OTHER_KILLER_POSITION = "6k1/3q1pp1/pp5p/1r5n/8/1P3PP1/PQ4BP/2R3K1 w - - 0 1";
    public static String REPETITION_POSITION = "2r3k1/R7/8/1R6/8/8/P4KPP/8 w - - 0 40 ";

    /*
     *  ################################################
        ##                                            ##
        ##              Zobrist Hashing               ##
        ##                                            ##
        ################################################
     */

    /*
     *  ################################################
        ##                                            ##
        ##                Constants                   ##
        ##                                            ##
        ################################################
     */

    public static int MAX_PLY = 64;
    public static int MAX_DEPTH = 32;
    public static int FULL_DEPTH_MOVES = 4;
    public static int REDUCTION_LIMIT = 3;

    public static long PSEUDORANDOM_NUMBER_STATE = 1804289383L;

    public static long NOT_FILE_A = -72340172838076674L;
    public static long NOT_FILE_AB = -217020518514230020L;
    public static long NOT_FILE_H = 9187201950435737471L;
    public static long NOT_FILE_HG = 4557430888798830399L;

    public static long[] PIECE_BITBOARDS = {
        0b11111111L << 48,
        0b01000010L << 56,
        0b100100L << 56,
        0b10000001L << 56,
        0b1000L << 56,
        0b10000L << 56,
        0b11111111L << 8,
        0b01000010L,
        0b100100L,
        0b10000001L,
        0b1000L,
        0b10000L,
    };
    
    // Occupancy bitboards: [WHITE, BLACK, BOTH]
    public static long[] OCCUPANCIES = {-281474976710656L, 65535L, -281474976645121L};

    public static char SIDE_TO_MOVE = 'w';

    public static char[] ASCII_PIECES = {'P', 'N', 'B', 'R', 'Q', 'K', 'p', 'n', 'b', 'r', 'q', 'k'};

    public static String[] UNICODE_PIECES = {"♟︎", "♞", "♝", "♜", "♛", "♚", "♙", "♘", "♗", "♖", "♕", "♔"};

    public static String[] SQUARES = {
        "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
        "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
        "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
        "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
        "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
        "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
        "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
        "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1", "no_square",
    };

    public static HashMap<String, Integer> BOARDSQUARES = new HashMap<String, Integer>();

    public static HashMap<Character, Integer> PIECES = new HashMap<Character, Integer>(); 

    /*
    *  ################################################
        ##                                            ##
        ##              Move Encoding                 ##
        ##                                            ##
        ################################################


                binary move bits                               hexadecimal constants
            
            0000 0000 0000 0000 0011 1111    source square       0x3f
            0000 0000 0000 1111 1100 0000    target square       0xfc0
            0000 0000 1111 0000 0000 0000    piece               0xf000
            0000 1111 0000 0000 0000 0000    promoted piece      0xf0000
            0001 0000 0000 0000 0000 0000    capture flag        0x100000
            0010 0000 0000 0000 0000 0000    double push flag    0x200000
            0100 0000 0000 0000 0000 0000    enpassant flag      0x400000
            1000 0000 0000 0000 0000 0000    castling flag       0x800000
    */
    public static int SOURCE_SQUARE_MASK = 0x3f;
    public static int TARGET_SQUARE_MASK = 0xfc0;
    public static int MOVING_PIECE_MASK = 0xf000;
    public static int PROMOTED_PIECE_MASK = 0xf0000;
    public static int CAPTURE_MASK = 0x100000;
    public static int DOUBLE_PAWN_PUSH_MASK = 0x200000;
    public static int EN_PASSANT_MASK = 0x400000;
    public static int CASTLING_MASK = 0x800000;

    // Half-move counter
    public static int PLY = 0;

    public static long[] MOVE_LIST = new long[256];
    public static int MOVE_COUNT = 0;
    public static long BEST_MOVE = 0;

    /*
     *  ################################################
        ##                                            ##
        ##             Positional scores              ##
        ##                                            ##
        ################################################
     */

    public static int[] PAWN_POSITIONAL_SCORES = {
        90,  90,  90,  90,  90,  90,  90,  90,
        30,  30,  30,  40,  40,  30,  30,  30,
        20,  20,  20,  30,  30,  30,  20,  20,
        10,  10,  10,  20,  20,  10,  10,  10,
         5,   5,  10,  20,  20,   5,   5,   5,
         0,   0,   0,   5,   5,   0,   0,   0,
         0,   0,   0, -10, -10,   0,   0,   0,
         0,   0,   0,   0,   0,   0,   0,   0
    };

    public static int[] KNIGHT_POSITIONAL_SCORES = {
        -5,   0,   0,   0,   0,   0,   0,  -5,
        -5,   0,   0,  10,  10,   0,   0,  -5,
        -5,   5,  20,  20,  20,  20,   5,  -5,
        -5,  10,  20,  30,  30,  20,  10,  -5,
        -5,  10,  20,  30,  30,  20,  10,  -5,
        -5,   5,  20,  10,  10,  20,   5,  -5,
        -5,   0,   0,   0,   0,   0,   0,  -5,
        -5, -10,   0,   0,   0,   0, -10,  -5
    };

    public static int[] BISHOP_POSITIONAL_SCORES = {
        0,   0,   0,   0,   0,   0,   0,   0,
        0,   0,   0,   0,   0,   0,   0,   0,
        0,   0,   0,  10,  10,   0,   0,   0,
        0,   0,  10,  20,  20,  10,   0,   0,
        0,   0,  10,  20,  20,  10,   0,   0,
        0,  10,   0,   0,   0,   0,  10,   0,
        0,  30,   0,   0,   0,   0,  30,   0,
        0,   0, -10,   0,   0, -10,   0,   0
    };

    public static int[] ROOK_POSITIONAL_SCORES = {
        50,  50,  50,  50,  50,  50,  50,  50,
        50,  50,  50,  50,  50,  50,  50,  50,
         0,   0,  10,  20,  20,  10,   0,   0,
         0,   0,  10,  20,  20,  10,   0,   0,
         0,   0,  10,  20,  20,  10,   0,   0,
         0,   0,  10,  20,  20,  10,   0,   0,
         0,   0,  10,  20,  20,  10,   0,   0,
         0,   0,   0,  20,  20,   0,   0,   0
    };

    public static int[] KING_POSITIONAL_SCORES = {
        0,   0,   0,   0,   0,   0,   0,   0,
        0,   0,   5,   5,   5,   5,   0,   0,
        0,   5,   5,  10,  10,   5,   5,   0,
        0,   5,  10,  20,  20,  10,   5,   0,
        0,   5,  10,  20,  20,  10,   5,   0,
        0,   0,   5,  10,  10,   5,   0,   0,
        0,   5,   5,  -5,  -5,   0,   5,   0,
        0,   0,   5,   0, -15,   0,  10,   0
    };

    public static String[] MIRROR_POSITIONAL_SCORES = {
        "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1",
        "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
        "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
        "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
        "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
        "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
        "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
        "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"
    };

    public static int[][] POSITIONAL_PIECE_SCORES = {
        PAWN_POSITIONAL_SCORES,
        KNIGHT_POSITIONAL_SCORES,
        BISHOP_POSITIONAL_SCORES,
        ROOK_POSITIONAL_SCORES,
        {},
        KING_POSITIONAL_SCORES,
    };

    /*
     *  ################################################
        ##                                            ##
        ##        Attack [and Masks] Tables           ##
        ##                                            ##
        ################################################
     */
    public static long[][] PAWN_ATTACKS = new long[2][64];
    public static long[][] BISHOP_ATTACKS = new long[64][512];
    public static long[][] ROOK_ATTACKS = new long[64][4096];
    public static long[] KNIGHT_ATTACKS = new long[64];
    public static long[] KING_ATTACKS = new long[64];

    // Masks (only pawns, bishops and rooks need these)
    public static long[][] PAWN_MOVES_MASKS = new long[2][64];
    public static long[] BISHOP_MASKS = new long[64];
    public static long[] ROOK_MASKS = new long[64];

    public static int[] BISHOP_RELEVANCY_OCC_COUNT = {
        6, 5, 5, 5, 5, 5, 5, 6, 
        5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 9, 9, 7, 5, 5,
        5, 5, 7, 7, 7, 7, 5, 5,
        5, 5, 5, 5, 5, 5, 5, 5,
        6, 5, 5, 5, 5, 5, 5, 6,
    };
    public static int[] ROOK_RELEVANCY_OCC_COUNT = {
        12, 11, 11, 11, 11, 11, 11, 12, 
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        11, 10, 10, 10, 10, 10, 10, 11,
        12, 11, 11, 11, 11, 11, 11, 12,
    };

    /*
     *  ################################################
        ##                                            ##
        ##         UCI Time Control Variables         ##
        ##                                            ##
        ################################################
     */
    public static int UCI_QUIT = 0;                 // exit from engine flag
    public static int UCI_MOVES_TO_GO = 30;         // UCI "movestogo" command moves counter
    public static int UCI_MOVE_TIME = -1;           // UCI "movetime" command time counter
    public static int UCI_TIME = -1;                // UCI "time" command holder (ms)
    public static int UCI_INCREMENT_TIME = 0;       // UCI "inc" command's time increment holder
    public static int UCI_START_TIME = 0;           // UCI "starttime" command time holder
    public static int UCI_STOP_TIME = 0;            // UCI "stoptime" command time holder
    public static Boolean UCI_TIME_IS_SET = false;  // variable to flag time control availability
    public static int UCI_STOPPED = 0;              // variable to flag when the time is up

    /*
    *  ################################################
        ##                                            ##
        ##             Material scores                ##
        ##                                            ##
        ################################################


            ♙ =   100   = ♙
            ♘ =   300   = ♙ * 3
            ♗ =   350   = ♙ * 3 + ♙ * 0.5
            ♖ =   500   = ♙ * 5
            ♕ =   1000  = ♙ * 10
            ♔ =   10000 = ♙ * 100
    */
    // Ordered as in pieces: ["P", "N", "B", "R", "Q", "K", "p", "n", "b", "r", "q", "k"]
    public static int[] MATERIAL_SCORES = {100, 300, 350, 500, 1_000, 10_000, -100, -300, -350, -500, -1_000, -10_000};

    public static char[] SIDES = {'w', 'b'};
    public static HashMap<Character, Integer> COLOURS = new HashMap<Character, Integer>();
    
    public static String ENPASSANT_SQUARE = "no_square";

    /*
     *  ################################################
        ##                                            ##
        ##                Castling                    ##
        ##                                            ##
        ################################################

        Castling representations
            bin    dec
            
        0001    1  black king can castle to the queen side 
        0010    2  black king can castle to the king side 
        0100    4  white king can castle to the queen side
        1000    8  white king can castle to the king side

        examples
        1111    17  both sides can castle both directions
        
        1001       black king => queen side
                    white king => king side
     */
    
    public static HashMap<Character, Integer> CASTLING = new HashMap<Character, Integer>();
    public static int CASTLING_RIGHT = 0;
    /*
     *  # This predefined table helps with updating
        # castling right after every move.
        # Movement on squares other than kings' and
        # rooks' opening squares do not affect castling right
        # which is set at 15 for full rights for both sides.
        # However, movements on kings' and/or rooks' opening
        # squares, whether it's a source or target square,
        # changes castling rights.
     */
    public static int[] CASTLING_RIGHT_LOOKUP_TABLE = {
        14, 15, 15, 15, 12, 15, 15, 13,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15,
        11, 15, 15, 15,  3, 15, 15, 7,
    };

    /*
     * Board state copies
     */
    public static long[] OCCUPANCIES_COPY = OCCUPANCIES.clone();
    public static long[] PIECE_BITBOARDS_COPY = PIECE_BITBOARDS.clone();
    public static char SIDE_TO_MOVE_COPY = 'w';
    public static String ENPASSANT_SQUARE_COPY = "no_square";
    public static int CASTLING_RIGHT_COPY = 0;
    public static long[] MOVE_LIST_COPY = MOVE_LIST.clone();
    public static int MOVE_COUNT_COPY = 0;

    /*
     *  ################################################
        ##                                            ##
        ##                Magic Numbers               ##
        ##                                            ##
        ################################################
     */

     public static long[] ROOK_MAGIC_NUMBERS = {
        0x8a80104000800020L,
        0x140002000100040L,
        0x2801880a0017001L,
        0x100081001000420L,
        0x200020010080420L,
        0x3001c0002010008L,
        0x8480008002000100L,
        0x2080088004402900L,
        0x800098204000L,
        0x2024401000200040L,
        0x100802000801000L,
        0x120800800801000L,
        0x208808088000400L,
        0x2802200800400L,
        0x2200800100020080L,
        0x801000060821100L,
        0x80044006422000L,
        0x100808020004000L,
        0x12108a0010204200L,
        0x140848010000802L,
        0x481828014002800L,
        0x8094004002004100L,
        0x4010040010010802L,
        0x20008806104L,
        0x100400080208000L,
        0x2040002120081000L,
        0x21200680100081L,
        0x20100080080080L,
        0x2000a00200410L,
        0x20080800400L,
        0x80088400100102L,
        0x80004600042881L,
        0x4040008040800020L,
        0x440003000200801L,
        0x4200011004500L,
        0x188020010100100L,
        0x14800401802800L,
        0x2080040080800200L,
        0x124080204001001L,
        0x200046502000484L,
        0x480400080088020L,
        0x1000422010034000L,
        0x30200100110040L,
        0x100021010009L,
        0x2002080100110004L,
        0x202008004008002L,
        0x20020004010100L,
        0x2048440040820001L,
        0x101002200408200L,
        0x40802000401080L,
        0x4008142004410100L,
        0x2060820c0120200L,
        0x1001004080100L,
        0x20c020080040080L,
        0x2935610830022400L,
        0x44440041009200L,
        0x280001040802101L,
        0x2100190040002085L,
        0x80c0084100102001L,
        0x4024081001000421L,
        0x20030a0244872L,
        0x12001008414402L,
        0x2006104900a0804L,
        0x1004081002402L,        
     };

    public static long[] BISHOP_MAGIC_NUMBERS = {
        0x40040844404084L,
        0x2004208a004208L,
        0x10190041080202L,
        0x108060845042010L,
        0x581104180800210L,
        0x2112080446200010L,
        0x1080820820060210L,
        0x3c0808410220200L,
        0x4050404440404L,
        0x21001420088L,
        0x24d0080801082102L,
        0x1020a0a020400L,
        0x40308200402L,
        0x4011002100800L,
        0x401484104104005L,
        0x801010402020200L,
        0x400210c3880100L,
        0x404022024108200L,
        0x810018200204102L,
        0x4002801a02003L,
        0x85040820080400L,
        0x810102c808880400L,
        0xe900410884800L,
        0x8002020480840102L,
        0x220200865090201L,
        0x2010100a02021202L,
        0x152048408022401L,
        0x20080002081110L,
        0x4001001021004000L,
        0x800040400a011002L,
        0xe4004081011002L,
        0x1c004001012080L,
        0x8004200962a00220L,
        0x8422100208500202L,
        0x2000402200300c08L,
        0x8646020080080080L,
        0x80020a0200100808L,
        0x2010004880111000L,
        0x623000a080011400L,
        0x42008c0340209202L,
        0x209188240001000L,
        0x400408a884001800L,
        0x110400a6080400L,
        0x1840060a44020800L,
        0x90080104000041L,
        0x201011000808101L,
        0x1a2208080504f080L,
        0x8012020600211212L,
        0x500861011240000L,
        0x180806108200800L,
        0x4000020e01040044L,
        0x300000261044000aL,
        0x802241102020002L,
        0x20906061210001L,
        0x5a84841004010310L,
        0x4010801011c04L,
        0xa010109502200L,
        0x4a02012000L,
        0x500201010098b028L,
        0x8040002811040900L,
        0x28000010020204L,
        0x6000020202d0240L,
        0x8918844842082200L,
        0x4010011029020020L,
    };

}
