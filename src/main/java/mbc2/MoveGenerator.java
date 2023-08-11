package mbc2;

import java.util.ArrayList;

public class MoveGenerator {
    public static void getNoCapturePawnMoves(
        int source,
        int squareOffset,
        int ownColour,
        int piece,
        ArrayList<Integer> moveList
    ) {
        /*
        *  Gets pawn moves on a given square and for a given side.

            The idea is the following:
                - check if pawn can move one square forward.
                    if not, no move is possible and the LHS
                    of the multiplier resolves to zero.
                - check if double pawn push is possible
                - return list of human-readable designations
                    of legal moves
            :Inputs:
                - offset +8 if black else -8
        */
        long move = ((1 - (1 & (Config.OCCUPANCIES[2] >> (source + squareOffset)))) *
                            (Config.PAWN_MOVES_MASKS[ownColour][source] & ~Config.OCCUPANCIES[2]));
        while (move != 0) {
            int target = BitBoard.getLSBIndex(move);
            move &= ~Long.lowestOneBit(move);
            if ((target >= 0 && target < 8) || (target >= 56 && target < 64)) {
                // We're promoting the pawn.
                for (int num = 10; num >= 7; num--) {
                    int encodedMove = MoveCoder.encodeMove(source, target, piece, num, 0, 0, 0, 0);
                    moveList.add(encodedMove);
                }
            } 
            else {
                switch (Math.abs(target - source)) {
                    case 16:
                        /* 
                         * We're making a double pawn push.
                         * Shouldn't we update en passant square here (not doing it yet)?
                         * (config.ENPASSANT_SQUARE = config.SQUARES[target_square - offset])
                         */
                        int encodedMove = MoveCoder.encodeMove(source, target, piece, 0, 0, 1, 0, 0);
                        moveList.add(encodedMove);
                        break;
                        
                    default:
                        // Regular pawn push
                        encodedMove = MoveCoder.encodeMove(source, target, piece, 0, 0, 0, 0, 0);
                        moveList.add(encodedMove);
                        break;
                }
            }
        }
    }
}
