package mbc2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AttacksGenerator {
    public static long generatePawnAttacks(char colour, int square) {
        long attacks = 0L;
        long bitboard = BitBoard.setBitAtIndex(0, square);
        switch (colour) {
            case 'b':
                if (((bitboard << 7) & NOT_FILE_H) != 0) {
                    attacks |= bitboard << 7;
                }
                if (((bitboard << 9) & NOT_FILE_A) != 0) {
                    attacks |= bitboard << 9;
                }
                break;
        
            default:
                if (((bitboard >>> 7) & NOT_FILE_A) != 0) {
                    attacks |= bitboard >>> 7;
                }
                if (((bitboard >>> 9) & NOT_FILE_H) != 0) {
                    attacks |= bitboard >>> 9;
                }
                break;
        }
        return attacks;
    }

    public static long generateKingAttacks(int square) {
        long attacks = 0L;
        long bboard = BitBoard.setBitAtIndex(0L, square);

        if (((bboard >>> 1) & NOT_FILE_H) != 0) {
            attacks |= bboard >>> 1;
        }
        if (((bboard >>> 9) & NOT_FILE_H) != 0) {
            attacks |= bboard >>> 9;
        }
        if (((bboard >>> 7) & NOT_FILE_A) != 0) {
            attacks |= bboard >>> 7;
        }
        if (((bboard << 1) & NOT_FILE_A) != 0) {
            attacks |= bboard << 1;
        }
        if (((bboard << 9) & NOT_FILE_A) != 0) {
            attacks |= bboard << 9;
        }
        if (((bboard << 7) & NOT_FILE_H) != 0) {
            attacks |= bboard << 7;
        }        
        // if ((bboard >>> 8) > 0) {attacks |= bboard >>> 8;}
        attacks |= bboard >>> 8;
        attacks |= bboard << 8;
        return attacks;
    }

    public static long generateKnightAttacks(int square) {
        long attacks = 0L;
        long bboard = BitBoard.setBitAtIndex(0, square);
        if (((bboard >>> 17) & NOT_FILE_H) != 0) {
            attacks |= bboard >>> 17;
        }
        if (((bboard >>> 10) & NOT_FILE_HG) != 0) {
            attacks |= bboard >>> 10;
        }
        if (((bboard >>> 15) & NOT_FILE_A) != 0) {
            attacks |= bboard >>> 15;
        }
        if (((bboard >>> 6) & NOT_FILE_AB) != 0) {
            attacks |= bboard >>> 6;
        }
        if (((bboard << 17) & NOT_FILE_A) != 0) {
            attacks |= bboard << 17;
        }
        if (((bboard << 10) & NOT_FILE_AB) != 0) {
            attacks |= bboard << 10;
        }
        if (((bboard << 15) & NOT_FILE_H) != 0) {
            attacks |= bboard << 15;
        }
        if (((bboard << 6) & NOT_FILE_HG) != 0) {
            attacks |= bboard << 6;
        }
        return attacks;
    }

    public static long generateRookAttacks(int square) {
        long attacks = 0L;
        int targetRank = square / 8;
        int targetFile = square % 8;
        for (int rank = targetRank + 1; rank < 7; rank++) {
            attacks |= (1L << (rank * 8 + targetFile));
        }
        for (int rank = targetRank - 1; rank > 0; rank--) {
            attacks |= (1L << (rank * 8 + targetFile));
        }
        for (int file = targetFile - 1; file > 0; file--) {
            attacks |= (1L << (targetRank * 8 + file));
        }
        for (int file = targetFile + 1; file < 7; file++) {
            attacks |= (1L << (targetRank * 8 + file));
        }
        return attacks;
    }

    private static class BishopHelperClass{
        long run(List<Integer> leftList, List<Integer> rightList, boolean onTheFly, long bitboard) {
            long result = 0L;
            List<Pair<Integer, Integer>> zipped = ZipUtility.zip(leftList, rightList);
            for (Pair<Integer, Integer> pair : zipped) {
                result |= (1L << (pair.first * 8 + pair.second));
                if (onTheFly && ((1L << (pair.first * 8 + pair.second)) & bitboard) != 0) {
                    break;
                }
            }
            return result;
        }
    }
    // We'll use this in the Bishop attack methods
    private static BishopHelperClass bishopHelper = new BishopHelperClass();

    public static long generateBishopAttacks(int square) {
        long attacks = 0L;
        int targetRank = square / 8;
        int targetFile = square % 8;
        
        // These simulate Python's range function.
        List<Integer> leftList = IntStream.iterate(1 + targetRank, i -> i < 7, i -> i + 1).boxed().collect(Collectors.toList());
        List<Integer> leftListMinus = IntStream.iterate(targetRank - 1, i -> i > 0, i -> i - 1).boxed().collect(Collectors.toList());
        List<Integer> rightList = IntStream.iterate(1 + targetFile, i -> i < 7, i -> i + 1).boxed().collect(Collectors.toList());
        List<Integer> rightListMinus = IntStream.iterate(targetFile - 1, i -> i > 0, i -> i - 1).boxed().collect(Collectors.toList());

        attacks |= bishopHelper.run(leftList, rightList, false, 0);
        attacks |= bishopHelper.run(leftListMinus, rightList, false, 0);
        attacks |= bishopHelper.run(leftList, rightListMinus, false, 0);
        attacks |= bishopHelper.run(leftListMinus, rightListMinus, false, 0);

        return attacks;
    }

    public static long getRookAttacksOnTheFly(int square, long bitboard) {
        /*
         *  Returns a bitboard in which all the squares a rook on square "square"
            can go to have their corresponding bit index set, taking into account blocked squares
            defined by "bitboard".
         */
        long result = 0L;
        int targetRank = square / 8;
        int targetFile = square % 8;

        for (int rank = targetRank + 1; rank <= 7; rank++) {
            long bit = 1L << (rank * 8 + targetFile);
            result |= bit;
            if ((bit & bitboard) != 0) break;
        }
        for (int rank = targetRank - 1; rank >= 0; rank--) {
            long bit = 1L << (rank * 8 + targetFile);
            result |= bit;
            if ((bit & bitboard) != 0) break;
        }
        for (int file = targetFile - 1; file >= 0; file--) {
            long bit = 1L << (targetRank * 8 + file);
            result |= bit;
            if ((bit & bitboard) != 0) break;
        }
        for (int file = targetFile + 1; file <= 7; file++) {
            long bit = 1L << (targetRank * 8 + file);
            result |= bit;
            if ((bit & bitboard) != 0) break;
        }

        return result;
    }

    public static long getBishopAttacksOnTheFly(int square, long bitboard) {
        /*
         *  Returns a bitboard in which all the squares a bishop on square "square"
            can go to have their corresponding bit index set, taking into account blocked squares
            defined by "bitboard".
         */
        long result = 0L;
        int targetRank = square / 8;
        int targetFile = square % 8;
        List<Integer> leftList = IntStream.iterate(1 + targetRank, i -> i <= 7, i -> i + 1).boxed().collect(Collectors.toList());
        List<Integer> leftListMinus = IntStream.iterate(targetRank - 1, i -> i >= 0, i -> i - 1).boxed().collect(Collectors.toList());
        List<Integer> rightList = IntStream.iterate(1 + targetFile, i -> i <= 7, i -> i + 1).boxed().collect(Collectors.toList());
        List<Integer> rightListMinus = IntStream.iterate(targetFile - 1, i -> i >= 0, i -> i - 1).boxed().collect(Collectors.toList());

        result |= bishopHelper.run(leftList, rightList, true, bitboard);
        result |= bishopHelper.run(leftListMinus, rightList, true, bitboard);
        result |= bishopHelper.run(leftList, rightListMinus, true, bitboard);
        result |= bishopHelper.run(leftListMinus, rightListMinus, true, bitboard);
        return result;
    }

    public static long getQueenAttacksOnTheFly(int square, long bitboard) {
        return getBishopAttacksOnTheFly(square, bitboard) | getRookAttacksOnTheFly(square, bitboard);
    }

    public static long getRookAttacks(int square, long occupancy) {
        // occupancy &= ROOK_MASKS[square];
        // occupancy *= ROOK_MAGIC_NUMBERS[square];
        // occupancy &= 0xFFFFFFFFFFFFFFFFL;
        // occupancy >>= 64 - ROOK_RELEVANCY_OCC_COUNT[square];
        // occupancy = (int) occupancy < 0 ? 4096 + occupancy : occupancy;
        int shift = 64 - ROOK_RELEVANCY_OCC_COUNT[square];
        long maskedOccupancy = (occupancy & ROOK_MASKS[square]) * ROOK_MAGIC_NUMBERS[square] & 0xFFFFFFFFFFFFFFFFL;
        long shiftedOccupancy = maskedOccupancy >> shift;
        int adjustedIndex = (int) shiftedOccupancy < 0 ? 4096 + (int) shiftedOccupancy : (int) shiftedOccupancy;

        // return ROOK_ATTACKS[square][(int)occupancy];
        return ROOK_ATTACKS[square][adjustedIndex];
    }

    public static long getBishopAttacks(int square, long occupancy) {
        occupancy &= BISHOP_MASKS[square];
        occupancy *= BISHOP_MAGIC_NUMBERS[square];
        occupancy &= 0xFFFFFFFFFFFFFFFFL;
        occupancy >>= 64 - BISHOP_RELEVANCY_OCC_COUNT[square];
        occupancy = (int) occupancy < 0 ? 512 + occupancy : occupancy;
    
        return BISHOP_ATTACKS[square][(int)occupancy];
    }

    public static long getQueenAttacks(int square, long occupancy) {
        return getBishopAttacks(square, occupancy) | getRookAttacks(square, occupancy);
    }

    public static long NOT_FILE_A = -72340172838076674L;
    public static long NOT_FILE_AB = -217020518514230020L;
    public static long NOT_FILE_H = 9187201950435737471L;
    public static long NOT_FILE_HG = 4557430888798830399L;
    
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
