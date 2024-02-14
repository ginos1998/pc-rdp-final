package pc.borbotones.logger;

import pc.borbotones.exceptions.RdpException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Logger {
    private static Logger instance = null;
    private String className;
    private List<String> logs;

    private Logger() {
        this.logs = new ArrayList<>();
        this.className = this.getClass().getName();
        clearLogsInFile();
    }

    public static Logger getLogger() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        try {
            String logClass = getLogClassFormat();
            String logMessage = logClass + message;
            logs.add(logMessage);
            System.out.println(logMessage);
        } catch (RdpException e) {
            throw new RdpException("Error logging message. " + e.getMessage());
        }
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

    public void log(Object... messages) {
        Arrays.stream(messages)
            .filter(String.class::isInstance)
            .forEach(m -> log(m.toString()));
    }

    public void logInvariants(List<Integer> invariantsCounterList) {
        for(int i = 0; i < invariantsCounterList.size(); i++){
            String message = "Invariant " + (i+1) + " has been fired " + invariantsCounterList.get(i) + " times";
            log(message);
        }
    }

    public void logPercentages(List<Double> invariantPercentages) {
        for(int i = 0; i < invariantPercentages.size(); i++){
            String message = "Invariant " + (i+1) + " has been fired " + invariantPercentages.get(i) + " % of the times";
            log(message);
        }
    }

    private String getLogClassFormat() {
        String currentTime = String.valueOf(System.currentTimeMillis());
        if (className != null) {
            return "[" + currentTime + " " + className + "]  ";
        }
        return "[" + currentTime + " " + Logger.class.getName() + "]  ";
    }

    public void writeLogsToFile() {
        try (FileWriter fileWriter = new FileWriter("log.txt", true)) {
            System.out.println("Writing logs to file...");
            for (String log : logs) {
                fileWriter.write(log + "\n");
            }
        } catch (IOException e) {
            throw new RdpException("Error writing logs to file. " + e.getMessage());
        }
    }

    public void clearLogsInFile() {
        try (FileWriter fileWriter = new FileWriter("log.txt")) {
            System.out.println("Clearing logs in file...");
            fileWriter.write("");
        } catch (IOException e) {
            throw new RdpException("Error clearing logs in file. " + e.getMessage());
        }
    }

}
