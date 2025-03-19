package mbc2;

public class TranspositionTable {
    private TTEntry[] hashTable;

    public TranspositionTable(int size) {
        hashTable = new TTEntry[size];
    }

    public void storeEntry(long hashKey, int depth, int score, int flag, int ply) {
        int index = (int) hashKey % Config.HASH_TABLE_SIZE;
        // update score if necessary
        if (score < -Config.MATE_SCORE) score -= ply;
        else if (score > Config.MATE_SCORE) score += ply;
        index = (index < 0) ? Config.HASH_TABLE_SIZE + index : index;
        hashTable[index] = new TTEntry(hashKey, depth, flag, score);
    }

    public int retrieveEntry(int alpha, int beta, int depth, long hashKey, int ply) {
        int index = (int) hashKey % Config.HASH_TABLE_SIZE;
        index = (index < 0) ? Config.HASH_TABLE_SIZE + index : index;
        TTEntry entry = hashTable[index];
        if (entry != null) {
            // make sure we're dealing with the exact position we need
            if (entry.getHashKey() != hashKey || entry.getDepth() < depth) {
                return Config.NO_HASH_ENTRY;
            }

            int storedFlag = entry.getHashFlag();
            int storedScore = entry.getScore();

            // Retrieve score independent of path from root to current node
            if (storedScore < -Config.MATE_SCORE) {
                storedScore += ply;
            }
            else if (storedScore > Config.MATE_SCORE) {
                storedScore -= ply;
            }

            if (storedFlag == Config.HASH_ALPHA_FLAG && storedScore <= alpha) return alpha;
            else if (storedFlag == Config.HASH_BETA_FLAG && storedScore >= beta) return beta;
            else if (storedFlag == Config.HASH_EXACT_FLAG) return storedScore;
        }
        
        return Config.NO_HASH_ENTRY;
    }

    public int getSize() {
        return hashTable.length;
    }

    public void clear() {
        for (TTEntry entry: hashTable) {
            if (entry != null) {
                entry.clear();
            }
        }
    }
}
