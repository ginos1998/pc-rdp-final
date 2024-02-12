package pc.borbotones.logger;

public class LoggerFactory {
    private LoggerFactory() {
// private constructor
    }

    public static RdpLogger getLogger() {
        return new RdpLoggerImpl();
    }

    public static RdpLogger getLogger(Class<?> clazz) {
        return new RdpLoggerImpl(clazz.getName());
    }

}
