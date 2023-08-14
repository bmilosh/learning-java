package mbc2;

public class MoveUtils {
    public static boolean isSquareAttacked(int square, int side) {
        /*
         * Determines if 'square' is attacked by white (side '0')
         * or black (side '1')
         */
        int offset = (side == 0) ? 0 : 6; // 0 for White, 6 for Black
        // System.out.println(side + " " + square);
        return (
            (Config.PAWN_ATTACKS[side ^ 1][square] & Config.PIECE_BITBOARDS[offset])
            | (Config.KNIGHT_ATTACKS[square] & Config.PIECE_BITBOARDS[offset + 1])
            | (AttacksGenerator.getBishopAttacks(square, Config.OCCUPANCIES[2]) & Config.PIECE_BITBOARDS[offset + 2])
            | (AttacksGenerator.getRookAttacks(square, Config.OCCUPANCIES[2]) & Config.PIECE_BITBOARDS[offset + 3])
            | (AttacksGenerator.getQueenAttacks(square, Config.OCCUPANCIES[2]) & Config.PIECE_BITBOARDS[offset + 4])
            | (Config.KING_ATTACKS[square] & Config.PIECE_BITBOARDS[offset + 5])
        ) != 0;
    }

    public static boolean isKingUnderCheck(int square, int opponentColour) {
        return isSquareAttacked(square, opponentColour);
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

    public static boolean makeMove(int move, boolean onlyCaptures) {
        if (!onlyCaptures) {
            // We first need to preserve current board state in case we need
            // to take it back
            BoardState boardState = new BoardState();
            boardState.copyBoardState();

            char king = Config.SIDE_TO_MOVE == 'w' ? 'K' : 'k';
            int ownColour = Config.COLOURS.get(Config.SIDE_TO_MOVE);
            int captureOffset = (king == 'K') ? 6 : 0;
            int doublePPOffset = (king == 'K') ? 8 : -8;

            // Parse move
            int source = MoveCoder.getSourceSquare(move);
            int target = MoveCoder.getTargetSquare(move);
            int piece = MoveCoder.getMovingPiece(move);
            int promotedPiece = MoveCoder.getPromotedPiece(move);
            int capture = MoveCoder.getCaptureFlag(move);
            int doublePP = MoveCoder.getDPPFlag(move);
            int enPassant = MoveCoder.getEnPassantFlag(move);
            int castling = MoveCoder.getCastlingFlag(move);

            // Update moving piece bitboard
            Config.PIECE_BITBOARDS[piece] = BitBoard.popBitAtIndex(
                BitBoard.setBitAtIndex(Config.PIECE_BITBOARDS[piece], target), source
            );

            // Update occupancy bitboard for the current side
            Config.OCCUPANCIES[ownColour] = BitBoard.popBitAtIndex(
                BitBoard.setBitAtIndex(Config.OCCUPANCIES[ownColour], target), source
            );

            // Handle promotion
            if (promotedPiece != 0) {
                // First, remove the pawn that we set above in its bitboard
                Config.PIECE_BITBOARDS[piece] = BitBoard.popBitAtIndex(
                    Config.PIECE_BITBOARDS[piece], target
                );
                // Now, set the piece we're promoting to
                Config.PIECE_BITBOARDS[promotedPiece] = BitBoard.setBitAtIndex(
                    Config.PIECE_BITBOARDS[promotedPiece], target
                );
            }

            // Handle capture
            if (capture != 0) {
                for (int num = 0; num < 6; num++) {
                    int capturedPiece = num + captureOffset;
                    if (BitBoard.getBitAtIndex(Config.PIECE_BITBOARDS[capturedPiece], target) != 0) {
                        // This was the piece we captured. We pop
                        // its bit from its piece bitboard
                        Config.PIECE_BITBOARDS[capturedPiece] = BitBoard.popBitAtIndex(
                            Config.PIECE_BITBOARDS[capturedPiece], target
                        );
                        // Update occupancies for the side whose piece was captured
                        Config.OCCUPANCIES[ownColour ^ 1] = BitBoard.popBitAtIndex(
                            Config.OCCUPANCIES[ownColour ^ 1], target
                        );
                        break;
                    }
                }
            }

            // Handle castling
            if (castling != 0) {
                // check which side we're castling to and initialise
                // some needed variables
                int rook = (king == 'K') ? Config.PIECES.get('R') : Config.PIECES.get('r');
                int currentRookPosOffset = (target > source) ? 1 : -2;
                int newRookPosOffset = (target > source) ? 1 : -1;
                /*
                 * Update the appropriate side's rook bitboard:
                 * Pop it from the edge square and set it in the castled square
                 */
                Config.PIECE_BITBOARDS[rook] = BitBoard.popBitAtIndex(
                    BitBoard.setBitAtIndex(Config.PIECE_BITBOARDS[rook], target - newRookPosOffset), 
                    target + currentRookPosOffset
                );

                // Update occupancies taking into account the rook movement
                Config.OCCUPANCIES[ownColour] = BitBoard.popBitAtIndex(
                    BitBoard.setBitAtIndex(Config.OCCUPANCIES[ownColour], target - newRookPosOffset), 
                    target + currentRookPosOffset
                );
            }
            /*
             *  # We update castling right after every move.
                # Castling right only changes if at least one of
                # source and target squares is a rooks' or kings'
                # starting square.
             */
            Config.CASTLING_RIGHT &= Config.CASTLING_RIGHT_LOOKUP_TABLE[source] & Config.CASTLING_RIGHT_LOOKUP_TABLE[target];

            // Handle en passant capture
            if (enPassant != 0) {
                /*
                 * We make sure to remove the pawn that has now
                 * been captured through en passant
                 */
                Config.PIECE_BITBOARDS[captureOffset] = BitBoard.popBitAtIndex(
                    Config.PIECE_BITBOARDS[captureOffset], target + doublePPOffset
                );
                /*
                 * Update occupancies for the other side taking
                 * into account the en passant capture
                 */
                Config.OCCUPANCIES[ownColour ^ 1] = BitBoard.popBitAtIndex(
                    Config.OCCUPANCIES[ownColour ^ 1], target + doublePPOffset
                );
            }
            /*
             *  # As long as we're not making a double pawn push,
                # we always reset the en passant square to empty.
                # This is because, per the rules, en passant capture
                # is only available for the very next move after
                # a double pawn push.
             */
            Config.ENPASSANT_SQUARE = "no_square";

            // Handle double pawn push
            if (doublePP != 0) {
                // We set the en passant square
                Config.ENPASSANT_SQUARE = Config.SQUARES[target + doublePPOffset];
            }

            // We update the occupancy for both sides
            Config.OCCUPANCIES[2] = Config.OCCUPANCIES[0] | Config.OCCUPANCIES[1];

            // Update side to move
            Config.SIDE_TO_MOVE = Config.SIDES[ownColour ^ 1];

            // Check if the move just registered leaves the king in check
            long kingBitboard = Config.PIECE_BITBOARDS[Config.PIECES.get(king)];
            int kingPosition = BitBoard.getLSBIndex(kingBitboard);
            // System.out.println(kingPosition);
            // System.out.println(Config.PIECES.get(king));

            if (isSquareAttacked(kingPosition, ownColour ^ 1)) {
                boardState.restoreBoardState();
                return false;
            } else {
                return true;
            }
        }
        else {
            if (MoveCoder.getCaptureFlag(move) != 0) {
                return makeMove(move, false);
            }
            return false;
        }
    }
}
