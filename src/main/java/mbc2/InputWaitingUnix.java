package mbc2;

import java.io.IOException;

public class InputWaitingUnix {
    
    public static int inputWaiting() throws IOException {

        try {
            int res = System.in.available();
            // System.out.println("System.in.available: " + res);
            return res;
        } catch (IOException e) {
            // Handle the exception as needed
            e.printStackTrace();
            return 0;
        }
    }
}
