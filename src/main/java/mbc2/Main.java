package mbc2;

// import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        // PrintUtils.printBitBoard(0b111111101111111011111110111111101111111011111110111111101111111L);
        // PrintUtils.printBitBoard(Config.OCCUPANCIES[2]);

        // List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        // List<Integer> ages = Arrays.asList(25, 30);
        
        // This gives use the Java equivalent of Python's range function.
        List<Integer> nums2 = IntStream.iterate(8, i -> i > 3, i -> i - 1).boxed().collect(Collectors.toList());
        List<Integer> nums3 = IntStream.iterate(17, i -> i < 27, i -> i + 1).boxed().collect(Collectors.toList());

        List<Pair<Integer, Integer>> zipped = ZipUtility.zip(nums2, nums3);

        for (Pair<Integer, Integer> pair : zipped) {
            System.out.println(pair.first + " : " + pair.second);
        }
    }
}