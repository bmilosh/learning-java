package mbc2;

public class MagicNumberGenerator {

    private static long generateCandidateMagicNumber(Config Config) {
        return (
            PseudorandomGenerator.generateRandom64bitNumber(Config)
            & PseudorandomGenerator.generateRandom64bitNumber(Config)
            & PseudorandomGenerator.generateRandom64bitNumber(Config)
        );
    }

    public static long generateMagicNumber(int square, int numberOfSetBits, boolean isBishop, Config Config) {
        /*
        *  :Input:
                - square: an int representing the board square
                - num_of_set_bits
                - bishop: 0 if it's a rook and 1 if a bishop
            :Returns:
                - magic number if found
                - 0 otherwise

            Given a sliding piece on a square, it returns a magic
            number associated to that square if it can find one.
            Else, it returns 0.

            The key idea is to find a random number that assigns
            to each possible occupancy combination related to the
            attack path of the sliding piece, a unique attack.
        */

        /*
         init occupancies:
         we use an array of size 4096 as that's the
         maximum size we can need (say, rook on a1)
         */
        long[] occupancies = new long[4096];

        // Init attacks
        long[] attacks = new long[4096];

        // init attack mask for current piece
        long attackMask = isBishop ? AttacksGenerator.generateBishopAttacks(square) : AttacksGenerator.generateRookAttacks(square);

        // init occupancy indices
        int occupancyIndices = 1 << numberOfSetBits;
        for (int index = 0; index < occupancyIndices; index++) {
            occupancies[index] = OccupancySetter.run(index, numberOfSetBits, attackMask);
            attacks[index] = isBishop ? AttacksGenerator.getBishopAttacksOnTheFly(square, occupancies[index]) : AttacksGenerator.getRookAttacksOnTheFly(square, occupancies[index]);
        }

        for (int x = 0; x < 100_000_000; x++) {
            long magicCandidate = generateCandidateMagicNumber(Config);  // Doesn't work as int
            if ((Long.bitCount((magicCandidate * attackMask) & 0xFF00000000000000L)) < 6) {
                continue;
            }
            // init used attacks and fail flag
            long[] usedAttacks = new long[4096];
            boolean failFlag = false;

            for (int index = 0; !failFlag && (index < occupancyIndices); index++) {
                /*
                 *  My understanding of what makes a magic candidate ideal
                    is that it should, among other things, map each occupancy
                    combination to a unique attack.

                    '& 0xffffffffffffffff' is needed to ensure 64 bits
                    and avoid having an IndexError later on when we do
                    usedAttacks[magicIndex]
                 */
                
                int magicIndex = (int) ((((occupancies[index] * magicCandidate) & 0xFFFFFFFFFFFFFFFFL) >> (64 - numberOfSetBits)) & 0xFFFFFFFFL);
                magicIndex = magicIndex < 0 ? 4096 + magicIndex : magicIndex;  // This is needed to avoid getting negative indices
                if (usedAttacks[magicIndex] == 0) {
                    usedAttacks[magicIndex] = attacks[index];
                } else if (usedAttacks[magicIndex] != attacks[index]) {
                    /*
                     * Since we've seen this magic index before, then
                       it means we have a case of two occupancy combinations
                       being mapped to different attacks. Hence, we can say
                       that this candidate magic number doesn't work and can
                       be discarded.
                     */
                    failFlag = true;
                    break;
                }
            }
            if (!failFlag) {
                // our candidate magic number works
                return magicCandidate;
            }
        }
        // If we're here, then no good magic number was found
        System.out.println("!!!!!!!!!!!!!! NO MAGIC NUMBER FOUND !!!!!!!!!!!!!!");
        return 0L;
    }
}
