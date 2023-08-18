package mbc2;

public class MoveCoder {
    /*
    *   ################################################
        ##                                            ##
        ##               Move Encoding                ##
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

    private static void sanitiseMove(
        int moveSource,     // between 0 and 63
        int moveTarget,     // between 0 and 63
        int movingPiece,    // between 0 and 11
        int promotedPiece,  // between 0 and 11
        int capture,        // 0 or 1
        int doublePawnPush, // 0 or 1
        int enPassant,      // 0 or 1
        int castling        // 0 or 1

    ) {
        if ((moveSource < 0) || (moveSource > 63)) {
            throw new IllegalArgumentException("moveSource should be between 0 and 63");
        }
        if ((moveSource < 0) || (moveTarget > 63)) {
            throw new IllegalArgumentException("moveTarget should be between 0 and 63");
        }
        if ((movingPiece < 0) || (movingPiece > 11)) {
            throw new IllegalArgumentException("movingPiece should be between 0 and 11");
        }
        if ((promotedPiece < 0) || (promotedPiece > 11)) {
            throw new IllegalArgumentException("promotedPiece should be between 0 and 11");
        }
        if ((capture < 0) || (capture > 1)) {
            throw new IllegalArgumentException("capture should be either 0 or 1");
        }
        if ((doublePawnPush < 0) || (doublePawnPush > 1)) {
            throw new IllegalArgumentException("doublePawnPush should be either 0 or 1");
        }
        if ((enPassant < 0) || (enPassant > 1)) {
            throw new IllegalArgumentException("enPassant should be either 0 or 1");
        }
        if ((castling < 0) || (castling > 1)) {
            throw new IllegalArgumentException("castling should be either 0 or 1");
        }
    }

    public static int encodeMove(
        int moveSource,     // between 0 and 63
        int moveTarget,     // between 0 and 63
        int movingPiece,    // between 0 and 11
        int promotedPiece,  // between 0 and 11
        int capture,        // 0 or 1
        int doublePawnPush, // 0 or 1
        int enPassant,      // 0 or 1
        int castling        // 0 or 1
    ) {
        sanitiseMove(moveSource, moveTarget, movingPiece, promotedPiece, capture, doublePawnPush, enPassant, castling);
        return (
            moveSource | moveTarget << 6 | movingPiece << 12 | promotedPiece << 16 
            | capture << 20 | doublePawnPush << 21 | enPassant << 22 | castling << 23
        );
    }
    /*
    *  ################################################
        ##                                            ##
        ##               Move Decoding                ##
        ##                                            ##
        ################################################
    */

    public static int getSourceSquare(int encodedMove) {
        return encodedMove & SOURCE_SQUARE_MASK;
    }

    public static int getTargetSquare(int encodedMove) {
        return (encodedMove & TARGET_SQUARE_MASK) >>> 6;
    }

    public static int getMovingPiece(int encodedMove) {
        return (encodedMove & MOVING_PIECE_MASK) >>> 12;
    }

    public static int getPromotedPiece(int encodedMove) {
        return (encodedMove & PROMOTED_PIECE_MASK) >>> 16;
    }

    public static int getCaptureFlag(int encodedMove) {
        return (encodedMove & CAPTURE_MASK) >>> 20;
    }

    public static int getDPPFlag(int encodedMove) {
        return (encodedMove & DOUBLE_PAWN_PUSH_MASK) >>> 21;
    }

    public static int getEnPassantFlag(int encodedMove) {
        return (encodedMove & EN_PASSANT_MASK) >>> 22;
    }

    public static int getCastlingFlag(int encodedMove) {
        return (encodedMove & CASTLING_MASK) >>> 23;
    }
}
