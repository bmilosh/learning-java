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
    private int CASTLING_RIGHT_COPY, MOVE_COUNT_COPY;
    private String ENPASSANT_SQUARE_COPY;

    public void copyBoardState() {
        this.PIECE_BITBOARDS_COPY = Config.PIECE_BITBOARDS.clone();
        this.OCCUPANCIES_COPY = Config.OCCUPANCIES.clone();
        this.SIDE_TO_MOVE_COPY = Config.SIDE_TO_MOVE;
        this.CASTLING_RIGHT_COPY = Config.CASTLING_RIGHT;
        this.ENPASSANT_SQUARE_COPY = Config.ENPASSANT_SQUARE;
    }

    public void restoreBoardState() {
        Config.PIECE_BITBOARDS = this.PIECE_BITBOARDS_COPY;
        Config.OCCUPANCIES = this.OCCUPANCIES_COPY;
        Config.SIDE_TO_MOVE = this.SIDE_TO_MOVE_COPY;
        Config.CASTLING_RIGHT = this.CASTLING_RIGHT_COPY;
        Config.ENPASSANT_SQUARE = this.ENPASSANT_SQUARE_COPY;
    }
}
