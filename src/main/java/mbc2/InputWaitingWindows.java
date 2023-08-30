package mbc2;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.Wincon;
import com.sun.jna.ptr.IntByReference;

public class InputWaitingWindows {

    public static int inputWaiting() {
        int init = 0, pipe = 0;
        HANDLE inh = null;
        IntByReference dw = new IntByReference(0);

        if (init == 0) {
            // Initialize the console handle and check for pipe status
            init = 1;
            inh = Kernel32.INSTANCE.GetStdHandle(Wincon.STD_INPUT_HANDLE);  // -10 for STD_INPUT_HANDLE
            pipe = Kernel32.INSTANCE.GetConsoleMode(inh, dw) ? 1 : 0;

            if (pipe == 0) {
                // If not a pipe, configure console mode and flush buffer
                Kernel32.INSTANCE.SetConsoleMode(inh, dw.getValue() & ~(Wincon.ENABLE_MOUSE_INPUT | Wincon.ENABLE_WINDOW_INPUT));
                Kernel32.INSTANCE.FlushConsoleInputBuffer(inh);
            }
        }

        if (pipe != 0) {
            int res = 0;
            try {
                res = System.in.available();
                if (res <= 0) {
                    return 0;
                }
                else {
                    return res;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Got an error here somehow. returning dw: " + dw.getValue());
            return dw.getValue();
        } else {
            // If not a pipe, get number of input events
            Kernel32.INSTANCE.GetNumberOfConsoleInputEvents(inh, dw);
            return dw.getValue() <= 1 ? 0 : dw.getValue();
        }
    }
}
