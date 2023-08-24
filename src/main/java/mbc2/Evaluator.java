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
        System.out.printf("info score cp %d depth %d nodes %d pv ", score, depth, this.config.LEAF_NODES);

        // loop over moves in a PV line
        for (int num = 0; num < this.config.PV_LENGTH[0]; num++) {
            // print PV move
            PrintUtils.printMove(this.config.PV_TABLE[0][num], false);
            System.out.print(" ");
        }
        System.out.println();
        System.out.print("bestmove ");
        PrintUtils.printMove(this.config.PV_TABLE[0][0], true);
    }

    public int scoreMove(int move) {
        // score capture move
        if (MoveCoder.getCaptureFlag(move) != 0) {
            int target = 0;
            // white captures black pieces, and vice-versa
            int targetOffset = this.config.SIDE_TO_MOVE == 'w' ? 6 : 0;
            int moveTarget = MoveCoder.getTargetSquare(move);
            while (target < 6) {
                long bitboard = this.config.PIECE_BITBOARDS[target + targetOffset];
                if (BitBoard.getBitAtIndex(bitboard, moveTarget) != 0) {
                    // We've found the piece we're capturing.
                    target += targetOffset;
                    break;
                }
                target++;
            }
            // score move by MVV LVA lookup [source piece][target piece]
            return Config.MVV_LVA[MoveCoder.getMovingPiece(move)][target] + 10000;
        }
        // score quiet moves
        else {
            // score 1st killer move
            if (this.config.KILLER_MOVES[0][this.ply] == move) return 9000;
            // score 2nd killer move
            else if (this.config.KILLER_MOVES[1][this.ply] == move) return 8000;
            // score history move
            else return this.config.HISTORY_MOVES[MoveCoder.getMovingPiece(move)][MoveCoder.getTargetSquare(move)];
        }
    }

    public int[] sortMoves(ArrayList<Integer> moveList) {
        int size = moveList.size();
        int[] sortedMoves = new int[size];
        // init score moves array
        int[] moveScores = new int[size];
        for (int idx = 0; idx < size; idx++) {
            moveScores[idx] = scoreMove(moveList.get(idx));
        }
        for (int current = 0; current < size; current++) {
            // check moves ahead of current move
            for (int next = current + 1; next < size; next++) {
                int nMove = sortedMoves[next] == 0 ? moveList.get(next) : sortedMoves[next];
                int cMove = sortedMoves[current] == 0 ? moveList.get(current) : sortedMoves[current];
                int nScore = moveScores[next];
                int cScore = moveScores[current];
                if (cScore < nScore) {
                    // swap moves
                    sortedMoves[current] = nMove;
                    sortedMoves[next] = cMove;
                    // swap scores
                    moveScores[current] = nScore;
                    moveScores[next] = cScore;
                } else {
                    sortedMoves[current] = cMove;
                    sortedMoves[next] = nMove;
                }
            }
        }
        return sortedMoves;
    }

    public void printMoveScore(int[] moveList) {
        System.out.println("        Move scores:\n\n");

        for (int move : moveList) {
            System.out.printf("        move: %s  score: %d\n", PrintUtils.moveToString(move), scoreMove(move));
        }
    }

    public void printMoveScore(ArrayList<Integer> moveList) {
        System.out.println("        Move scores:\n\n");

        for (int move : moveList) {
            System.out.printf("        move: %s  score: %d\n", PrintUtils.moveToString(move), scoreMove(move));
        }
    }

    public int quiescenceSearch(int alpha, int beta) {
        this.config.LEAF_NODES++;

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
        int[] moveList1 = sortMoves(moveList);
        for (int move : moveList1) {
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
        // init PV length 
        this.config.PV_LENGTH[this.ply] = this.ply;

        if (depth == 0) {
            return quiescenceSearch(alpha, beta);
        }

        this.config.LEAF_NODES++;

        int score;
        int legalMoves = 0;

        int ownColour = Config.COLOURS.get(this.config.SIDE_TO_MOVE);
        boolean inCheck = this.moveUtils.isKingUnderCheck(
                    BitBoard.getLSBIndex(this.config.PIECE_BITBOARDS[Config.PIECES.get('K') + (6 * ownColour)]), 
                    ownColour ^ 1
        );
        if (inCheck) depth++;

        BoardState boardState = new BoardState(this.config);

        ArrayList<Integer> moveList = this.moveGenerator.generateMoves();
        int[] moveList1 = sortMoves(moveList);
        for (int move : moveList1) {
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
                // on quiet moves
                if (MoveCoder.getCaptureFlag(move) == 0) {
                    // store killer moves
                    this.config.KILLER_MOVES[1][this.ply] = this.config.KILLER_MOVES[0][this.ply];
                    this.config.KILLER_MOVES[0][this.ply] = move;
                }
                // node (move) fails high
                return beta;
            }
            // better move
            if (score > alpha) {
                if (MoveCoder.getCaptureFlag(move) == 0) {

                    // store history move
                    this.config.HISTORY_MOVES[MoveCoder.getMovingPiece(move)][MoveCoder.getTargetSquare(move)] += depth;
                }
                // PV node
                alpha = score;

                // write PV move
                this.config.PV_TABLE[this.ply][this.ply] = move;

                // loop over the next plies
                for (int nextPly = this.ply + 1; nextPly < this.config.PV_LENGTH[this.ply + 1]; nextPly++) {
                    // copy move from deeper ply into a current ply's line
                    this.config.PV_TABLE[this.ply][nextPly] = this.config.PV_TABLE[this.ply + 1][nextPly];
                }
                // adjust PV length
                this.config.PV_LENGTH[this.ply] = this.config.PV_LENGTH[this.ply + 1];
            }
        }
        if (legalMoves == 0) {
            // if in check, return mating score (assuming closest distance to mating position)
            if (inCheck) return -49000 + this.ply;
            // else return stalemate score
            else return 0;
        }

        // node (move) fails low
        return alpha;
    }
}
