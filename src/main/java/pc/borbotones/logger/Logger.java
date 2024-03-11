package pc.borbotones.logger;

import pc.borbotones.exceptions.RdpException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static Logger instance = null;
    private List<String> logs;
    private long initialTime;
    private long finalTime;

    private Logger() {
        this.logs = new ArrayList<>();
        this.initialTime = System.currentTimeMillis();
        clearLogsInFile();
    }

    public static Logger getLogger() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        logs.add(info(message));
    }

    public String info(String message) {
        String logClass = getLogClassFormat();
        String logMessage = logClass + message;
        System.out.println(logMessage);
        return logMessage;
    }

    public void error(String message) {
        log(getLogClassFormat() + message);
    }

    public void error(Object... messages) {
        String format = getLogClassFormat();
        StringBuilder message = new StringBuilder(format);
        for (Object m : messages) {
            message.append(m).append('\n');
        }
        error(message.toString());
    }

    public void logInvariants(List<Integer> invariantsCounterList) {
        for(int i = 0; i < invariantsCounterList.size(); i++){
            String message = "Invariant " + (i+1) + " has been fired " + invariantsCounterList.get(i) + " times";
            log(message);
        }
    }

    private String getLogClassFormat() {
        return System.currentTimeMillis() + " ";
    }

    /**
     * Writes the logs to a file.
     */
    public void writeLogsToFile() {
        this.finalTime = System.currentTimeMillis();
        long totalTime = this.finalTime - this.initialTime;
        try (FileWriter fileWriter = new FileWriter("log.txt", true)) {
            System.out.println("Writing logs to file...");
            fileWriter.write("Logs from " + this.initialTime + " to " + this.finalTime + ". Total time: " + totalTime + "\n\n");
            for (String log : logs) {
                fileWriter.write(log + "\n");
            }
        } catch (IOException e) {
            throw new RdpException("Error writing logs to file. " + e.getMessage());
        }
    }

    /**
     * Clears the logs in the file.
     */
    public void clearLogsInFile() {
        try (FileWriter fileWriter = new FileWriter("log.txt")) {
            System.out.println("Clearing logs in file...");
            fileWriter.write("");
        } catch (IOException e) {
            throw new RdpException("Error clearing logs in file. " + e.getMessage());
        }
    }
}
