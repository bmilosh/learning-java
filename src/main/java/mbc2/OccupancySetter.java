package mbc2;

public class OccupancySetter {

    public static long run(int index, int numberOfSetBitsInMask, long attackMask) {
        /*
        *  With any given index, you get a bitboard showing
            one possible occupancy combination you might expect
            for a sliding piece set on a particular square (this
            square is indicated by the attack mask passed as a
            parameter to the function). Observe that the same
            index paired with the same attack mask will always
            result in the same occupancy combination.

            There are (2**num_of_set_bits_in_mask) such possible
            occupancy combinations.

            For example, with the following attack mask (bishop on f8):
            8    0  0  0  0  0  0  0  0
            7    0  0  0  0  1  0  1  0
            6    0  0  0  1  0  0  0  0
            5    0  0  1  0  0  0  0  0
            4    0  1  0  0  0  0  0  0
            3    0  0  0  0  0  0  0  0
            2    0  0  0  0  0  0  0  0
            1    0  0  0  0  0  0  0  0

                a  b  c  d  e  f  g  h

                Bitboard: 8657588224

            calling the method for index 5 would result in the following occupancy board:
            8    0  0  0  0  0  0  0  0
            7    0  0  0  0  1  0  0  0
            6    0  0  0  1  0  0  0  0
            5    0  0  0  0  0  0  0  0
            4    0  0  0  0  0  0  0  0
            3    0  0  0  0  0  0  0  0
            2    0  0  0  0  0  0  0  0
            1    0  0  0  0  0  0  0  0

                a  b  c  d  e  f  g  h

                Bitboard: 528384

            while calling the method for index 18 would give the following occupancy board:
            8    0  0  0  0  0  0  0  0
            7    0  0  0  0  0  0  1  0
            6    0  0  0  0  0  0  0  0
            5    0  0  0  0  0  0  0  0
            4    0  1  0  0  0  0  0  0
            3    0  0  0  0  0  0  0  0
            2    0  0  0  0  0  0  0  0
            1    0  0  0  0  0  0  0  0

                a  b  c  d  e  f  g  h

                Bitboard: 8589950976

        */

        long occupancy = 0L;
        for (int num = 0; num < numberOfSetBitsInMask; num++) {
            long square = Long.lowestOneBit(attackMask);
            attackMask &= ~square;
            if ((index & (1 << num)) != 0) {
                occupancy |= square;
            }
        }
        return occupancy;
    }
}
