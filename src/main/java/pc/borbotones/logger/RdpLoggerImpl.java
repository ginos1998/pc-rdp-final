package pc.borbotones.logger;

import pc.borbotones.exceptions.RdpException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RdpLoggerImpl implements RdpLogger {

    private String className;

    public RdpLoggerImpl() {

    }

    public RdpLoggerImpl(String className) {
        this.className = className;
    }

    public void log(String message) {
        try {
            String logClass = getLogClassFormat();
            String logMessage = logClass + message;
            System.out.println(logMessage);
            writeToFile(logMessage);
        } catch (RdpException e) {
            System.out.println("Error while logging a message. " + e.getMessage());
        }
    }

    public void log(Object... messages) {
        StringBuilder message = new StringBuilder();
        for (Object m : messages) {
            message.append(m).append(" | ");
        }
        log(message.toString());
    }

    private String getLogClassFormat() {
        if (className != null) {
            return "[" + className + "]  ";
        }
        return "[" + RdpLoggerImpl.class.getName() + "]  ";
    }

    private void writeToFile(String message) throws RdpException {
        try (FileWriter fileWriter = new FileWriter("log.txt", true)){
            fileWriter.write(message + "\n");
        } catch (IOException e) {
            List<String> errors = Arrays.asList("Error writing to file.", e.getMessage());
            throw new RdpException(e, errors);
        }
    }

}
