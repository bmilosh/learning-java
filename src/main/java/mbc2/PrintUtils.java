package mbc2;

public class PrintUtils {
    // public static Config Config;
    
    public static void printBitBoard(long bitboard) {
        System.out.println();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (file == 0) {
                    System.out.printf("%d   ", 8 - rank);
                }
                int square = rank * 8 + file;
                int value = BitBoard.getBitAtIndex(bitboard, square);
                if (file < 7) {
                    System.out.printf(" %d ", value);
                } else {
                    System.out.printf(" %d \n", value);
                }
            }
        }
        System.out.println("\n     a  b  c  d  e  f  g  h\n");
        System.out.println("        Bitboard: " + bitboard);
    }

    public static String moveToString(int move) {
        int promotedPiece = MoveCoder.getPromotedPiece(move);
        char pMS = promotedPiece != 0 ? Config.ASCII_PIECES[promotedPiece] : ' ';
        char pMSLower = Character.toLowerCase(pMS);
        String source = Config.SQUARES[MoveCoder.getSourceSquare(move)];
        String target = Config.SQUARES[MoveCoder.getTargetSquare(move)];
        if (pMSLower != ' ') {
            return String.format("%s%s%s", source,target, pMSLower);
        }
        return String.format("%s%s", source, target);
    }

    public static void printMove(int move, boolean addNewLine) {
        if (addNewLine) System.out.println(moveToString(move));
        else System.out.print(moveToString(move));
    }

    public static void printBoard(Config config) {
        System.out.println();
        // Get side to move and en passant square
        String stm = config.SIDE_TO_MOVE == 'w' ? "white" : "black";
        String enPString = config.ENPASSANT_SQUARE != "no_square" ? config.ENPASSANT_SQUARE : "None";

        // Get all castling rights
        char K = (config.CASTLING_RIGHT & 8) != 0 ? 'K' : '-';
        char Q = (config.CASTLING_RIGHT & 4) != 0 ? 'Q' : '-';
        char k = (config.CASTLING_RIGHT & 2) != 0 ? 'k' : '-';
        char q = (config.CASTLING_RIGHT & 1) != 0 ? 'q' : '-';

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (file == 0) {
                    System.out.printf("%d   ", 8 - rank);
                }
                int square = rank * 8 + file;
                char piece = '.';
                for (int idx = 0; idx < config.PIECE_BITBOARDS.length; idx++) {
                    if (BitBoard.getBitAtIndex(config.PIECE_BITBOARDS[idx], square) != 0) {
                        piece = Config.ASCII_PIECES[idx];
                        break;
                    }
                }
                if (file < 7) {
                    System.out.printf(" %s ", piece);
                } else {
                    System.out.printf(" %s \n", piece);
                }
            }
        }
        System.out.println("\n     a  b  c  d  e  f  g  h\n");
        System.out.printf("        Side to move: %s\n", stm);
        System.out.printf("        Enpassant square: %s\n", enPString);
        System.out.printf("        Castling rights: %s%s%s%s\n", K, Q, k, q);
        System.out.println();
    }
}
