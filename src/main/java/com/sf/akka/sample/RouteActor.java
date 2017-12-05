package com.sf.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.lightbend.akka.sample.Greeter;

/**
 * Created by gal on 5/12/17.
 */
public class RouteActor extends AbstractActor {

    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


    private String routeName;
    private ActorRef nextActor;

    public RouteActor(String routeName, ActorRef firstActor) {
        this.routeName = routeName;
        this.nextActor = firstActor;
    }

    static public Props props(String routeName, ActorRef firstActor) {
        return Props.create(RouteActor.class, () -> new RouteActor(routeName, firstActor));
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RouteMessage.class, x -> {
                    log.info("Route actor :: Received {}", x);
                    nextActor.tell(x, getSelf());
                }).matchAny(x -> {
                    log.info("Route actor :: Not matched message: {}", x);
                })
                .build();
    }
}
