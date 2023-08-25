package mbc2;

import java.util.ArrayList;

public class Evaluator {
    private Config config;
    private MoveGenerator moveGenerator;
    private MoveUtils moveUtils;
    public int ply = 0;
    public int bestMove = 0;

    /*
    * most valuable victim & less valuable attacker
                            
        (Victims) Pawn Knight Bishop   Rook  Queen   King
      (Attackers)
            Pawn   105    205    305    405    505    605
          Knight   104    204    304    404    504    604
          Bishop   103    203    303    403    503    603
            Rook   102    202    302    402    502    602
           Queen   101    201    301    401    501    601
            King   100    200    300    400    500    600
    */
    // Access as MVV_LVA_TABLE[attacker_piece_index][victim_piece_index]
    public static int[][] MVV_LVA = {
        {105, 205, 305, 405, 505, 605,  105, 205, 305, 405, 505, 605},
        {104, 204, 304, 404, 504, 604,  104, 204, 304, 404, 504, 604},
        {103, 203, 303, 403, 503, 603,  103, 203, 303, 403, 503, 603},
        {102, 202, 302, 402, 502, 602,  102, 202, 302, 402, 502, 602},
        {101, 201, 301, 401, 501, 601,  101, 201, 301, 401, 501, 601},
        {100, 200, 300, 400, 500, 600,  100, 200, 300, 400, 500, 600},
    
        {105, 205, 305, 405, 505, 605,  105, 205, 305, 405, 505, 605},
        {104, 204, 304, 404, 504, 604,  104, 204, 304, 404, 504, 604},
        {103, 203, 303, 403, 503, 603,  103, 203, 303, 403, 503, 603},
        {102, 202, 302, 402, 502, 602,  102, 202, 302, 402, 502, 602},
        {101, 201, 301, 401, 501, 601,  101, 201, 301, 401, 501, 601},
        {100, 200, 300, 400, 500, 600,  100, 200, 300, 400, 500, 600}
    };

    // Accessed as KILLER_MOVES[id (not more than 2)][ply (we chose a max of 64 plies)]
    private int[][] KILLER_MOVES = new int[2][64];
    // Accessed as HISTORY_MOVES[piece_index][square]
    private int[][] HISTORY_MOVES = new int[12][64];
    public int ORIGINAL_DEPTH;

    /*
     *  ################################################
        ##                                            ##
        ##           Principal variation              ##
        ##                                            ##
        ################################################

      ================================
            Triangular PV table
      --------------------------------
        PV line: e2e4 e7e5 g1f3 b8c6
      ================================

           0    1    2    3    4    5
      
      0    m1   m2   m3   m4   m5   m6
      
      1    0    m2   m3   m4   m5   m6 
      
      2    0    0    m3   m4   m5   m6
      
      3    0    0    0    m4   m5   m6
       
      4    0    0    0    0    m5   m6
      
      5    0    0    0    0    0    m6

     */

    private int[] PV_LENGTH = new int[Config.MAX_PLY];
    private int[][] PV_TABLE = new int[Config.MAX_PLY][Config.MAX_PLY];

    // For PV scoring
    private boolean FOLLOW_PV = false;
    private boolean SCORE_PV = false;


    public Evaluator(Config config, MoveGenerator moveGenerator, MoveUtils moveUtils) {
        this.config = config;
        this.moveGenerator = moveGenerator;
        this.moveUtils = moveUtils;
    }

    public void searchPosition(int depth) {
        // For iterative deepening, we reinitialise these variables
        this.KILLER_MOVES = new int[2][64];
        this.HISTORY_MOVES = new int[64][64];
        this.PV_LENGTH = new int[Config.MAX_PLY];
        this.PV_TABLE = new int[Config.MAX_PLY][Config.MAX_PLY];
        this.config.LEAF_NODES = 0;
        this.FOLLOW_PV = false;
        this.SCORE_PV = false;

        // Iterative deepening
        int currentDepth = 1;
        while (currentDepth <= depth) {
            // Can remove this later
            // this.config.LEAF_NODES = 0;

            // Enable PV following
            this.FOLLOW_PV = true;

            int score = negamax(-50000, 50000, currentDepth);
            System.out.printf("info score cp %d depth %d nodes %d pv ", score, currentDepth, this.config.LEAF_NODES);

            // loop over moves in a PV line
            for (int num = 0; num < this.PV_LENGTH[0]; num++) {
                // print PV move
                PrintUtils.printMove(this.PV_TABLE[0][num], false);
                System.out.print(" ");
            }
            System.out.println();

            currentDepth++;
        }
        System.out.print("bestmove ");
        PrintUtils.printMove(this.PV_TABLE[0][0], true);
    }

    public void enablePVMoveScoring(ArrayList<Integer> moveList) {
        // First, disable PV following
        this.FOLLOW_PV = false;

        // loop over available moves
        for (int move : moveList) {
            // ensure we hit PV
            if (this.PV_TABLE[0][this.ply] == move) {
                // First, enable move scoring
                this.SCORE_PV = true;
                // Next, enable PV following
                this.FOLLOW_PV = true;
            }
        }
    }

    public int scoreMove(int move) {
        // If PV scoring is allowed
        if (this.SCORE_PV) {
            if (this.PV_TABLE[0][this.ply] == move) {
                // disable PV scoring
                this.SCORE_PV = false;
                // give PV move the highest score so it gets searched first first
                return 20000;
            }
        }
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
            return MVV_LVA[MoveCoder.getMovingPiece(move)][target] + 10000;
        }
        // score quiet moves
        else {
            // score 1st killer move
            if (this.KILLER_MOVES[0][this.ply] == move) return 9000;
            // score 2nd killer move
            else if (this.KILLER_MOVES[1][this.ply] == move) return 8000;
            // score history move
            else return this.HISTORY_MOVES[MoveCoder.getMovingPiece(move)][MoveCoder.getTargetSquare(move)];
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
        // init found_PV flag
        boolean found_PV = false;
        // init PV length 
        this.PV_LENGTH[this.ply] = this.ply;

        if (depth == 0) {
            return quiescenceSearch(alpha, beta);
        }

        // avoid overflow
        if (this.ply >= Config.MAX_PLY) return evaluatePosition();

        this.config.LEAF_NODES++;

        int score = 0;
        int legalMoves = 0;

        int ownColour = Config.COLOURS.get(this.config.SIDE_TO_MOVE);
        boolean inCheck = this.moveUtils.isKingUnderCheck(
                    BitBoard.getLSBIndex(this.config.PIECE_BITBOARDS[Config.PIECES.get('K') + (6 * ownColour)]), 
                    ownColour ^ 1
        );
        if (inCheck) depth++;

        BoardState boardState = new BoardState(this.config);

        ArrayList<Integer> moveList = this.moveGenerator.generateMoves();
        // score PV before sorting
        if (this.FOLLOW_PV) {
            // enable scoring of PV moves
            enablePVMoveScoring(moveList);
        }
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

            // Principal Variation Search (PVS)
            if (found_PV) {
                /* "Once you've found a move with a score that is between alpha and beta,
                the rest of the moves are searched with the goal of proving that they are all bad.
                It's possible to do this a bit faster than a search that worries that one
                of the remaining moves might be good."
                From CMK himself.
                */         
                score = -negamax(-alpha - 1, -alpha, depth - 1);  

                /* "If the algorithm finds out that it was wrong, and that one of the
                subsequent moves was better than the first PV move, it has to search again,
                in the normal alpha-beta manner.  This happens sometimes, and it's a waste of time,
                but generally not often enough to counteract the savings gained from doing the
                "bad move proof" search referred to earlier." 
                From CMK himself.
                */ 

                /*
                    # "We expect all other moves to come back with a fail low (score <= alpha).
                    # The reason for this assumption is that we trust our move ordering.
                    # But if that search comes back with a (score > alpha) then it failed high and is probably
                    # a new best score. But only if it is also (score < beta), otherwise it is a real fail high.
                    # If it is inside our window we re-search it now and give it a new chance.
                    # Re-searches can be very fast especially when we use a transposition table.
                    # Then the PVS assumption continues, probably with a new best move."
                    # from Harald Luessen's comment on this video from Code Monkey King's BBC series
                    # https://www.youtube.com/watch?v=Gs4Zk6aihyQ&list=PLmN0neTso3Jxh8ZIylk74JpwfiWNI76Cs&index=62
                 */
                if ((score > alpha) && (score < beta)) {
                    score = -negamax(-beta, -alpha, depth - 1);
                }          
            }
            else {
                // for all other types of nodes (moves), do normal alpha-beta search
                score = -negamax(-beta, -alpha, depth - 1);
            }
            

            this.ply--;

            // restore board state
            boardState.restoreBoardState();

            // fail-hard beta cutoff
            if (score >= beta) {
                // on quiet moves
                if (MoveCoder.getCaptureFlag(move) == 0) {
                    // store killer moves
                    this.KILLER_MOVES[1][this.ply] = this.KILLER_MOVES[0][this.ply];
                    this.KILLER_MOVES[0][this.ply] = move;
                }
                // node (move) fails high
                return beta;
            }
            // better move
            if (score > alpha) {
                if (MoveCoder.getCaptureFlag(move) == 0) {
                    // store history move
                    this.HISTORY_MOVES[MoveCoder.getMovingPiece(move)][MoveCoder.getTargetSquare(move)] += depth;
                }
                // PV node
                alpha = score;

                // enable found PV flag
                found_PV = true;

                // write PV move
                this.PV_TABLE[this.ply][this.ply] = move;

                // loop over the next plies
                for (int nextPly = this.ply + 1; nextPly < this.PV_LENGTH[this.ply + 1]; nextPly++) {
                    // copy move from deeper ply into a current ply's line
                    this.PV_TABLE[this.ply][nextPly] = this.PV_TABLE[this.ply + 1][nextPly];
                }
                // adjust PV length
                this.PV_LENGTH[this.ply] = this.PV_LENGTH[this.ply + 1];
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
