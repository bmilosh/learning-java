package mbc2;

public class ZobristHashing {
    private Config config;

    public ZobristHashing (Config config) {
        this.config = config;
    }

    public long generateHashKey() {
        long hashKey = 0L;

        // start with the pieces
        long bitboard;
        for (int num = 0; num < 12; num++) {
            bitboard = this.config.PIECE_BITBOARDS[num];
            // PrintUtils.printBitBoard(bitboard);
            while (bitboard != 0) {
                int square = BitBoard.getLSBIndex(bitboard);
                bitboard &= ~Long.lowestOneBit(bitboard);
                // hash piece
                hashKey ^= Config.PIECE_KEYS[num][square];
            }
        }

        // move to en passant
        if (!this.config.ENPASSANT_SQUARE.equals("no_square")) {
            // hash en passant
            hashKey ^= Config.ENPASSANT_KEYS[Config.BOARDSQUARES.get(this.config.ENPASSANT_SQUARE)];
        }

        // hash castling right
        hashKey ^= Config.CASTLING_KEYS[this.config.CASTLING_RIGHT];

        // hash side
        if (this.config.SIDE_TO_MOVE == 'b') {
            hashKey ^= Config.SIDE_KEY;
        }

        return hashKey;
    }
}
