package mbc2;

import java.util.ArrayList;

public class Perft {
    // private int captures = 0;
    // private int dpp = 0;
    // private int enpassant = 0;
    // private int castling = 0;
    private long LEAF_NODES = 0L;
    private Config config;
    private MoveUtils MoveUtils;
    private MoveGenerator MoveGenerator;

    public Perft(Config Config, MoveUtils MoveUtils, MoveGenerator MoveGenerator) {
        this.config = Config;
        this.MoveUtils = MoveUtils;
        this.MoveGenerator = MoveGenerator;
    }

    public void perftDriver(int depth) {
        if (depth == 0) {
            // this.config.LEAF_NODES++;
            LEAF_NODES++;
            return;
        }
        BoardState boardState = new BoardState(this.config);
        
        ArrayList<Integer> moveList = MoveGenerator.generateMoves();
        for (int move : moveList) {
            // preserve board state
            boardState.copyBoardState();
            // make move
            if (!MoveUtils.makeMove(move, false)) {
                continue;
            }
            // captures += MoveCoder.getCaptureFlag(move);
            // dpp += MoveCoder.getDPPFlag(move);
            // enpassant += MoveCoder.getEnPassantFlag(move);
            // castling += MoveCoder.getCastlingFlag(move);
            perftDriver(depth - 1);
            boardState.restoreBoardState();
        }
    }

    public void perftTest(int depth) {
        BoardState boardState = new BoardState(this.config);
        boardState.copyBoardState();

        long start = TimeUtility.getTimeMs();

        ArrayList<Integer> moveList = MoveGenerator.generateMoves();

        for (int move : moveList) {
            String moveString = PrintUtils.moveToString(move);
            // if (!moveString.equals("e1g1") && !moveString.equals("e1c1")) continue;
            // preserve board state
            boardState.copyBoardState();
            // make move
            if (!MoveUtils.makeMove(move, false)) {
                continue;
            }
            long cumNodes = LEAF_NODES;
            // int cumNodes = this.config.LEAF_NODES;
            perftDriver(depth - 1);
            long oldNodes = LEAF_NODES - cumNodes;
            // int oldNodes = this.config.LEAF_NODES - cumNodes;
            // int promotedPiece = MoveCoder.getPromotedPiece(move);
            // char pp = ' ';
            // if (promotedPiece != 0) {
            //     pp = Config.ASCII_PIECES[promotedPiece];
            // }
            System.out.printf("     %s nodes: %d\n", moveString, oldNodes);
            // System.out.printf("     %s%s%s nodes: %d\n", source, target, pp, oldNodes);

            boardState.restoreBoardState();
        }
        boardState.restoreBoardState();
        long ttaken = TimeUtility.getTimeMs() - start;
        double ttsecs = (double) ttaken / 1000;
        System.out.println();
        // System.out.printf("     Captures: %d\n", captures);
        // System.out.printf("     Double PP: %d\n", dpp);
        // System.out.printf("     En passant: %d\n", enpassant);
        // System.out.printf("     Castling: %d\n", castling);
        System.out.printf("     Depth: %d\n", depth);
        System.out.printf("     Nodes: %d\n", LEAF_NODES);
        // System.out.printf("     Nodes: %d\n", this.config.LEAF_NODES);
        System.out.printf("     Time: %dms\n", ttaken);
        System.out.printf("     Nodes / sec: %d\n", (int)(LEAF_NODES / ttsecs));
        // System.out.printf("     Nodes / sec: %d\n", (int)(this.config.LEAF_NODES / ttsecs));
    }
}
