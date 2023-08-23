package mbc2;

import java.util.ArrayList;

public class Evaluator {
    private Config config;
    private MoveGenerator moveGenerator;
    private MoveUtils moveUtils;
    public int ply = 0;
    public int bestMove = 0;

    public Evaluator(Config config, MoveGenerator moveGenerator, MoveUtils moveUtils) {
        this.config = config;
        this.moveGenerator = moveGenerator;
        this.moveUtils = moveUtils;
    }

    public void searchPosition(int depth) {
        int score = negamax(-50000, 50000, depth);
        if (this.bestMove != 0) {
            System.out.printf("info score cp %d depth %d nodes %d\n", score, depth, this.config.LEAF_NODES);
            System.out.println("bestmove " + PrintUtils.moveToString(this.bestMove));
        }
    }

    public int quiescenceSearch(int alpha, int beta) {
        int evaluation = evaluatePosition();

        // fail-hard (beta cutoff)
        if (evaluation >= beta) {
            return beta;
        }
        if (evaluation > alpha) {
            alpha = evaluation;
        }

        int score;

        BoardState boardState = new BoardState(this.config);

        ArrayList<Integer> moveList = this.moveGenerator.generateMoves();
        for (int move : moveList) {
            // preserve current board state
            boardState.copyBoardState();
            
            this.ply++;

            if (!this.moveUtils.makeMove(move, true)) {
                this.ply--;
                continue;
            }
            score = -quiescenceSearch(-beta, -alpha);

            this.ply--;

            // restore board state
            boardState.restoreBoardState();

            // fail-hard beta cutoff
            if (score >= beta) {
                // node (move) fails high
                return beta;
            }
            // better move
            if (score > alpha) {
                // PV node
                alpha = score;
            }
        }

        // fail high
        return alpha;
    }

    public int evaluatePosition() {
        // init score
        int score = 0;
        for (int idx = 0; idx < 12; idx++) {
            long bitboard = this.config.PIECE_BITBOARDS[idx];
            char piece = Config.ASCII_PIECES[idx];
            while (bitboard != 0) {
                int square = BitBoard.getLSBIndex(bitboard);
                int rank = square / 8;
                int file = square % 8;
                bitboard &= ~Long.lowestOneBit(bitboard);
                score += Config.MATERIAL_SCORES[idx];
                // add positional scores
                if (piece == 'Q' || piece == 'q') continue;
                if (Character.isUpperCase(piece)) {
                    score += Config.POSITIONAL_PIECE_SCORES[idx][square];
                } else {
                    score -= Config.POSITIONAL_PIECE_SCORES[idx % 6][(8 * (7 - rank)) + file];
                }
            }
        }
        return (this.config.SIDE_TO_MOVE == 'w') ? score : -score;
    }

    public int negamax(int alpha, int beta, int depth) {
        if (depth == 0) {
            return quiescenceSearch(alpha, beta);
        }
        this.config.LEAF_NODES++;
        int score;
        int bestSoFar = 0;
        int oldAlpha = alpha;
        int legalMoves = 0;
        int ownColour = Config.COLOURS.get(this.config.SIDE_TO_MOVE);
        boolean inCheck = this.moveUtils.isKingUnderCheck(
                    BitBoard.getLSBIndex(this.config.PIECE_BITBOARDS[Config.PIECES.get('K') + (6 * ownColour)]), 
                    ownColour ^ 1
        );

        BoardState boardState = new BoardState(this.config);

        ArrayList<Integer> moveList = this.moveGenerator.generateMoves();
        for (int move : moveList) {
            // preserve current board state
            boardState.copyBoardState();
            
            this.ply++;

            if (!this.moveUtils.makeMove(move, false)) {
                this.ply--;
                continue;
            }
            legalMoves++;
            score = -negamax(-beta, -alpha, depth - 1);

            this.ply--;

            // restore board state
            boardState.restoreBoardState();

            // fail-hard beta cutoff
            if (score >= beta) {
                // node (move) fails high
                return beta;
            }
            // better move
            if (score > alpha) {
                // PV node
                alpha = score;
                // root node
                if (this.ply == 0) {
                    // current best move
                    bestSoFar = move;
                }
            }
        }
        if (legalMoves == 0) {
            // if in check, return mating score (assuming closest distance to mating position)
            if (inCheck) return -49000 + this.ply;
            // else return stalemate score
            else return 0;
        }
        if (oldAlpha != alpha) {
            this.bestMove = bestSoFar;
        }

        // node (move) fails low
        return alpha;
    }
}
