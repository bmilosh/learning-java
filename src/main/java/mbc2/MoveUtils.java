package mbc2;

public class MoveUtils {
    public static boolean isSquareAttacked(int square, int side) {
        /*
         * Determines if 'square' is attacked by white (side '0')
         * or black (side '1')
         */
        int offset = (side == 0) ? 0 : 6; // 0 for White, 6 for Black
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
}
