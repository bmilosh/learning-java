import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import mbc2.Pair;
import mbc2.ZipUtility;

public class TestZipUtility {

    @Test
    void testZip() {
        List<Integer> range1 = Arrays.asList(1, 2, 3, 4);
        List<Integer> range2 = Arrays.asList(8, 7, 6, 5);

        List<Pair<Integer, Integer>> zipped = ZipUtility.zip(range1, range2);

        List<Pair<Integer, Integer>> expected = Arrays.asList(
            new Pair<>(1, 8),
            new Pair<>(2, 7),
            new Pair<>(3, 6),
            new Pair<>(4, 5)
        );

        assertEquals(expected, zipped);

        List<Integer> nums2 = IntStream.iterate(8, i -> i > 5, i -> i - 1).boxed().collect(Collectors.toList());
        List<Integer> nums3 = IntStream.iterate(17, i -> i < 27, i -> i + 1).boxed().collect(Collectors.toList());

        List<Pair<Integer, Integer>> zipped2 = ZipUtility.zip(nums2, nums3);

        List<Pair<Integer, Integer>> expected2 = Arrays.asList(
            new Pair<>(8, 17),
            new Pair<>(7, 18),
            new Pair<>(6, 19)
        );
        assertEquals(expected2, zipped2);

        // Combining different types shouldn't be a problem.
        List<String> arr1 = Arrays.asList("John", "James", "Joseph");

        List<Pair<Integer, String>> zipped3 = ZipUtility.zip(range1, arr1);
        List<Pair<Integer, String>> expected3 = Arrays.asList(
            new Pair<>(1, "John"),
            new Pair<>(2, "James"),
            new Pair<>(3, "Joseph")
            );
        assertEquals(expected3, zipped3);

        // If one List is empty, the result should also be an empty List.
        List<String> arr2 = Arrays.asList();
        List<Pair<Integer, String>> zipped4 = ZipUtility.zip(range1, arr2);
        List<Pair<Integer, String>> expected4 = Arrays.asList();
        assertEquals(expected4, zipped4);
    }
}
