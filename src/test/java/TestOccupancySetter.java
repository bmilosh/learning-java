import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import mbc2.OccupancySetter;

public class TestOccupancySetter {
    @Test
    void testSetter() {
        assertEquals(20480L, OccupancySetter.run(3, 5, 0b1000000100000010000101000000000000L));
        assertEquals(16384L, OccupancySetter.run(2, 5, 0b1000000100000010000101000000000000L));
    }
}
