package com.sf.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Arrays;

/**
 * Created by gal on 5/12/17.
 */
public class FilterActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    /**
     * Filter out messages that contains certain word
     *
     * @param routeMessage: the route message
     * @param word:         the word to filter out
     * @return false if the message contains test word or true otherwise
     */
    private boolean wordFilter(RouteMessage routeMessage, String word) {

        if (routeMessage.getMessage().contains(word)) {
            return false;
        }
        return true;
    }

    private boolean testFilter(RouteMessage routeMessage) {
        return this.wordFilter(routeMessage, "test");
    }

    private boolean partyFilter(RouteMessage routeMessage) {
        return this.wordFilter(routeMessage, "party");
    }

    private int mode;
    public final static int TEST_MODE = 0;
    public final static int PARTY_MODE = 1;
    private ActorRef next;

    public FilterActor(int mode, ActorRef next) {
        this.mode = mode;
        this.next = next;
    }

    static public Props props(int mode, ActorRef next) {
        return Props.create(FilterActor.class, () -> new FilterActor(mode, next));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RouteMessage.class, x -> {
                    log.info("Filter actor :: Received {}", x);
                    boolean filterResult = true;
                    switch (this.mode) {
                        case TEST_MODE:
                            filterResult = this.testFilter(x);
                            break;
                        case PARTY_MODE:
                            filterResult = this.partyFilter(x);
                            break;
                    }
                    log.info("Filter actor :: Filtering {}, result: {}", x, filterResult);
                    if (filterResult) {
                        this.next.tell(x, getSelf());
                    }
                })
                .build();
    }
}
