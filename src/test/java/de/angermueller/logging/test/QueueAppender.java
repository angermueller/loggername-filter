package de.angermueller.logging.test;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A simple appender which stores all received events in a queue.<br>
 * Used for testing.<br>
 *
 * @author Stefan Angerm&uuml;ller
 */
public class QueueAppender extends AppenderBase<ILoggingEvent> {

    private Queue<ILoggingEvent> queue = new LinkedBlockingQueue<ILoggingEvent>();

    protected void append(ILoggingEvent eventObject) {
        queue.add(eventObject);
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @see java.util.Queue
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public ILoggingEvent poll() {
        return queue.poll();
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @see java.util.Queue
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public ILoggingEvent peek() {
        return queue.peek();
    }

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @see java.util.Collection
     * @return the number of elements in this collection
     */
    public int size() {
        return queue.size();
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @see java.util.Collection
     * @return <tt>true</tt> if this collection contains no elements
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
