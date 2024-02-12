package pc.borbotones.logger;

import pc.borbotones.exceptions.RdpException;

public interface RdpLogger {
    void log(String message);
    void log(Object ...messages) ;
}
