package mbc2;

import java.io.IOException;

public class UCIComms {
    public InputReader inpRead;
    public Config config;

    public UCIComms(InputReader inputReader, Config config) {
        this.inpRead = inputReader;
        this.config = config;
    }

    public void main(String[] args) throws IOException {
        if (this.config.UCI_TIME_IS_SET && TimeUtility.getTimeMs() > this.config.UCI_STOP_TIME) {
            this.config.UCI_STOPPED = true;
            // System.out.println("In UCI Comms");
        }
        this.inpRead.main(args);
    }
}
