import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import mbc2.Config;
import mbc2.EngineInitMethods;
import mbc2.MoveCoder;

public class TestMoveCoder {
    @BeforeAll
    static void setup() {
        EngineInitMethods.initAll();
    }
    @Test
    void testMoveEncoding() {
        int moveSource = 0b111;
        int moveTarget = 0b111111;
        int movingPiece = 0b1001;
        int promotedPiece = 0;
        int capture = 1;
        int doublePawnPush = 0;
        int enPassant = 0;
        int castling = 0;
        Assertions.assertEquals(0b100001001111111000111, MoveCoder.encodeMove(
                    moveSource, 
                    moveTarget, 
                    movingPiece, 
                    promotedPiece, 
                    capture, 
                    doublePawnPush, 
                    enPassant, 
                    castling
        ));
        
        moveSource = Config.BOARDSQUARES.get("d2"); // 0b110011
        moveTarget = Config.BOARDSQUARES.get("d4"); // 0b100011
        movingPiece = Config.PIECES.get('P'); 
        promotedPiece = 0;
        capture = 0;
        doublePawnPush = 1;
        enPassant = 0;
        castling = 0;
        Assertions.assertEquals(0b1000000000100011110011, MoveCoder.encodeMove(
                    moveSource, 
                    moveTarget, 
                    movingPiece, 
                    promotedPiece, 
                    capture, 
                    doublePawnPush, 
                    enPassant, 
                    castling
        ));
        
        final int moveSourceFinal = moveSource;
        final int moveTargetFinal = moveTarget;
        final int promotedPieceFinal = promotedPiece;
        final int captureFinal = capture;
        final int doublePawnPushFinal = doublePawnPush;
        final int enPassantFinal = enPassant;
        final int castlingFinal = castling;
        final int movingPieceFinal = 14; // Should throw an exception because we don't have a piece with index greater than 11.
        Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
            MoveCoder.encodeMove(
                moveSourceFinal, 
                moveTargetFinal, 
                movingPieceFinal, 
                promotedPieceFinal, 
                captureFinal, 
                doublePawnPushFinal, 
                enPassantFinal, 
                castlingFinal
            );
        }, "movingPiece should be between 0 and 11");
    }

    @Test
    void testGetSourceSquare() {
        Assertions.assertEquals(0b110011, MoveCoder.getSourceSquare(0b1000000000100011110011));
        Assertions.assertEquals(0b111011, MoveCoder.getSourceSquare(0b1000000000100011111011));
        Assertions.assertEquals(0b100011, MoveCoder.getSourceSquare(0b1000000000100011100011));
    }

    @Test
    void testGetTargetSquare() {
        Assertions.assertEquals(0b100011, MoveCoder.getTargetSquare(0b1000000000100011110011));
        Assertions.assertEquals(0b100000, MoveCoder.getTargetSquare(0b1000000000100000111011));
        Assertions.assertEquals(0b11, MoveCoder.getTargetSquare(0b1000000000000011100011));
    }

    @Test
    void testGetMovingPiece() {
        Assertions.assertEquals(0, MoveCoder.getMovingPiece(0b1000000000100011110011));
        Assertions.assertEquals(1, MoveCoder.getMovingPiece(0b1000000001100000111011));
        Assertions.assertEquals(0b10, MoveCoder.getMovingPiece(0b1000000010000011100011));
        Assertions.assertEquals(0b1000, MoveCoder.getMovingPiece(0b1000001000000011100011));
    }

    @Test
    void testGetPromotedPiece() {
        Assertions.assertEquals(0, MoveCoder.getPromotedPiece(0b1000000000100011110011));
        Assertions.assertEquals(0b1010, MoveCoder.getPromotedPiece(0b1010100001100000111011));
        Assertions.assertEquals(0b1100, MoveCoder.getPromotedPiece(0b1011000010000011100011));
        Assertions.assertEquals(1, MoveCoder.getPromotedPiece(0b1000011000000011100011));
    }

    @Test
    void testGetCaptureFlag() {
        Assertions.assertEquals(1, MoveCoder.getCaptureFlag(0b1100000000100011110011));
        Assertions.assertEquals(0, MoveCoder.getCaptureFlag(0b1010100001100000111011));
        Assertions.assertEquals(0, MoveCoder.getCaptureFlag(0));
    }

    @Test
    void testGetDPPFlag() {
        Assertions.assertEquals(0, MoveCoder.getDPPFlag(0b100000000100011110011));
        Assertions.assertEquals(1, MoveCoder.getDPPFlag(0b1010100001100000111011));
        Assertions.assertEquals(0, MoveCoder.getDPPFlag(0));
    }

    @Test
    void testGetEnPassantFlag() {
        Assertions.assertEquals(0, MoveCoder.getEnPassantFlag(0b100100000000100011110011));
        Assertions.assertEquals(1, MoveCoder.getEnPassantFlag(0b11010100001100000111011));
        Assertions.assertEquals(0, MoveCoder.getEnPassantFlag(0));
    }

    @Test
    void testGetCastlingFlag() {
        Assertions.assertEquals(0, MoveCoder.getCastlingFlag(0b100000000100011110011));
        Assertions.assertEquals(1, MoveCoder.getCastlingFlag(0b111010100001100000111011));
        Assertions.assertEquals(0, MoveCoder.getCastlingFlag(0));
    }
}
