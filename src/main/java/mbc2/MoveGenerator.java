package mbc2;

import java.util.ArrayList;

public class MoveGenerator {
    public static void getNonCapturePawnMoves(
        int source,
        int squareOffset,
        int ownColour,
        int piece,
        ArrayList<Integer> moveList
    ) {
        /*
        *  Gets non-capture pawn moves on a given square and for a given side.

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

    public static void getAllPawnMoves(
        long pawnBitboard,
        int squareOffset,
        int ownColour,
        int piece,
        ArrayList<Integer> moveList
    ) {
        /*
         * Generates ALL possible pawn moves for a given side and appends each to 
         * the moveList. Capture moves are generated here while non-capture moves
         * are generated by 'getNonCapturePawnMoves'.
            :Inputs:
                - offset +8 if black else -8
         */
        while (pawnBitboard != 0) {
            int source = BitBoard.getLSBIndex(pawnBitboard);
            pawnBitboard &= ~Long.lowestOneBit(pawnBitboard);
            long captures = Config.PAWN_ATTACKS[ownColour][source] & Config.OCCUPANCIES[ownColour ^ 1];
            // Check en passant capture
            if ((Config.ENPASSANT_SQUARE != "no_square") && 
                (Config.PAWN_ATTACKS[ownColour][source] & 
                (1L << Config.BOARDSQUARES.get(Config.ENPASSANT_SQUARE))) != 0) {
                    int encodedMove = MoveCoder.encodeMove(source, 
                                        Config.BOARDSQUARES.get(Config.ENPASSANT_SQUARE), 
                                        piece, 0, 1, 0, 
                                        1, 0);
                    moveList.add(encodedMove);
                }
            
            while (captures != 0) {
                int target = BitBoard.getLSBIndex(captures);
                captures &= ~Long.lowestOneBit(captures);
                // Check if it's a promotion-capture
                if ((target >= 0 && target < 8) || (target >= 56 && target < 64)) {
                    for (int num = 10; num >= 7; num--) {
                        int encodedMove = MoveCoder.encodeMove(source, target, piece, num, 1,
                             0, 0, 0);
                        moveList.add(encodedMove);
                    }
                } else {
                    // Regular capture
                    int encodedMove = MoveCoder.encodeMove(source, target, piece, 0, 1, 0, 0, 0);
                    moveList.add(encodedMove);
                }
            }
            // Now we get the non-capture moves for this pawn
            getNonCapturePawnMoves(source, squareOffset, ownColour, piece, moveList);
        }
    }

    public static void getNonPawnMoves(
        int source,
        int ownColour,
        long attacks,
        int piece,
        ArrayList<Integer> moveList
    ) {
        long nonCaptureMoves = attacks & ~Config.OCCUPANCIES[2];
        long captureMoves = attacks & Config.OCCUPANCIES[ownColour ^ 1];

        // Process non-capture moves
        while (nonCaptureMoves != 0) {
            int target = BitBoard.getLSBIndex(nonCaptureMoves);
            nonCaptureMoves &= ~Long.lowestOneBit(nonCaptureMoves);
            int encodedMove = MoveCoder.encodeMove(source, target, piece, 0, 0, 0, 0, 0);
            moveList.add(encodedMove);
        }
        // Process captures
        while (captureMoves != 0) {
            int target = BitBoard.getLSBIndex(captureMoves);
            captureMoves &= ~Long.lowestOneBit(captureMoves);
            int encodedMove = MoveCoder.encodeMove(source, target, piece, 0, 1, 0, 0, 0);
            moveList.add(encodedMove);            
        }
    }

    public static boolean canCastle(char castleToSide, int kingPosition, int opponentColour) {
        /*
         *  :Inputs:
                - castleToSide = 'K' if white else 'k' for kingside castling and 'Q' or 'q' if queenside

            :Outputs:
                - boolean indicating ability to castle to indicated side

            IMPORTANT: This method assumes that castling rights have been set up properly!

            Castling rules: 'Castling is permitted only if neither the king nor the rook
            has previously moved (we're not checking these in this method); the squares
            between the king and the rook are vacant; and the king does not leave (e),
            cross over (d or f), or finish (c or g) on a square attacked by an enemy piece.'

            Source: 'https://en.wikipedia.org/wiki/Castling'
            (Words in parentheses mine)

         */
        // if ((Config.CASTLING_RIGHT & Config.CASTLING.get(king)) == 0) return false;
        // boolean notKingUnderCheck = !MoveUtils.isKingUnderCheck(kingPosition, opponentColour);
        boolean isKingSide = Character.toLowerCase(castleToSide) == 'k';
        long emptySquares = isKingSide ? 0b11L : 0b111L;
        int leftShift = isKingSide ? kingPosition + 1 : kingPosition - 3;
        int squareBesideKing = isKingSide ? kingPosition + 1 : kingPosition - 1;
        
        return (
            (Config.CASTLING_RIGHT & Config.CASTLING.get(castleToSide)) != 0 && // player has appropriate castling right
            !MoveUtils.isKingUnderCheck(kingPosition, opponentColour) &&
            !MoveUtils.isSquareAttacked(squareBesideKing, opponentColour) &&    // king isn't crossing an attacked square
            ((emptySquares << leftShift) & Config.OCCUPANCIES[2]) == 0          // squares between the king and the rook are vacant
            // !MoveUtils.isSquareAttacked(kingPosition + 1, opponentColour) &&    // king isn't crossing an attacked square
            // ((3L << kingPosition + 1) & Config.OCCUPANCIES[2]) == 0          // squares between the king and the rook are vacant
        );
    }
}
