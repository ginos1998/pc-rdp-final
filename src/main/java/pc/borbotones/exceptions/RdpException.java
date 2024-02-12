package pc.borbotones.exceptions;

import pc.borbotones.logger.LoggerFactory;
import pc.borbotones.logger.RdpLogger;

import java.util.List;

public class RdpException extends Exception {

    private final RdpLogger logger = LoggerFactory.getLogger(RdpException.class);

    public RdpException(String message) {
        super(message);
        logger.log(message);
    }

    public RdpException(String message, Throwable cause) {
        super(message, cause);
        logger.log(message);
    }

    public RdpException(Throwable e, List<String> errors) {
        super(e);
        logger.log(errors);
    }

}
