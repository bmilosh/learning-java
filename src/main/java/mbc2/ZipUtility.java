package mbc2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * This code was provided by ChatGPT (GPT-3.5).
 * It provides functionality similar to Python's `zip` function for 2 iterables.
 * No restrictions on the types of lists passed to it.
 */

public class ZipUtility {

    public static <T, U> List<Pair<T, U>> zip(List<T> list1, List<U> list2) {

        /*
         * Instead of throwing an exception when one list is smaller than the other,
         * we just stop once we've exhausted the contents of the smaller
         * list instead.
         */

        return IntStream.range(0, Math.min(list1.size(), list2.size()))
                .mapToObj(i -> new Pair<>(list1.get(i), list2.get(i)))
                .collect(Collectors.toList());
    }
}
