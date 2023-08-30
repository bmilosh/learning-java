package mbc2;

import java.io.IOException;

public class InputWaiting {
    public static int run() throws IOException {
        if (System.getProperty("os.name").startsWith("Windows")) {
            // System.out.println("We're in Windows");
            int res = InputWaitingWindows.inputWaiting();
            return res;
        } else {
            // System.out.println("We're in Linux");
            int res = InputWaitingUnix.inputWaiting();
            return res;
        }
    }
}
