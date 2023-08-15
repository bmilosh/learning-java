package mbc2;

public class Parsers {
    private Config config;
    private BoardState boardState;

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
}
