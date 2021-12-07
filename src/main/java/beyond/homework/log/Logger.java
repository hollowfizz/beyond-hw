package beyond.homework.log;

import beyond.homework.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Handles database logging functionality
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class Logger
{
    private LogLevel logLevel;

    private final LogRepository logRepository;

    /**
     * Sets log level.
     *
     * @param logLevel the log level
     */
    @Value("${logger.loglevel}")
    public void setLogLevel(String logLevel)
    {
        this.logLevel = LogLevel.valueOf(logLevel);
    }

    /**
     * Instantiates a new Logger.
     *
     * @param logRepository the log repository
     */
    @Autowired
    public Logger(LogRepository logRepository)
    {
        this.logRepository = logRepository;
    }

    //private LogLevel logLevel = LogLevel.valueOf(LogLevel.class, logLevels);

    /**
     * Persists log in the database
     *
     * @param message    the message
     * @param eventLevel the event level
     * @param e          the exception
     */
    public void publish(String message, LogLevel eventLevel, Exception e)
    {
        if (eventLevel.getValue() < this.logLevel.getValue())
        {
            return;
        }

        String exceptionAsString = null;
        if (e != null)
        {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            exceptionAsString = sw.toString();
        }

        Log log = Log.builder()
                .message(message)
                .log_level(eventLevel.getLevel())
                .log_time(new Timestamp(new Date().getTime()))
                .exception(exceptionAsString)
                .build();

        this.logRepository.save(log);
    }

    /**
     * Trace.
     *
     * @param message the message
     */
    public void trace(String message)
    {
        this.publish(message, LogLevel.TRACE, null);
    }

    /**
     * Info.
     *
     * @param message the message
     */
    public void info(String message)
    {
        this.publish(message, LogLevel.INFO, null);
    }

    /**
     * Debug.
     *
     * @param message the message
     */
    public void debug(String message)
    {
        this.publish(message, LogLevel.DEBUG, null);
    }

    /**
     * Warn.
     *
     * @param message the message
     */
    public void warn(String message)
    {
        this.publish(message, LogLevel.WARN, null);
    }

    /**
     * Error.
     *
     * @param message the message
     * @param e       the exception
     */
    public void error(String message, Exception e)
    {
        this.publish(message, LogLevel.ERROR, e);
    }
}
