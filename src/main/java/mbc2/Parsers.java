package mbc2;

import java.util.ArrayList;
import java.util.List;

public class Parsers {
    private Config config;
    private BoardState boardState;
    private MoveGenerator moveGen;
    private MoveUtils moveUtils;

    public Parsers(Config Config, BoardState boardState, MoveGenerator moveGen, MoveUtils moveUtils) {
        this.config = Config;
        this.boardState = boardState;
        this.moveGen = moveGen;
        this.moveUtils = moveUtils;
    }

    public void parseFEN(String fenString) {
        /*
         * Note that this method expects that EngineInitMethods.initAll()
         * has been called.
         */
        String[] FENToList = fenString.split(" ");
        // sanitise FEN
        if ((FENToList.length < 4) || (FENToList.length > 6)) {
            // System.out.println("!!!!!!!!!!!!!Invalid FEN string!!!!!!!!!!!!!");
            // System.out.println(fenString);
            // return;
            throw new IllegalArgumentException("!!!!!!!!!!!!!Invalid FEN string!!!!!!!!!!!!!");
        }

        /*
         * First thing to do is to copy board state and then
         * reset all board variables that can be impacted by the
           position from our custom FEN string.
         */
        
        this.boardState.copyBoardState();
        for (int idx = 0; idx < 6; idx++) {
            this.config.PIECE_BITBOARDS[idx] = 0L;       // white pieces
            this.config.PIECE_BITBOARDS[idx + 6] = 0L;   // black pieces
        }
        this.config.OCCUPANCIES[0] = 0L;
        this.config.OCCUPANCIES[1] = 0L;
        this.config.OCCUPANCIES[2] = 0L;
        this.config.CASTLING_RIGHT = 0;
        this.config.ENPASSANT_SQUARE = "no_square";

        // Parse position
        String position = FENToList[0];
        int square = 0;
        int index = 0;
        while (square < 64) {
            char piece = position.charAt(index);
            // System.out.println(piece);
            // System.out.println(Config.PIECES.toString());
            if (Config.PIECES.containsKey(piece)) {
                int idx = Config.PIECES.get(piece);
                this.config.PIECE_BITBOARDS[idx] = BitBoard.setBitAtIndex(
                    this.config.PIECE_BITBOARDS[idx], square
                );
                index++;
                square++;
            } else if (Character.isDigit(piece) && Character.getNumericValue(piece) > 0 && Character.getNumericValue(piece) <= 8) {
                square += Character.getNumericValue(piece);
                index++;
            } else if (piece == '/') {
                index++;
            } else {
                boardState.restoreBoardState();
                throw new IllegalArgumentException(String.format("!!!!! INVALID FEN CHARACTER IN SQUARE REPRESENTATION: %s !!!!!\n", piece));
                // return;
            }

            if ((index == position.length()) && square != 64) {
                boardState.restoreBoardState();
                throw new IllegalArgumentException(String.format("!!!!! INVALID FEN: \"%s\". WRONG NUMBER OF SQUARES !!!!!\n", fenString));
                // return;
            }
        }

        // Parse sideToMove
        char stm = FENToList[1].charAt(0);
        if (stm != 'w' && stm != 'b') {
            boardState.restoreBoardState();
            throw new IllegalArgumentException(String.format("!!!!! INVALID FEN CHARACTER IN SIDE-TO-MOVE REPRESENTATION: %s !!!!!\n", stm));
            // return;
        }
        this.config.SIDE_TO_MOVE = stm;

        // Parse castling rights
        String castlingRights = FENToList[2];
        if (!castlingRights.equals("-") && castlingRights.length() <= 4) {
            for (int num = 0; num < castlingRights.length(); num++) {
                char right = castlingRights.charAt(num);
                if (Config.CASTLING.containsKey(right) && ((this.config.CASTLING_RIGHT == 0) || 
                    (this.config.CASTLING_RIGHT != 0 && (this.config.CASTLING_RIGHT & Config.CASTLING.get(right)) == 0))) {
                        this.config.CASTLING_RIGHT |= Config.CASTLING.get(right);
                }
                else if (right == '-') continue;
                else {
                    boardState.restoreBoardState();
                    throw new IllegalArgumentException(String.format("!!!!! INVALID FEN STRING. CASTLING-RIGHTS REPRESENTATION IS INCORRECT: %s !!!!!\n", castlingRights));
                    // return;
                }

            }
        } else if (castlingRights.length() > 4) {
            boardState.restoreBoardState();
            throw new IllegalArgumentException(String.format("!!!!! INVALID FEN STRING. CASTLING-RIGHTS REPRESENTATION SHOULD BE OF LENGTH AT MOST 4: %S !!!!!\n", castlingRights));
            // return;
        }

        // Parse en passant square
        String enp = FENToList[3];
        if (!enp.equals("-") && enp.length() == 2) {
            if (!Config.BOARDSQUARES.containsKey(enp.toLowerCase())) {
                boardState.restoreBoardState();
                throw new IllegalArgumentException(String.format("!!!!! INVALID EN PASSANT REPRESENTATION IN FEN STRING: %s !!!!!\n", enp));
                // return;
            }
            this.config.ENPASSANT_SQUARE = enp.toLowerCase();
        } else if (!enp.equals("-") && enp.length() != 2) {
            boardState.restoreBoardState();
            throw new IllegalArgumentException(String.format("!!!!! INVALID EN PASSANT REPRESENTATION IN FEN STRING: %s !!!!!\n", enp));
            // return;
        }

        // Set occupancies for white and black pieces
        for (int num = 0; num < 6; num++) {
            this.config.OCCUPANCIES[0] |= this.config.PIECE_BITBOARDS[num];
            this.config.OCCUPANCIES[1] |= this.config.PIECE_BITBOARDS[num + 6];
        }

        // Set occupancies for all pieces
        this.config.OCCUPANCIES[2] = this.config.OCCUPANCIES[0] | this.config.OCCUPANCIES[1];

        // Sanitise move count (if available)
        if (FENToList.length > 4 && 
            (!FENToList[4].matches("\\d+") || !FENToList[FENToList.length - 1].matches("\\d+"))) {
                boardState.restoreBoardState();
                throw new IllegalArgumentException(String.format("!!!!! INVALID MOVE COUNT REPRESENTATION IN FEN STRING: %s !!!!!\n", FENToList[FENToList.length - 1] + " and/or " + FENToList[4]));
                // return;
            }

        ZobristHashing zbh = new ZobristHashing(this.config);
        this.config.HASH_KEY = zbh.generateHashKey();

    }

    public void parseGo(String command) {
        // Start by resetting all relevant UCI time control variables
        this.config.UCI_QUIT = false;
        this.config.UCI_MOVES_TO_GO = 30;
        this.config.UCI_MOVE_TIME = -1;
        this.config.UCI_TIME = -1;
        this.config.UCI_INCREMENT_TIME = 0;
        this.config.UCI_START_TIME = 0;
        this.config.UCI_STOP_TIME = 0;
        this.config.UCI_TIME_IS_SET = false;
        this.config.UCI_STOPPED = false;
    
        // Start by splitting the entered command
        String[] commandList = command.split(" ");

        // init depth
        int depth = -1;

        // Pointer to walk through the command list
        int pointer = 1;
        while (pointer < commandList.length) {
            String cmnd = commandList[pointer];

            if (cmnd.equals("depth")) {
                try {
                    depth = Integer.parseInt(commandList[pointer + 1]);
                } catch (Exception e) {
                    depth = 6;
                }
                pointer += 2;
                System.out.println("Command is: depth");
            }
            // else depth = 6;

            else if (cmnd.equals("infinite")) {
                // Nothing to do here since our default
                // flag for this (UCI_time_is_set) is already set to false
                pointer += 2;
                System.out.println("Command is: infinite");
            }
            else if ((cmnd.equals("binc") && this.config.SIDE_TO_MOVE == 'b') || 
                (cmnd.equals("winc") && config.SIDE_TO_MOVE == 'w')) {
                    this.config.UCI_INCREMENT_TIME = Integer.parseInt(commandList[pointer + 1]);
                    pointer += 2;
                    System.out.println("Command is: " + cmnd);
                }

            else if ((cmnd.equals("btime") && this.config.SIDE_TO_MOVE == 'b') || 
                (cmnd.equals("wtime") && config.SIDE_TO_MOVE == 'w')) {
                    this.config.UCI_TIME = Integer.parseInt(commandList[pointer + 1]);
                    pointer += 2;
                    System.out.println("Command is: " + cmnd);
                }

            else if (cmnd.equals("movestogo")) {
                this.config.UCI_MOVES_TO_GO = Integer.parseInt(commandList[pointer + 1]);
                pointer += 2;
                System.out.println("Command is: movestogo");
            }

            else if (cmnd.equals("movetime")) {
                this.config.UCI_MOVE_TIME = Integer.parseInt(commandList[pointer + 1]);
                pointer += 2;
                System.out.println("Command is: movetime");
            }
    
            else {
                pointer++;
                System.out.println("Command is: invalid");
            }
        }

        if (this.config.UCI_MOVE_TIME != -1) {
            // Set UCI_Time to be the given move time
            this.config.UCI_TIME = this.config.UCI_MOVE_TIME;
            // Set moves_to_go to 1. This'll come in handy when we're tweaking
            // UCI_time later below. The logic is that if we have a move time specified
            // then we don't need to worry about how many moves we still have to play
            // in the game within the time control set, so we just divide by 1
            this.config.UCI_MOVES_TO_GO = 1;
        }

        // Init the start time
        this.config.UCI_START_TIME = TimeUtility.getTimeMs();

        if (this.config.UCI_TIME != -1) {
            // We're searching with time
            this.config.UCI_TIME_IS_SET = true;
            // Tweak the time taking into account moves_to_go
            this.config.UCI_TIME /= this.config.UCI_MOVES_TO_GO;
            // A little insurance so we don't go over the time we have: reduce UCI_time by 50 ms
            this.config.UCI_TIME -= 50;
            // Now we set the stop time our search function will be working with
            this.config.UCI_STOP_TIME = this.config.UCI_START_TIME + this.config.UCI_TIME + this.config.UCI_INCREMENT_TIME;
        }


        depth = depth < 0 ? 64 : depth;
        System.out.println("depth is: " + depth);
        // Search the position
        Evaluator evaluator = new Evaluator(this.config, this.moveGen, this.moveUtils);
        // System.out.println("We're searching");
        evaluator.searchPosition(depth);
    }

    public void parsePosition(String command) {
        /*
        *   Parses the UCI Protocol 'position' command

            Examples:
                - position startpos
                - position startpos moves e2e4 e7e5
                - position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1
                - position fen r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1 moves e2a6 e8g8

        */
        // Start by splitting the entered command
        String[] commandList = command.split(" ");
        if (commandList.length < 2) {
            throw new IllegalArgumentException(String.format("Invalid command (insufficient words): %s", command));
        }
        
        int moveCommandPos = 2;

        switch (commandList[1].toLowerCase()) {
            case "startpos":
                parseFEN(Config.START_POSITION);
                // foundStart = true;
                break;
            case "fen":
                List<String> FEN = new ArrayList<>();
                while (moveCommandPos < commandList.length && moveCommandPos < 10) {
                    String current = commandList[moveCommandPos];
                    if (current.toLowerCase().equals("moves")) {
                        // foundFEN = true;
                        break;
                    }
                    FEN.add(current);
                    moveCommandPos++;
                }
                try {
                    parseFEN(String.join(" ", FEN));
                    // foundFEN = true;
                } catch (IllegalArgumentException e) {
                    throw e;
                }
                // parseFEN(String.join(" ", FEN));
                break;
                
            default:
                throw new IllegalArgumentException(String.format("Invalid command: %s", commandList[1]));
        }
        // Get the moves, if any
        if (moveCommandPos != commandList.length) { //  && !commandList[moveCommandPos].toLowerCase().equals("moves")
            if (!commandList[moveCommandPos].toLowerCase().equals("moves")) {
                // Invalid command was passed
                throw new IllegalArgumentException(String.format("Invalid command: %s", command));
            }
            moveCommandPos++;
            // We have moves
            while (moveCommandPos < commandList.length) {
                String move = commandList[moveCommandPos];
                // System.out.println(move);
                try {
                    int encodedMove = parseMove(move);
                    this.moveUtils.makeMove(encodedMove, false);
                    moveCommandPos++;
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(String.format("Invalid command: %s", command));
                    // throw e;
                }
            }
        }
        PrintUtils.printBoard(this.config);
    }

    /**
     * ParseMoveHelper
     */
    public class ParseMoveHelper {
    
        public Object[] run(long attacks, int target, int ownColour) {
            boolean illegal = false;
            int capture = 0;
            if (BitBoard.getBitAtIndex(attacks, target) != 0) {
                // Valid move by chess piece
                if (BitBoard.getBitAtIndex(config.OCCUPANCIES[2], target) != 0) {
                    // Target square is occupied
                    if (BitBoard.getBitAtIndex(config.OCCUPANCIES[ownColour], target) != 0) {
                        // Occupied by a friendly piece
                        illegal = true;
                    } else capture = 1;
                }
            }
            // Invalid rook move
            else illegal = true;
            return new Object[] {illegal, capture};
        }
    }
    ParseMoveHelper ParseMoveHelper = new ParseMoveHelper();

    public int parseMove(String moveString) {
        /*
        *     Given a move (likely from user input),
            parse it to see if it is legal.
            Should be compatible with UCI protocol.

            Illegal moves should throw IllegalArgumentException("Invalid move")

            Move should be structured like the following:
                - First two positions indicate source
                - Next two indicate target
                - If promotion, next position should indicate
                    piece promoted to.

                So,
                    'e3f5' indicates a knight move from the 'e3'
                square to the 'f5' square;
                    'h2h1q' indicates a pawn getting promoted to
                    a queen.

            General idea:
                - Confirm that move has source, target
                    and promotion (if exists).
                - Find which piece it is and see if move
                    can be made. (Take advantage of side
                    to move to narrow whose piece it is.)

        */
        // Convert to lower case (just in case)
        moveString = moveString.toLowerCase();

        // Move should be of length 4 or 5
        if (moveString.length() != 4 && moveString.length() != 5) {
            throw new IllegalArgumentException(String.format("Invalid move: %s", moveString));
        }

        // Sanitise source and target squares
        if (!Config.BOARDSQUARES.containsKey(moveString.substring(0, 2)) ||
            !Config.BOARDSQUARES.containsKey(moveString.substring(2, 4))) {
                throw new IllegalArgumentException(String.format("Invalid move: %s", moveString));
            }
        // Extract source and target squares
        int source = Config.BOARDSQUARES.get(moveString.substring(0, 2));
        int target = Config.BOARDSQUARES.get(moveString.substring(2, 4));

        // Extract promotion piece if available
        char promoString = ' ';
        int promotedPiece = -1;
        if (moveString.length() == 5) {
            promoString = moveString.charAt(4);
            if (promoString != 'n' && promoString != 'b' && promoString != 'r' && promoString != 'q') {
                // Invalid promotion piece
                throw new IllegalArgumentException(String.format("Invalid move: %s", moveString));
            }
            promotedPiece = this.config.SIDE_TO_MOVE == 'b' ? Config.PIECES.get(promoString) : Config.PIECES.get(promoString) - 6;
        }

        // Get the moving piece
        int movingPiece = -1;
        char pieceString = ' ';
        int pieceOffset = this.config.SIDE_TO_MOVE == 'w' ? 0 : 6;
        for (int idx = 0; idx < 6; idx++) {
            if (BitBoard.getBitAtIndex(this.config.PIECE_BITBOARDS[idx + pieceOffset], source) != 0) {
                movingPiece = idx + pieceOffset;
                pieceString = Config.ASCII_PIECES[idx + pieceOffset];
                break;
            }
        }
        if ((movingPiece == -1) || (movingPiece != 0 && movingPiece != 6 && promotedPiece != -1)) {
            // Trying to move the wrong piece; 
            // or from an empty square;
            // or promoting from a non-pawn move.
            throw new IllegalArgumentException(String.format("Invalid move: %s", moveString));
        }

        // Initialise remaining flags for encoding
        int capture = 0;
        int doublePP = 0;
        int enPassant = 0;
        int castling = 0;

        int ownColour = this.config.SIDE_TO_MOVE == 'w' ? 0 : 1;
        boolean illegalMove = false;
        Object[] res = new Object[2];

        // Start 
        pieceSwitch :  // switch label for breaking from deep within blocks
        switch (Character.toLowerCase(pieceString)) {
            case 'p':
                boolean isPawnPush = BitBoard.getBitAtIndex(
                            AttacksGenerator.PAWN_MOVES_MASKS[ownColour][source] & ~this.config.OCCUPANCIES[2],
                            target
                            ) != 0;   
                boolean isCapture = BitBoard.getBitAtIndex(
                            AttacksGenerator.PAWN_ATTACKS[ownColour][source] & this.config.OCCUPANCIES[ownColour ^ 1], 
                            target
                            ) != 0;   
                boolean isEnPassant = target == Config.BOARDSQUARES.get(this.config.ENPASSANT_SQUARE);  
                int pawnOffset = this.config.SIDE_TO_MOVE == 'w' ? -8 : 8; 

                // Start the checks
                if (isPawnPush) {
                    // if (!isCapture && !isEnPassant) {
                    if ((1 - (1 & (this.config.OCCUPANCIES[2] >> source + pawnOffset))) == 0) {
                        /*
                        * square just ahead of source square isn't empty ==> illegal move.
                        * target square not in the appropriate distance from source square
                        * for a pawn push ==> illegal move.
                        */
                        illegalMove = true;
                        break pieceSwitch;
                    }
                    if (Math.abs(target - source) == 16) {
                        doublePP = 1;
                    }
                    if ((target < 8 || (target >= 56 && target < 64)) && promotedPiece == -1) {
                        // We should be promoting, but the piece we're promoting
                        // to wasn't specified ==> illegal move
                        illegalMove = true;
                        break pieceSwitch;
                    }
                    // }
                }
                if (isCapture) {
                    if ((target < 8 || (target >= 56 && target < 64)) && promotedPiece == -1) {
                        // We should be promoting, but the piece we're promoting
                        // to wasn't specified ==> illegal move
                        illegalMove = true;
                        break pieceSwitch;
                    }
                    capture = 1;
                }
                if (isEnPassant) {
                    enPassant = 1;
                    capture = 1;
                }
                else if (
                    !isPawnPush && !isCapture && !isEnPassant
                ) {
                    /*
                     * Pawn move should be in one of the three categories above.
                     * Anything else is illegal
                     */
                    illegalMove = true;
                }
                
                break;
            case 'n':
                res = ParseMoveHelper.run( 
                            AttacksGenerator.KNIGHT_ATTACKS[source], 
                            target, ownColour);
                illegalMove = (boolean) res[0];
                capture = (int) res[1];
                break;
            case 'b':
                res = ParseMoveHelper.run( 
                            AttacksGenerator.getBishopAttacks(source, this.config.OCCUPANCIES[2]), 
                            target, ownColour);
                illegalMove = (boolean) res[0];
                capture = (int) res[1];
                
                break;
            case 'r':
                res = ParseMoveHelper.run( 
                            AttacksGenerator.getRookAttacks(source, this.config.OCCUPANCIES[2]), 
                            target, ownColour);
                illegalMove = (boolean) res[0];
                capture = (int) res[1];
                
                break;
            case 'q':
                res = ParseMoveHelper.run( 
                            AttacksGenerator.getQueenAttacks(source, this.config.OCCUPANCIES[2]), 
                            target, ownColour);
                illegalMove = (boolean) res[0];
                capture = (int) res[1];
                
                break;
        
            default: // 'k'
                // MoveUtils MoveUtils = new MoveUtils(this.config);
                // MoveGenerator moveGen = new MoveGenerator(MoveUtils, this.config);
                char king = this.config.SIDE_TO_MOVE == 'w' ? 'K' : 'k';
                char queen = this.config.SIDE_TO_MOVE == 'w' ? 'Q' : 'q';
                // Check ability to castle
                boolean canCastleKS = this.moveGen.canCastle(king, source, ownColour ^ 1);
                boolean canCastleQS = this.moveGen.canCastle(queen, source, ownColour ^ 1);
                // Check regular king move
                if (BitBoard.getBitAtIndex(AttacksGenerator.KING_ATTACKS[source], target) != 0) {
                    if (BitBoard.getBitAtIndex(this.config.OCCUPANCIES[2], target) != 0) {
                        // Target square is occupied
                        if (BitBoard.getBitAtIndex(this.config.OCCUPANCIES[ownColour], target) != 0) {
                            // Occupied by a friendly piece
                            illegalMove = true;
                        } else capture = 1;
                    }
                }
                // Not a regular king move? Are we castling?
                else if (source == target - 2) {
                    if (canCastleKS) {castling = 1;}
                    else illegalMove = true;
                }
                else if (source == target + 2) {
                    if (canCastleQS) {castling = 1;}
                    else illegalMove = true;
                }
                // Any other type of move is invalid
                else illegalMove = true;
                break;
        }

        if (illegalMove) {
            // For some reason, the move was illegal
            throw new IllegalArgumentException(String.format("Invalid move: %s", moveString));
        }
        // MoveCoder expects a number between 0 and 11
        promotedPiece = promotedPiece == -1 ? 0 : promotedPiece;

        return MoveCoder.encodeMove(source, target, movingPiece, promotedPiece, capture, doublePP, enPassant, castling);
    }
}
