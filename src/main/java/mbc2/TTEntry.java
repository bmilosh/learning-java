package mbc2;

public class TTEntry {
    private long hashKey;
    private int depth;
    private int hashFlag;
    private int score;

    public TTEntry(long hashKey, int depth, int hashFlag, int score) {
        this.hashKey = hashKey;
        this.depth = depth;
        this.hashFlag = hashFlag;
        this.score = score;
    }

    public long getHashKey() {
        return hashKey;
    }

    public int getDepth() {
        return depth;
    }

    public int getScore() {
        return score;
    }

    public int getHashFlag() {
        return hashFlag;
    }

    public void clear() {
        hashKey = 0L;
        depth = 0;
        hashFlag = 0;
        score = 0;
    }

    public void setHashKey(long newHashKey) {
        hashKey = newHashKey;
    }

    public void setDepth(int newDepth) {
        depth = newDepth;
    }

    public void setScore(int newScore) {
        score = newScore;
    }

    public void setHashFlag(int newFlag) {
        hashFlag = newFlag;
    }

    @Override
    public String toString() {
        return String.format(
            "TTEntry{hashKey=0x%s, depth=%d, hashFlag=%s, score=%d}",
            Long.toHexString(hashKey),
            depth,
            Config.HASH_FLAGS[hashFlag],
            score
        );
    }
}
