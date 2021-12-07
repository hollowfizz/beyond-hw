package beyond.homework.log;

/**
 * The enum for Log level.
 */
public enum LogLevel
{

    /**
     * Trace log level.
     */
    TRACE("TRACE", 1),
    /**
     * Info log level.
     */
    INFO("INFO", 2),
    /**
     * Debug log level.
     */
    DEBUG("DEBUG", 3),
    /**
     * Warn log level.
     */
    WARN("WARN", 4),
    /**
     * Error log level.
     */
    ERROR("ERROR", 5);

    private final String level;
    private final Integer value;

    LogLevel(String level, Integer value)
    {
        this.level = level;
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Integer getValue()
    {
        return value;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public String getLevel()
    {
        return level;
    }
}
