package mbc2;

import java.util.ArrayList;

public class Perft {
    public static int captures = 0;
    public static int dpp = 0;
    public static int enpassant = 0;
    public static int castling = 0;

    public static void perftDriver(int depth) {
        if (depth == 0) {
            Config.LEAF_NODES++;
            return;
        }
        BoardState boardState = new BoardState();
        
        ArrayList<Integer> moveList = MoveGenerator.generateMoves();
        for (int move : moveList) {
            // preserve board state
            boardState.copyBoardState();
            // make move
            if (!MoveUtils.makeMove(move, false)) {
                continue;
            }
            captures += MoveCoder.getCaptureFlag(move);
            dpp += MoveCoder.getDPPFlag(move);
            enpassant += MoveCoder.getEnPassantFlag(move);
            castling += MoveCoder.getCastlingFlag(move);
            perftDriver(depth - 1);
            boardState.restoreBoardState();
        }
    }

    public static void perftTest(int depth) {
        BoardState boardState = new BoardState();
        boardState.copyBoardState();
        long start = TimeUtility.getTimeMs();
        ArrayList<Integer> moveList = MoveGenerator.generateMoves();
        for (int move : moveList) {
            String source = Config.SQUARES[MoveCoder.getSourceSquare(move)];
            String target = Config.SQUARES[MoveCoder.getTargetSquare(move)];
            // preserve board state
            boardState.copyBoardState();
            // make move
            if (!MoveUtils.makeMove(move, false)) {
                continue;
            }
            int cumNodes = Config.LEAF_NODES;
            perftDriver(depth - 1);
            int oldNodes = Config.LEAF_NODES - cumNodes;
            int promotedPiece = MoveCoder.getPromotedPiece(move);
            char pp = ' ';
            if (promotedPiece != 0) {
                pp = Config.ASCII_PIECES[promotedPiece];
            }
            System.out.printf("     %s%s%s nodes: %d\n", source, target, pp, oldNodes);

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
        System.out.printf("     Nodes: %d\n", Config.LEAF_NODES);
        System.out.printf("     Time: %dms\n", ttaken);
        System.out.printf("     Nodes / sec: %d\n", (int)(Config.LEAF_NODES / ttsecs));
    }
}
