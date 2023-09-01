package mbc2;

public class BoardState {
    /*
     * Whenever we want to copy the current board state:
     * 
     * BoardState boardState = new BoardState();
     * boardState.copyBoardState();
     * 
     * And to restore:
     * 
     * boardState.restoreBoardState();
     */
    private long[] PIECE_BITBOARDS_COPY;
    private long[] OCCUPANCIES_COPY;
    private char SIDE_TO_MOVE_COPY;
    private int CASTLING_RIGHT_COPY;
    private String ENPASSANT_SQUARE_COPY;
    private long HASH_KEY_COPY;
    private Config Config;

    public BoardState(Config Config) {
        this.Config = Config;
    }

    public void copyBoardState() {
        this.PIECE_BITBOARDS_COPY = this.Config.PIECE_BITBOARDS.clone();
        this.OCCUPANCIES_COPY = this.Config.OCCUPANCIES.clone();
        this.SIDE_TO_MOVE_COPY = this.Config.SIDE_TO_MOVE;
        this.CASTLING_RIGHT_COPY = this.Config.CASTLING_RIGHT;
        this.ENPASSANT_SQUARE_COPY = this.Config.ENPASSANT_SQUARE;
        this.HASH_KEY_COPY = this.Config.HASH_KEY;
    }

    public void restoreBoardState() {
        this.Config.PIECE_BITBOARDS = this.PIECE_BITBOARDS_COPY;
        this.Config.OCCUPANCIES = this.OCCUPANCIES_COPY;
        this.Config.SIDE_TO_MOVE = this.SIDE_TO_MOVE_COPY;
        this.Config.CASTLING_RIGHT = this.CASTLING_RIGHT_COPY;
        this.Config.ENPASSANT_SQUARE = this.ENPASSANT_SQUARE_COPY;
        this.Config.HASH_KEY = this.HASH_KEY_COPY;
    }
}
