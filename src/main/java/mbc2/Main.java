package mbc2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        EngineInitMethods.initAll();
        // ZobristHashing zBH = new ZobristHashing(config);
        MoveUtils MoveUtils = new MoveUtils(config);
        MoveGenerator MoveGenerator = new MoveGenerator(MoveUtils, config);
        Perft perft = new Perft(config, MoveUtils, MoveGenerator);
        
        // BoardState boardState = new BoardState(config);
        // Parsers parsers = new Parsers(config, boardState, MoveGenerator, MoveUtils);
        // // Evaluator evaluator = new Evaluator(config, MoveGenerator, MoveUtils);
        // parsers.parseFEN(Config.TRICKY_POSITION);
        // PrintUtils.printBoard(config);
        // // System.out.println("hash key: 0x" + Long.toHexString(zBH.generateHashKey()));

        // TTEntry entry = new TTEntry(67849343L, 4, Config.HASH_EXACT_FLAG, 20000);
        // System.out.println(entry);

        // int move = parsers.parseMove("e2a6");
        // MoveUtils.makeMove(move, false);
        // move = parsers.parseMove("c7c5");
        // MoveUtils.makeMove(move, false);

        // PrintUtils.printBoard(config);
        // // long s = TimeUtility.getTimeMs();
        // evaluator.ORIGINAL_DEPTH = 7;
        // evaluator.searchPosition(evaluator.ORIGINAL_DEPTH);
        // // System.out.println("Time taken: " + (TimeUtility.getTimeMs() - s) + " ms");
        // // perft.perftTest(3);


        int min = 0;
        int max = 67108860;
        int numberOfIntegers = 20000000;
        
        // Set<Integer> uniqueIntegers = new HashSet<>();
        Random random = new Random();
        ArrayList<Integer> uniqueIntegers = new ArrayList<>();

        long genRand = TimeUtility.getTimeMs();
        System.out.println("Starting to generate " + numberOfIntegers + " unique random numbers");
        
        while (uniqueIntegers.size() < numberOfIntegers) {
            int randomNumber = random.nextInt(max - min + 1) + min;
            uniqueIntegers.add(randomNumber);
        }
        System.out.println("Took " + (TimeUtility.getTimeMs() - genRand) + " ms to generate " + numberOfIntegers + " unique random integers.");
        System.out.println();
        
        // Regular clear transposition table
        populateHashTable(uniqueIntegers, config);
        long regularStart = TimeUtility.getTimeMs();
        System.out.println("Starting regular clear");
        config.HASH_TABLE.clearTranspositionTableRegular();
        System.out.println("Time taken for regular clear is: " + (TimeUtility.getTimeMs() - regularStart) + " ms");
        System.out.println();

        populateHashTable(uniqueIntegers, config);
        // ArrayList clear transposition table
        long arrayStart = TimeUtility.getTimeMs();
        System.out.println("Starting arraylist clear");
        config.HASH_TABLE.clearTranspositionTableArrayList();
        System.out.println("Time taken for arraylist clear is: " + (TimeUtility.getTimeMs() - arrayStart) + " ms");
        System.out.println();
        
    }

    public static void populateHashTable(Set<Integer> uniqueIntegers, Config config) {
        Random random = new Random();
        for (int index : uniqueIntegers) {
            // Do something with uniqueInteger
            TTEntry entry = new TTEntry(
                random.nextLong(),
                random.nextInt(20),
                random.nextInt(3),
                random.nextInt(50000)
            );
            // int index = (int) entry.getHashKey() % Config.HASH_TABLE_SIZE;
            config.HASH_TABLE.storeEntry(index, entry);
        }
        for (int i = 0; i < 20; i++) {
            TTEntry entry = config.HASH_TABLE.retrieveEntry(i);
            if (entry == null) {
                System.out.println("Position " + i + " is empty");
            }
            else System.out.println(entry.toString());
        }
        System.out.println();
    }

    public static void populateHashTable(ArrayList<Integer> uniqueIntegers, Config config) {
        Random random = new Random();
        for (int index : uniqueIntegers) {
            // Do something with uniqueInteger
            TTEntry entry = new TTEntry(
                random.nextLong(),
                random.nextInt(20),
                random.nextInt(3),
                random.nextInt(50000)
            );
            // int index = (int) entry.getHashKey() % Config.HASH_TABLE_SIZE;
            config.HASH_TABLE.storeEntry(index, entry);
        }
        for (int i = 0; i < 20; i++) {
            TTEntry entry = config.HASH_TABLE.retrieveEntry(i);
            if (entry == null) {
                System.out.println("Position " + i + " is empty");
            }
            else System.out.println(entry.toString());
        }
        System.out.println();
    }
}