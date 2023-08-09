package mbc2;

public class PseudorandomGenerator {
    public static long generateRandom32bitNumber() {
        /*
         * Uses the XOR algorithm
         */
        long temp = Config.PSEUDORANDOM_NUMBER_STATE;
        
        temp ^=  (temp << 13) & 0xFFFFFFFFL;
        temp ^= temp >> 17;
        temp ^=  (temp << 5) & 0xFFFFFFFFL;

        Config.PSEUDORANDOM_NUMBER_STATE =  temp;
        return  temp; 
    }

    public static long generateRandom64bitNumber() {
        long n1, n2, n3, n4;

        n1 = (long) generateRandom32bitNumber() & 0xFFFFL;
        n2 = (long) generateRandom32bitNumber() & 0xFFFFL;
        n3 = (long) generateRandom32bitNumber() & 0xFFFFL;
        n4 = (long) generateRandom32bitNumber() & 0xFFFFL;

        return n1 | (n2 << 16) | (n3 << 32) | (n4 << 48);
    }
}
