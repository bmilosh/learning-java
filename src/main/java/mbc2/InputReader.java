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
        // char[] input = new char[256];
        byte[] input = new byte[256];
        byte[] inputTrimmed;
        // char[] inputTrimmed;

        // "Listen" to STDIN
        if (InputWaiting.run() != 0) {
            // Tell engine to stop calculating
            // this.config.UCI_STOPPED = true;
            System.out.println("There's input waiting");

            // Loop to read bytes from STDIN
            try {
                // System.out.println("here");
                // BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                // bytes = br.read(input, 0, 256);
                bytes = System.in.read(input, 0, Math.min(input.length, System.in.available()));
                // System.out.println("Gotten input: " + bytes);

                // Until bytes available
                // while (bytes < 0) {
                //     System.out.println("Waiting for input");
                //     bytes = br.read(input, 0, 256);
                //     System.out.println(bytes);
                // }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Find the first occurrence of '\n'
            int endc = new String(input).indexOf('\n');

            // If found new line, set value at pointer to 0
            // if (endc != -1) {
            if (bytes > 0) {
                inputTrimmed = new byte[bytes - 1];
                System.arraycopy(input, 0, inputTrimmed, 0, bytes - 1);
                // inputTrimmed = new byte[endc];
                // System.arraycopy(input, 0, inputTrimmed, 0, endc);
            } else {
                inputTrimmed = input;
            }
            String newString = new String(inputTrimmed, java.nio.charset.StandardCharsets.UTF_8);
            // String newString = new String(inputTrimmed, java.nio.charset.StandardCharsets.UTF_8).strip().split(" ")[0].strip();

            // If input is available
            // if (inputTrimmed.length > 0) {
            // if (newString.length() > 0) {
            if (bytes > 0) {
                // System.out.println("Received string: '" + newString + "'.");
                // Match UCI "quit" command
                // String newString = new String(inputTrimmed, java.nio.charset.StandardCharsets.UTF_8).strip().split(" ")[0];
                // System.out.println("Received input: " + newString + " has length: " + newString.length());
                // if (new String(inputTrimmed, java.nio.charset.StandardCharsets.UTF_8).startsWith("quit")) {
                if (newString.equals("quit")) {
                    // Tell engine to terminate execution
                    this.config.UCI_QUIT = true;
                    this.config.UCI_STOPPED = true;
                    // System.out.println("entered quit command");
                }

                // Match UCI "stop" command
                // else if (new String(inputTrimmed).startsWith("stop")) {
                else if (newString.equals("stop")) {
                    // Tell engine to terminate execution
                    this.config.UCI_QUIT = true;
                    this.config.UCI_STOPPED = true;
                    // System.out.println("entered stop command");
                    // System.out.println("Length is " + newString.length());
                }
            }
        }
    }

}
