package pc.borbotones.exceptions;

import pc.borbotones.logger.Logger;

import java.util.List;

public class RdpException extends RuntimeException {

    // Custom exception for the RDP

    public RdpException(String message) {
        super(message);
        Logger.getLogger().error(message);
        System.exit(1);
    }

    public RdpException(Throwable e, List<String> errors) {
        super(e);
        Logger.getLogger().error(errors);
    }

}
