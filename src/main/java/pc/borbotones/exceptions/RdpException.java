package pc.borbotones.exceptions;

import pc.borbotones.logger.Logger;

import java.util.List;

public class RdpException extends RuntimeException {


    public RdpException(String message) {
        super(message);
        Logger.getLogger().error(message);
        System.exit(1);
    }

    public RdpException(Throwable cause, String message) {
        super(message, cause);
        Logger.getLogger().error(message);
    }

    public RdpException(Throwable e, List<String> errors) {
        super(e);
        Logger.getLogger().error(errors);
    }

}
