package de.angermueller.logging.test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import de.angermueller.logging.LoggerNameFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * Test for LoggerNameFilter.<br>
 *
 * @see de.angermueller.logging.LoggerNameFilter
 * @author Stefan Angerm&uuml;ller
 */
public class LoggerNameFilterTest {

    private LoggerContext lc;
    private QueueAppender queueAppender;

    /**
     * Setup LoggerContext and QueueAppender
     */
    @Before
    public void setUp() {
        lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.start();
        queueAppender = new QueueAppender();
        queueAppender.setContext(lc);
        queueAppender.start();
    }

    /**
     * Stop QueueAppender and LoggerContext
     */
    @After
    public void tearDown() {
        queueAppender.stop();
        lc.stop();
    }

    /**
     * Build a logger based on a name.<br>
     * Add the queue appender and set level to TRACE.<br>
     *
     * @param name The name
     * @return Logger with the given name
     */
    private Logger getLogger(String name) {
        Logger logger = (Logger) LoggerFactory.getLogger(name);
        logger.addAppender(queueAppender);
        logger.setLevel(Level.TRACE);
        return logger;
    }

    /**
     * Build a logger based on a class.
     * @param clazz The class
     * @return The logger
     */
    private Logger getLogger(Class<?> clazz) {
        Logger logger = (Logger) LoggerFactory.getLogger(clazz);
        logger.addAppender(queueAppender);
        logger.setLevel(Level.TRACE);
        return logger;
    }

    /**
     * Build a LoggerNameFilter with the given overrideLevel and pattern.<br>
     * Adds this filter to the QueueAppender and uses the current LoggerContext.<br>
     *
     * @param overrideLevel The override level
     * @param pattern The pattern
     * @return The Filter
     */
    private LoggerNameFilter buildLoggerNameFilter(Level overrideLevel, String pattern) {
        LoggerNameFilter filter = new LoggerNameFilter();
        filter.setContext(lc);
        filter.setOverrideLevel(overrideLevel);
        filter.setRegex(pattern);
        queueAppender.addFilter(filter);
        filter.start();
        return filter;
    }

    @Test
    public void testFilter() throws Exception {
        buildLoggerNameFilter(Level.INFO, "de.angermueller.logging.test.*");
        Logger privateLogger = getLogger(LoggerNameFilterTest.class);
        Logger externalLogger = getLogger(Object.class);

        privateLogger.trace("This should be logged");
        Assert.assertEquals("privateLogger.trace has NOT been logged", 1, queueAppender.size());

        privateLogger.debug("This should be logged");
        Assert.assertEquals("privateLogger.debug has NOT been logged", 2, queueAppender.size());

        privateLogger.info("This should be logged");
        Assert.assertEquals("privateLogger.info has NOT been logged", 3, queueAppender.size());

        privateLogger.warn("This should be logged");
        Assert.assertEquals("privateLogger.warn has NOT been logged", 4, queueAppender.size());

        privateLogger.error("This should be logged");
        Assert.assertEquals("privateLogger.error has NOT been logged", 5, queueAppender.size());

        externalLogger.trace("This should NOT be logged");
        Assert.assertEquals("externalLogger.trace HAS been logged", 5, queueAppender.size());

        externalLogger.debug("This should NOT be logged");
        Assert.assertEquals("externalLogger.debug HAS been logged", 5, queueAppender.size());

        externalLogger.info("This should be logged");
        Assert.assertEquals("externalLogger.info has NOT been logged", 6, queueAppender.size());

        externalLogger.warn("This should be logged");
        Assert.assertEquals("externalLogger.warn has NOT been logged", 7, queueAppender.size());

        externalLogger.error("This should be logged");
        Assert.assertEquals("externalLogger.error has NOT been logged", 8, queueAppender.size());
    }

    @Test
    public void testSetup() throws Exception {
        buildLoggerNameFilter(Level.INFO, ".*");
        Logger privateLogger = getLogger(LoggerNameFilterTest.class);
        privateLogger.trace("Hello world");
        Assert.assertEquals("Setup not working, nothing in queue found", 1, queueAppender.size());
    }
}
