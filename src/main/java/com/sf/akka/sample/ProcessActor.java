package com.sf.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gal on 5/12/17.
 */
public class ProcessActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    private int counter = 0;
    private int holdCounter;
    private List<ActorRef> next;

    public ProcessActor(int holdCounter, List<ActorRef> next) {
        this.holdCounter = holdCounter;
        this.next = next;
    }

    static public Props props(int holdCounter, ActorRef... actors) {
        return Props.create(ProcessActor.class, () -> new ProcessActor(holdCounter, Arrays.asList(actors)));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RouteMessage.class, x -> {
                    log.info("Processor actor ::Received {}", x);
                    this.counter++;
                    log.info("Processor actor :: Currently we have received {} messages", this.counter);

                    if (this.counter >= this.holdCounter) {
                        this.counter = 0;
                        for (ActorRef actor : this.next) {
                            actor.tell(new RouteMessage("Counter reach its limit"), getSender());
                        }
                    }
                })
                .build();
    }
}
