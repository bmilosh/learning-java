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
                if (((bitboard << 7) & Config.NOT_FILE_H) != 0) {
                    attacks |= bitboard << 7;
                }
                if (((bitboard << 9) & Config.NOT_FILE_A) != 0) {
                    attacks |= bitboard << 9;
                }
                break;
        
            default:
                if (((bitboard >> 7) & Config.NOT_FILE_A) != 0) {
                    attacks |= bitboard >> 7;
                }
                if (((bitboard >> 9) & Config.NOT_FILE_H) != 0) {
                    attacks |= bitboard >> 9;
                }
                break;
        }
        return attacks;
    }

    public static long generateKingAttacks(int square) {
        long attacks = 0;
        long bboard = BitBoard.setBitAtIndex(0, square);

        if (((bboard >> 1) & Config.NOT_FILE_H) != 0) {
            attacks |= bboard >> 1;
        }
        if (((bboard >> 9) & Config.NOT_FILE_H) != 0) {
            attacks |= bboard >> 9;
        }
        if (((bboard >> 7) & Config.NOT_FILE_A) != 0) {
            attacks |= bboard >> 7;
        }
        if (((bboard << 1) & Config.NOT_FILE_A) != 0) {
            attacks |= bboard << 1;
        }
        if (((bboard << 9) & Config.NOT_FILE_A) != 0) {
            attacks |= bboard << 9;
        }
        if (((bboard << 7) & Config.NOT_FILE_H) != 0) {
            attacks |= bboard << 7;
        }        
        attacks |= bboard >> 8;
        attacks |= bboard << 8;
        return attacks;
    }

    public static long generateKnightAttacks(int square) {
        long attacks = 0;
        long bboard = BitBoard.setBitAtIndex(0, square);
        if (((bboard >> 17) & Config.NOT_FILE_H) != 0) {
            attacks |= bboard >> 17;
        }
        if (((bboard >> 10) & Config.NOT_FILE_HG) != 0) {
            attacks |= bboard >> 10;
        }
        if (((bboard >> 15) & Config.NOT_FILE_A) != 0) {
            attacks |= bboard >> 15;
        }
        if (((bboard >> 6) & Config.NOT_FILE_AB) != 0) {
            attacks |= bboard >> 6;
        }
        if (((bboard << 17) & Config.NOT_FILE_A) != 0) {
            attacks |= bboard << 17;
        }
        if (((bboard << 10) & Config.NOT_FILE_AB) != 0) {
            attacks |= bboard << 10;
        }
        if (((bboard << 15) & Config.NOT_FILE_H) != 0) {
            attacks |= bboard << 15;
        }
        if (((bboard << 6) & Config.NOT_FILE_HG) != 0) {
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
            result |= (1L << (rank * 8 + targetFile));
            if (((1L << (rank * 8 + targetFile)) & bitboard) != 0) {
                break;
            }
        }
        for (int rank = targetRank - 1; rank >= 0; rank--) {
            result |= (1L << (rank * 8 + targetFile));
            if (((1L << (rank * 8 + targetFile)) & bitboard) != 0) {
                break;
            }
        }
        for (int file = targetFile - 1; file >= 0; file--) {
            result |= (1L << (targetRank * 8 + file));
            if (((1L << (targetRank * 8 + file)) & bitboard) != 0) {
                break;
            }
        }
        for (int file = targetFile + 1; file <= 7; file++) {
            result |= (1L << (targetRank * 8 + file));
            if (((1L << (targetRank * 8 + file)) & bitboard) != 0) {
                break;
            }
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
}
