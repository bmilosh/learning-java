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
    public static String KILLER_POSITION = "rnbqkb1r/pp1p1pPp/8/2p1pP2/1P6/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1";
    public static String CMK_POSITION = "r2q1rk1/ppp2ppp/2n1bn2/2b1p3/3pP3/3P1NPP/PPP1NPB1/R1BQ1RK1 b - - 0 9 ";
    public static String OTHER_KILLER_POSITION = "6k1/3q1pp1/pp5p/1r5n/8/1P3PP1/PQ4BP/2R3K1 w - - 0 1";
    public static String REPETITION_POSITION = "2r3k1/R7/8/1R6/8/8/P4KPP/8 w - - 0 40 ";
    public static String PERFT_POSITION_6 = "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10";

    /*
     *  ################################################
        ##                                            ##
        ##              Zobrist Hashing               ##
        ##                                            ##
        ################################################
     */

    // Indexed by piece and then square
    public static long[][] PIECE_KEYS = new long[12][64];

    // one for each square
    public static long[] ENPASSANT_KEYS = new long[64];

    // Castling keys
    public static long[] CASTLING_KEYS = new long[16];

    // Side key. We're keeping a key only for black
    public static long SIDE_KEY = 0;

    // hash key
    public long HASH_KEY = 0L;

    // hash flags
    public static int HASH_EXACT_FLAG = 0;
    public static int HASH_ALPHA_FLAG = 1;
    public static int HASH_BETA_FLAG = 2;
    public static String[] HASH_FLAGS = {"EXACT", "ALPHA", "BETA"};

    // hash_table size
    public static int HASH_TABLE_SIZE = 0x4000000;
    // public static int HASH_TABLE_SIZE = 0x400000;

    // Initialise transposition table
    public TranspositionTable HASH_TABLE = new TranspositionTable(HASH_TABLE_SIZE);

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

    public long PSEUDORANDOM_NUMBER_STATE = 1804289383L;

    // public static long NOT_FILE_A = -72340172838076674L;
    // public static long NOT_FILE_AB = -217020518514230020L;
    // public static long NOT_FILE_H = 9187201950435737471L;
    // public static long NOT_FILE_HG = 4557430888798830399L;

    public long[] PIECE_BITBOARDS = {
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
    public long[] OCCUPANCIES = {-281474976710656L, 65535L, -281474976645121L};

    public char SIDE_TO_MOVE = 'w';

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
    *   ################################################
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
    // Half-move counter
    public int PLY = 0;

    public static long[] MOVE_LIST = new long[256];
    public int MOVE_COUNT = 0;
    public long BEST_MOVE = 0;
    public int LEAF_NODES = 0;

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
    // /*
    // * most valuable victim & less valuable attacker
                            
    //     (Victims) Pawn Knight Bishop   Rook  Queen   King
    //   (Attackers)
    //         Pawn   105    205    305    405    505    605
    //       Knight   104    204    304    404    504    604
    //       Bishop   103    203    303    403    503    603
    //         Rook   102    202    302    402    502    602
    //        Queen   101    201    301    401    501    601
    //         King   100    200    300    400    500    600
    // */
    // // Access as MVV_LVA_TABLE[attacker_piece_index][victim_piece_index]
    // public static int[][] MVV_LVA = {
    //     {105, 205, 305, 405, 505, 605,  105, 205, 305, 405, 505, 605},
    //     {104, 204, 304, 404, 504, 604,  104, 204, 304, 404, 504, 604},
    //     {103, 203, 303, 403, 503, 603,  103, 203, 303, 403, 503, 603},
    //     {102, 202, 302, 402, 502, 602,  102, 202, 302, 402, 502, 602},
    //     {101, 201, 301, 401, 501, 601,  101, 201, 301, 401, 501, 601},
    //     {100, 200, 300, 400, 500, 600,  100, 200, 300, 400, 500, 600},
    
    //     {105, 205, 305, 405, 505, 605,  105, 205, 305, 405, 505, 605},
    //     {104, 204, 304, 404, 504, 604,  104, 204, 304, 404, 504, 604},
    //     {103, 203, 303, 403, 503, 603,  103, 203, 303, 403, 503, 603},
    //     {102, 202, 302, 402, 502, 602,  102, 202, 302, 402, 502, 602},
    //     {101, 201, 301, 401, 501, 601,  101, 201, 301, 401, 501, 601},
    //     {100, 200, 300, 400, 500, 600,  100, 200, 300, 400, 500, 600}
    // };

    // // Accessed as KILLER_MOVES[id (not more than 2)][ply (we chose a max of 64 plies)]
    // public int[][] KILLER_MOVES = new int[2][64];
    // // Accessed as HISTORY_MOVES[piece_index][square]
    // public int[][] HISTORY_MOVES = new int[12][64];
    // public int ORIGINAL_DEPTH;

    // /*
    //  *  ################################################
    //     ##                                            ##
    //     ##           Principal variation              ##
    //     ##                                            ##
    //     ################################################

    //   ================================
    //         Triangular PV table
    //   --------------------------------
    //     PV line: e2e4 e7e5 g1f3 b8c6
    //   ================================

    //        0    1    2    3    4    5
      
    //   0    m1   m2   m3   m4   m5   m6
      
    //   1    0    m2   m3   m4   m5   m6 
      
    //   2    0    0    m3   m4   m5   m6
      
    //   3    0    0    0    m4   m5   m6
       
    //   4    0    0    0    0    m5   m6
      
    //   5    0    0    0    0    0    m6

    //  */

    // public int[] PV_LENGTH = new int[MAX_PLY];
    // public int[][] PV_TABLE = new int[MAX_PLY][MAX_PLY];

    // // For PV scoring
    // public boolean FOLLOW_PV = false;
    // public boolean SCORE_PV = false;

    /*
     *  ################################################
        ##                                            ##
        ##        Attack [and Masks] Tables           ##
        ##                                            ##
        ################################################
     */
    // public static long[][] PAWN_ATTACKS = new long[2][64];
    // public static long[][] BISHOP_ATTACKS = new long[64][512];
    // public static long[][] ROOK_ATTACKS = new long[64][4096];
    // public static long[] KNIGHT_ATTACKS = new long[64];
    // public static long[] KING_ATTACKS = new long[64];

    // // Masks (only pawns, bishops and rooks need these)
    // public static long[][] PAWN_MOVES_MASKS = new long[2][64];
    // public static long[] BISHOP_MASKS = new long[64];
    // public static long[] ROOK_MASKS = new long[64];

    // public static int[] BISHOP_RELEVANCY_OCC_COUNT = {
    //     6, 5, 5, 5, 5, 5, 5, 6, 
    //     5, 5, 5, 5, 5, 5, 5, 5,
    //     5, 5, 7, 7, 7, 7, 5, 5,
    //     5, 5, 7, 9, 9, 7, 5, 5,
    //     5, 5, 7, 9, 9, 7, 5, 5,
    //     5, 5, 7, 7, 7, 7, 5, 5,
    //     5, 5, 5, 5, 5, 5, 5, 5,
    //     6, 5, 5, 5, 5, 5, 5, 6,
    // };
    // public static int[] ROOK_RELEVANCY_OCC_COUNT = {
    //     12, 11, 11, 11, 11, 11, 11, 12, 
    //     11, 10, 10, 10, 10, 10, 10, 11,
    //     11, 10, 10, 10, 10, 10, 10, 11,
    //     11, 10, 10, 10, 10, 10, 10, 11,
    //     11, 10, 10, 10, 10, 10, 10, 11,
    //     11, 10, 10, 10, 10, 10, 10, 11,
    //     11, 10, 10, 10, 10, 10, 10, 11,
    //     12, 11, 11, 11, 11, 11, 11, 12,
    // };

    /*
     *  ################################################
        ##                                            ##
        ##         UCI Time Control Variables         ##
        ##                                            ##
        ################################################
     */
    public boolean UCI_QUIT = false;          // exit from engine flag
    public int UCI_MOVES_TO_GO = 30;         // UCI "movestogo" command moves counter
    public int UCI_MOVE_TIME = -1;           // UCI "movetime" command time counter
    public int UCI_TIME = -1;                // UCI "time" command holder (ms)
    public int UCI_INCREMENT_TIME = 0;       // UCI "inc" command's time increment holder
    public long UCI_START_TIME = 0;           // UCI "starttime" command time holder
    public long UCI_STOP_TIME = 0;            // UCI "stoptime" command time holder
    public boolean UCI_TIME_IS_SET = false;  // variable to flag time control availability
    public boolean UCI_STOPPED = false;      // variable to flag when the time is up

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
    
    public String ENPASSANT_SQUARE = "no_square";

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
        1111    15  both sides can castle both directions
        
        1001       black king => queen side
                    white king => king side
     */
    
    public static HashMap<Character, Integer> CASTLING = new HashMap<Character, Integer>();
    public int CASTLING_RIGHT = 0;
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

    // /*
    //  * Board state copies
    //  */
    // public long[] OCCUPANCIES_COPY = OCCUPANCIES.clone();
    // public long[] PIECE_BITBOARDS_COPY = PIECE_BITBOARDS.clone();
    // public static char SIDE_TO_MOVE_COPY = 'w';
    // public static String ENPASSANT_SQUARE_COPY = "no_square";
    // public static int CASTLING_RIGHT_COPY = 0;
    // public static long[] MOVE_LIST_COPY = MOVE_LIST.clone();
    // public static int MOVE_COUNT_COPY = 0;

}
