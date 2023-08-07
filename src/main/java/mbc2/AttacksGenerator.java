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

    public static long generateBishopAttacks(int square) {
        long attacks = 0L;
        int targetRank = square / 8;
        int targetFile = square % 8;
        
        List<Integer> leftList = IntStream.iterate(1 + targetRank, i -> i < 7, i -> i + 1).boxed().collect(Collectors.toList());
        List<Integer> rightList = IntStream.iterate(1 + targetFile, i -> i < 7, i -> i + 1).boxed().collect(Collectors.toList());
        List<Pair<Integer, Integer>> zipped = ZipUtility.zip(leftList, rightList);
        for (Pair<Integer, Integer> pair : zipped) {
            attacks |= (1L << (pair.first * 8 + pair.second));
        }

        leftList = IntStream.iterate(targetRank - 1, i -> i > 0, i -> i - 1).boxed().collect(Collectors.toList());
        zipped = ZipUtility.zip(leftList, rightList);
        for (Pair<Integer, Integer> pair : zipped) {
            attacks |= (1L << (pair.first * 8 + pair.second));
        }

        leftList = IntStream.iterate(1 + targetRank, i -> i < 7, i -> i + 1).boxed().collect(Collectors.toList());
        rightList = IntStream.iterate(targetFile - 1, i -> i > 0, i -> i - 1).boxed().collect(Collectors.toList());
        zipped = ZipUtility.zip(leftList, rightList);
        for (Pair<Integer, Integer> pair : zipped) {
            attacks |= (1L << (pair.first * 8 + pair.second));
        }

        leftList = IntStream.iterate(targetRank - 1, i -> i > 0, i -> i - 1).boxed().collect(Collectors.toList());
        zipped = ZipUtility.zip(leftList, rightList);
        for (Pair<Integer, Integer> pair : zipped) {
            attacks |= (1L << (pair.first * 8 + pair.second));
        }

        return attacks;
    }
}
