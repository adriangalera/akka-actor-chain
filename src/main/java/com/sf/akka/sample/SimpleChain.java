package com.sf.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * Created by gal on 5/12/17.
 */
public class SimpleChain {

    public static void main(String[] args) throws InterruptedException {


        //Thread.sleep(10_000);

        final ActorSystem system = ActorSystem.create("akkasftest");
        try {

            final ActorRef sillyResponseActor = system.actorOf(ResponseActor.props(ResponseActor.SILLY));
            final ActorRef responseActor = system.actorOf(ResponseActor.props(ResponseActor.FORMAT_ONE_ASTERISK));
            final ActorRef processActor = system.actorOf(ProcessActor.props(2, responseActor, sillyResponseActor));
            final ActorRef filterActor = system.actorOf(FilterActor.props(FilterActor.TEST_MODE, processActor));
            final ActorRef route1 = system.actorOf(RouteActor.props("Simplest Route", filterActor));

            System.out.println("Created actors");
            int counter = 0;

            /*
            route1.tell(new RouteMessage("this is a test message"), ActorRef.noSender());
            Thread.sleep(1000);

            route1.tell(new RouteMessage("this is the message #" + counter), ActorRef.noSender());
            counter++;
            */
            /*
            while (true) {
                for (int i = 0; i < 100; i++) {
                    route1.tell(new RouteMessage("this is the message #" + counter), ActorRef.noSender());
                    Thread.sleep(1);
                }
                counter++;
                Thread.sleep(1000);
            }
            */

            route1.tell(new RouteMessage("this is the message #" + counter), ActorRef.noSender());
            counter++;
            route1.tell(new RouteMessage("this is the message #" + counter), ActorRef.noSender());
            counter++;
            route1.tell(new RouteMessage("this is the message #" + counter), ActorRef.noSender());

            while (true) {
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            system.terminate();
        }
    }
}
