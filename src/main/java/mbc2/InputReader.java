package mbc2;

import java.io.IOException;

public class InputReader {
    public Config config;

    public InputReader(Config config) {
        this.config = config;
    }

    public void main(String[] args) throws IOException {
        readInput();
    }

    public void readInput() throws IOException {
        // Bytes to read holder
        int bytes = -1;

        // GUI/user input
        byte[] input = new byte[256];
        byte[] inputTrimmed;

        // "Listen" to STDIN
        if (InputWaiting.run() != 0) {
            try {
                bytes = System.in.read(input, 0, Math.min(input.length, System.in.available()));
                // System.out.println("Gotten input: " + bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // If found new line, set value at pointer to 0
            if (bytes > 0) {
                inputTrimmed = new byte[bytes - 1];
                System.arraycopy(input, 0, inputTrimmed, 0, bytes - 1);

                String newString = new String(inputTrimmed, java.nio.charset.StandardCharsets.UTF_8);
                // Match UCI "quit" command
                if (newString.equals("quit")) {
                    // Tell engine to terminate execution
                    this.config.UCI_QUIT = true;
                    this.config.UCI_STOPPED = true;
                    // System.out.println("entered quit command");
                }

                // Match UCI "stop" command
                else if (newString.equals("stop")) {
                    // Tell engine to terminate execution
                    this.config.UCI_QUIT = true;
                    this.config.UCI_STOPPED = true;
                    // System.out.println("entered stop command");
                }
            // } else {
            //     inputTrimmed = input;
            // }
            // String newString = new String(inputTrimmed, java.nio.charset.StandardCharsets.UTF_8);
            // // String newString = new String(inputTrimmed, java.nio.charset.StandardCharsets.UTF_8).strip().split(" ")[0].strip();

            // // If input is available
            // if (bytes > 0) {
            //     // Match UCI "quit" command
            //     if (newString.equals("quit")) {
            //         // Tell engine to terminate execution
            //         this.config.UCI_QUIT = true;
            //         this.config.UCI_STOPPED = true;
            //         // System.out.println("entered quit command");
            //     }

            //     // Match UCI "stop" command
            //     else if (newString.equals("stop")) {
            //         // Tell engine to terminate execution
            //         this.config.UCI_QUIT = true;
            //         this.config.UCI_STOPPED = true;
            //         // System.out.println("entered stop command");
            //     }
            }
        }
    }

}
