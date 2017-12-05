package com.sf.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by gal on 5/12/17.
 */
public class ResponseActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public final static int FORMAT_ONE_ASTERISK = 0;
    public final static int FORMAT_TWO_ASTERISK = 1;
    public final static int SILLY = 2;

    private int format;

    public ResponseActor(int format) {
        this.format = format;
    }

    static public Props props(int format) {
        return Props.create(ResponseActor.class, () -> new ResponseActor(format));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RouteMessage.class, x -> {
                    log.info("Response Actor ::: Received {}", x);
                    switch (this.format) {
                        case FORMAT_ONE_ASTERISK:
                            log.info("Response Actor ::: Output format: * {}", x);
                            break;
                        case FORMAT_TWO_ASTERISK:
                            log.info("Response Actor ::: Output format: ** {}", x);
                            break;
                        case SILLY:
                            Thread.sleep(5000);
                            log.info("Response Actor ::: SILLY: {}", x);
                            break;
                    }
                })
                .build();
    }
}
