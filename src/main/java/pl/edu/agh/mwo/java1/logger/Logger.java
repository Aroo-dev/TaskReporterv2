package pl.edu.agh.mwo.java1.logger;



import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 * Klasa do logowania wiadomości.
 */
public class Logger {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());

    public void logInfo(String message) {
        logger.log(Level.INFO, message);
    }

    public void logWarning(String message) {
        logger.log(Level.WARNING, message);
    }

    public void logSevere(String message) {
        logger.log(Level.SEVERE, message);
    }

    public void logSevere(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }
}
