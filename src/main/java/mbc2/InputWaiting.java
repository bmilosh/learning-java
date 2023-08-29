package mbc2;

import java.io.IOException;

public class InputWaiting {
    public static int run() throws IOException {
        if (System.getProperty("os.name").startsWith("Windows")) {
            // System.out.println("We're in Windows");
            int res = InputWaitingWindows.inputWaiting();
            // System.out.println("Back to InputWaiting. res is: " + res);
            return res;
            // return InputWaitingWindows.inputWaiting();
        } else {
            // System.out.println("We're in Linux");
            int res = InputWaitingUnix.inputWaiting();
            // System.out.println("Back to InputWaiting. res is: " + res);
            return res;
        }
    }
}
