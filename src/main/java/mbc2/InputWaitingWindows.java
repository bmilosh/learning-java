package mbc2;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.Wincon;
import com.sun.jna.ptr.IntByReference;

public class InputWaitingWindows {

    public static int inputWaiting() {
        // System.out.println("Input waiting starting" );
        int init = 0, pipe = 0;
        HANDLE inh = null;
        IntByReference dw = new IntByReference(0);

        if (init == 0) {
            // System.out.println("Init is 0" );
            // Initialize the console handle and check for pipe status
            init = 1;
            // inh = Kernel32.INSTANCE.GetStdHandle(3); // -10 for STD_INPUT_HANDLE
            inh = Kernel32.INSTANCE.GetStdHandle(Wincon.STD_INPUT_HANDLE);
            pipe = Kernel32.INSTANCE.GetConsoleMode(inh, dw) ? 1 : 0;
            // pipe = Kernel32.INSTANCE.GetConsoleMode(inh, dw) == 0 ? 1 : 0;

            if (pipe == 0) {
                // If not a pipe, configure console mode and flush buffer
                Kernel32.INSTANCE.SetConsoleMode(inh, dw.getValue() & ~(Wincon.ENABLE_MOUSE_INPUT | Wincon.ENABLE_WINDOW_INPUT));
                Kernel32.INSTANCE.FlushConsoleInputBuffer(inh);
            }
        }

        if (pipe != 0) {
            int res = 0;
            // Clear the input buffer before checking available input
            try {
                res = System.in.available();
                // System.out.println("Total bytes available is " + res);
                if (res <= 0) {
                    // System.out.println("no input, returning 0, but total bytes available is " + res);
                    // System.out.println("no input, returning 0, but total bytes available is " + dw.getValue());
                    return 0;
                }
                else {
                    // System.out.println("input available, we're returning total bytes available: "  + res);
                    // System.out.println("input available, we're returning total bytes available: "  + dw.getValue());
                    // int r = dw.getValue();
                    return res;
                }
                // while (System.in.available() > 0) {
                    
                //     System.in.read();
                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // System.out.println("pipe isn't 0" );
            // If pipe, check for input availability
            // if (!Kernel32.INSTANCE.PeekNamedPipe(inh, null, 0, null, dw, null)) {
            // if (!Kernel32.INSTANCE.PeekNamedPipe(inh, null, 0, null, dw, null)) {
            //     // System.out.println("returning 1, but total bytes available is " + dw.getValue());
            //     // System.out.println("no input, returning 0, but total bytes available is " + dw.getValue());
            //     return 0;
            // }
            // int r = res;
            // // int r = dw.getValue();
            // // System.out.println("instead, we're returning 1, but total bytes available is "  + dw.getValue());
            // System.out.println("input available, we're returning total bytes available: "  + r);
            // return r;
            System.out.println("Got an error here somehow. returning dw: " + dw.getValue());
            return dw.getValue();
        } else {
            // System.out.println("pipe is 0, dw is " + dw.getValue() );
            // If not a pipe, get number of input events
            Kernel32.INSTANCE.GetNumberOfConsoleInputEvents(inh, dw);
            return dw.getValue() <= 1 ? 0 : dw.getValue();
        }
    }
}
