package de.angermueller.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This filter filters logging events based on their logger names.<br/>
 * Use this to get more (e.g. TRACE-) logging from your own classes while testing and suppress other loggers.
 *
 * @author Stefan Angerm&uuml;ller
 * @version 1.0.0
 * @see ch.qos.logback.core.filter.Filter
 */
public class LoggerNameFilter extends Filter<ILoggingEvent> {

    private Pattern pattern = Pattern.compile(".*");
    private Level overrideLevel = Level.WARN;

    /**
     * The pattern for the events name to match.
     * @return The pattern
     */
    public String getRegex() {
        return pattern.pattern();
    }

    /**
     * Sets the regular expression for the logger name to match.<br/>
     * Any event with a logger name matching this expression will be accepted by this filter.<br/>
     * <b>Default:</b> <tt>.*</tt> (will match any string)
     * @param regex The regular expression, must not be <tt>null</tt>
     * @throws PatternSyntaxException If the expression's syntax is invalid
     */
    public void setRegex(String regex) {
        if(regex == null)
            throw new NullPointerException("Regex must not be null");
        this.pattern = Pattern.compile(regex);
    }

    /**
     * The override level.
     * @return The level
     */
    public Level getOverrideLevel() {
        return overrideLevel;
    }

    /**
     * Sets the override level. <br/>
     * Any event with a level equal or greater this level will be accepted by this filter.<br/>
     * If this level is set to <tt>null</tt>, the level of events will not have any effect on the result.<br/>
     * <b>Default:</b> Level.WARN
     * @param overrideLevel The level
     */
    public void setOverrideLevel(Level overrideLevel) {
        this.overrideLevel = overrideLevel;
    }

    /**
     * @see ch.qos.logback.core.filter.Filter
     * @param event The event to decide upon.
     * @return The result
     */
    @Override
    public FilterReply decide(ILoggingEvent event) {
        if(!isStarted())
            return FilterReply.NEUTRAL;
        return (overrideLevel != null && event.getLevel().isGreaterOrEqual(overrideLevel)) || pattern.matcher(event.getLoggerName()).matches() ? FilterReply.ACCEPT : FilterReply.DENY;
    }

}
