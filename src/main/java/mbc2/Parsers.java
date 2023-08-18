package mbc2;

public class Parsers {
    private Config config;
    private BoardState boardState;
    // private MoveUtils MoveUtils = new MoveUtils(config);

    public Parsers(Config Config, BoardState boardState) {
        this.config = Config;
        this.boardState = boardState;
    }

    public void parseFEN(String fenString) {
        /*
         * Note that this method expects that EngineInitMethods.initAll()
         * has been called.
         */
        String[] FENToList = fenString.split(" ");
        // sanitise FEN
        if ((FENToList.length < 4) || (FENToList.length > 6)) {
            System.out.println("!!!!!!!!!!!!!Invalid FEN string!!!!!!!!!!!!!");
            System.out.println(fenString);
            return;
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
                System.out.printf("!!!!! INVALID FEN CHARACTER IN SQUARE REPRESENTATION: %s !!!!!\n", piece);
                boardState.restoreBoardState();
                return;
            }

            if ((index == position.length()) && square != 64) {
                System.out.printf("!!!!! INVALID FEN: \"%s\". WRONG NUMBER OF SQUARES !!!!!\n", fenString);
                boardState.restoreBoardState();
                return;
            }
        }

        // Parse sideToMove
        char stm = FENToList[1].charAt(0);
        if (stm != 'w' && stm != 'b') {
            System.out.printf("!!!!! INVALID FEN CHARACTER IN SIDE-TO-MOVE REPRESENTATION: %s !!!!!\n", stm);
            boardState.restoreBoardState();
            return;
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
                else {
                    System.out.printf("!!!!! INVALID FEN STRING. CASTLING-RIGHTS REPRESENTATION IS INCORRECT: %S !!!!!\n", castlingRights);
                    boardState.restoreBoardState();
                    return;
                }

            }
        } else if (castlingRights.length() > 4) {
            System.out.printf("!!!!! INVALID FEN STRING. CASTLING-RIGHTS REPRESENTATION SHOULD BE OF LENGTH AT MOST 4: %S !!!!!\n", castlingRights);
            boardState.restoreBoardState();
            return;
        }

        // Parse en passant square
        String enp = FENToList[3];
        if (!enp.equals("-") && enp.length() == 2) {
            if (!Config.BOARDSQUARES.containsKey(enp.toLowerCase())) {
                System.out.printf("!!!!! INVALID EN PASSANT REPRESENTATION IN FEN STRING: %s !!!!!\n", enp);
                boardState.restoreBoardState();
                return;
            }
            this.config.ENPASSANT_SQUARE = enp.toLowerCase();
        } else if (!enp.equals("-") && enp.length() != 2) {
            System.out.printf("!!!!! INVALID EN PASSANT REPRESENTATION IN FEN STRING: %s !!!!!\n", enp);
            boardState.restoreBoardState();
            return;
        }

        // Set occupancies for white and black pieces
        for (int num = 0; num < 6; num++) {
            this.config.OCCUPANCIES[0] |= this.config.PIECE_BITBOARDS[num];
            this.config.OCCUPANCIES[1] |= this.config.PIECE_BITBOARDS[num + 6];
        }

        // Set occupancies for all pieces
        this.config.OCCUPANCIES[2] = this.config.OCCUPANCIES[0] | this.config.OCCUPANCIES[1];

    }

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
                    if ((1 - (1 & (this.config.OCCUPANCIES[2] >>> source + pawnOffset))) == 0) {
                        /*
                            * square just ahead of source square isn't empty ==> illegal move.
                            * target square not in the appropriate distance from source square
                            * for a pawn push ==> illegal move.
                            */
                        // throw new IllegalArgumentException(String.format("Invalid move: %s", moveString));
                        illegalMove = true;
                        break pieceSwitch;
                    }
                    if (Math.abs(target - source) == 16) {
                        doublePP = 1;
                    }
                    if ((target < 8 || (target >= 56 && target < 64)) && promotedPiece == -1) {
                        // We should be promoting, but the piece we're promoting
                        // to wasn't specified ==> illegal move
                        // throw new IllegalArgumentException(String.format("Invalid move: %s", moveString));
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
                res = InnerParsers.run( 
                            AttacksGenerator.KNIGHT_ATTACKS[source], 
                            target, ownColour, this.config);
                illegalMove = (boolean) res[0];
                capture = (int) res[1];
                // if (BitBoard.getBitAtIndex(AttacksGenerator.KNIGHT_ATTACKS[source], target) != 0) {
                //     if (BitBoard.getBitAtIndex(this.config.OCCUPANCIES[2], target) != 0) {
                //         // Target square is occupied
                //         if (BitBoard.getBitAtIndex(this.config.OCCUPANCIES[ownColour], target) != 0) {
                //             // Occupied by a friendly piece
                //             illegalMove = true;
                //         } else capture = 1;
                //     }
                // }
                // // Invalid knight move
                // else illegalMove = true;
                break;
            case 'b':
                res = InnerParsers.run( 
                            AttacksGenerator.getBishopAttacks(source, this.config.OCCUPANCIES[2]), 
                            target, ownColour, this.config);
                illegalMove = (boolean) res[0];
                capture = (int) res[1];
                
                break;
            case 'r':
                res = InnerParsers.run( 
                            AttacksGenerator.getRookAttacks(source, this.config.OCCUPANCIES[2]), 
                            target, ownColour, this.config);
                illegalMove = (boolean) res[0];
                capture = (int) res[1];
                
                break;
            case 'q':
                res = InnerParsers.run( 
                            AttacksGenerator.getQueenAttacks(source, this.config.OCCUPANCIES[2]), 
                            target, ownColour, this.config);
                illegalMove = (boolean) res[0];
                capture = (int) res[1];
                
                break;
        
            default: // 'k'
                MoveUtils MoveUtils = new MoveUtils(this.config);
                MoveGenerator moveGen = new MoveGenerator(MoveUtils, this.config);
                char king = this.config.SIDE_TO_MOVE == 'w' ? 'K' : 'k';
                char queen = this.config.SIDE_TO_MOVE == 'w' ? 'Q' : 'q';
                // Check ability to castle
                boolean canCastleKS = moveGen.canCastle(king, source, ownColour ^ 1);
                boolean canCastleQS = moveGen.canCastle(queen, source, ownColour ^ 1);
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

    /**
     * InnerParsers
     */
    public class InnerParsers {
    
        public static Object[] run(long attacks, int target, int ownColour, Config config) {
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
}
