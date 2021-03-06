package org.lolhens.commons.eventmanager;

import org.lolhens.commons.hash.HashUtil;
import org.lolhens.commons.tuple.ArrayTuple;
import org.lolhens.commons.tuple.Tuple;
import org.lolhens.commons.tuple.TupleFacade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Event {
    private enum State {
        NONE(0), STARTED(1), DONE(2), CANCELLED(3);

        private final int phase;

        State(int phase) {
            this.phase = phase;
        }

        public boolean isChangeable() {
            return phase == 1;
        }

        public boolean isStarted() {
            return phase >= 1;
        }

        public boolean isDone() {
            return phase >= 2;
        }

        public boolean isCancelled() {
            return phase == 3;
        }
    }

    private final EventManager eventManager;
    private final EventType type;

    public final Tuple in;
    public final Tuple out;

    private volatile State state = State.NONE;

    private final List<ListenerContainer> listenerContainers;
    private volatile ListenerContainer currListenerContainer;

    protected Event(EventManager eventManager, EventType type, Object[] in, List<ListenerContainer> listenerContainers) {
        this.eventManager = eventManager;
        this.type = type;

        this.in = new ArrayTuple(in);
        out = new TupleFacade(new ArrayList<>());

        this.listenerContainers = new LinkedList<>();
        if (listenerContainers != null) {
            synchronized (listenerContainers) {
                this.listenerContainers.addAll(listenerContainers);
            }
        }
    }

    protected final void schedule() {
        if (state.isStarted()) return;
        state = State.STARTED;

        Iterator<ListenerContainer> iterator = listenerContainers.iterator();
        while (iterator.hasNext())
            if (!iterator.next().isFiltered(this)) iterator.remove();

        type.onEvent(this);
        if (state.isCancelled()) return;

        iterator = listenerContainers.iterator();
        while (iterator.hasNext()) {
            currListenerContainer = iterator.next();
            currListenerContainer.invoke(this);
            if (state.isCancelled()) break;
        }
        currListenerContainer = null;

        type.onEventPost(this);
        if (state.isCancelled()) return;

        state = State.DONE;
    }

    /**
     * Returns the EventManager, that handles this Event
     *
     * @return EventManager - the EventManager which handles this Event.
     */
    public final EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Returns the EventType of this Event
     *
     * @return EventType - the EventType this event is of.
     */
    public final EventType getEventType() {
        return type;
    }

    /**
     * Check if the Event is of the given EventType
     *
     * @param eventType EventType - EventType to check for.
     * @return boolean - if the EventType was equal to the given one.
     */
    public final boolean isEventType(EventType eventType) {
        return type.equals(eventType);
    }

    public final List<ListenerContainer> getListenerContainers() {
        return new ArrayList<>(listenerContainers);
    }

    public final ListenerContainer getCurrentListenerContainer() {
        return currListenerContainer;
    }

    /**
     * Check, if all the data of an Async Event is collected.
     *
     * @return boolean - true, if the event is done.
     */
    public final boolean isDone() {
        return state.isDone();
    }

    /**
     * Cancel this EventStream to stop the process of calling all the other
     * EventListeners.
     */
    public final void cancel() {
        synchronized (state) {
            if (state.isChangeable()) state = State.CANCELLED;
        }
    }

    public final void setCancelled(boolean cancelled) {
        if (cancelled) {
            synchronized (state) {
                if (state.isChangeable()) state = State.CANCELLED;
            }
        }
    }

    /**
     * Check if the event is cancelled
     *
     * @return boolean - true, if the event was cancelled.
     */
    public final boolean isCancelled() {
        return state.isCancelled();
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(eventManager, listenerContainers, type);
    }

    @Override
    public boolean equals(Object obj) {
        return HashUtil.equals(this, obj);
    }
}
