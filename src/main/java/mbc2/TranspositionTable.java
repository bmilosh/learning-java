package mbc2;

import java.util.ArrayList;

public class TranspositionTable {
    private TTEntry[] hashTable;
    private ArrayList<Integer> usedIndices = new ArrayList<>();

    public TranspositionTable(int size) {
        hashTable = new TTEntry[size];
    }

    public void storeEntry(int index, TTEntry entry) {
        hashTable[index] = entry;
        usedIndices.add(index);
    }

    public TTEntry retrieveEntry(int index) {
        return hashTable[index];
    }

    public int getSize() {
        return hashTable.length;
    }

    public void clearTranspositionTableArrayList() {
        for (int index: usedIndices) {
            TTEntry entry = retrieveEntry(index);
            entry.clear();
        }
    }

    public void clearTranspositionTableRegular() {
        for (TTEntry entry: hashTable) {
            if (entry != null) {
                entry.clear();
            }
        }
    }
}
