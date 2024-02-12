package pc.borbotones.logger;

import java.util.List;

public interface RdpLogger {
    void log(String message);
    void log(Object ...messages);

    void logInvariants(List<Integer> invariantsCounterList);
}
