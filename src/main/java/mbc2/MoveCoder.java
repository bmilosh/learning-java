package mbc2;

public class MoveCoder {
    /*
    *  ################################################
        ##                                            ##
        ##               Move Encoding                ##
        ##                                            ##
        ################################################
    */
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
        return encodedMove & Config.SOURCE_SQUARE_MASK;
    }

    public static int getTargetSquare(int encodedMove) {
        return (encodedMove & Config.TARGET_SQUARE_MASK) >> 6;
    }

    public static int getMovingPiece(int encodedMove) {
        return (encodedMove & Config.MOVING_PIECE_MASK) >> 12;
    }

    public static int getPromotedPiece(int encodedMove) {
        return (encodedMove & Config.PROMOTED_PIECE_MASK) >> 16;
    }

    public static int getCaptureFlag(int encodedMove) {
        return (encodedMove & Config.CAPTURE_MASK) >> 20;
    }

    public static int getDPPFlag(int encodedMove) {
        return (encodedMove & Config.DOUBLE_PAWN_PUSH_MASK) >> 21;
    }

    public static int getEnPassantFlag(int encodedMove) {
        return (encodedMove & Config.EN_PASSANT_MASK) >> 22;
    }

    public static int getCastlingFlag(int encodedMove) {
        return (encodedMove & Config.CASTLING_MASK) >> 23;
    }
}
