package com.sf.akka.sample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by gal on 5/12/17.
 */
public class TestSimpleChain {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testGreeterActorSendingOfGreeting() {
        //final TestKit testProbe = new TestKit(system);
        final ActorRef responseActor = system.actorOf(ResponseActor.props(ResponseActor.FORMAT_ONE_ASTERISK));
        final ActorRef processActor = system.actorOf(ProcessActor.props(10, responseActor));
        final ActorRef filterActor = system.actorOf(FilterActor.props(FilterActor.TEST_MODE, processActor));
        final ActorRef route1 = system.actorOf(RouteActor.props("Simplest Route", filterActor));

        RouteMessage message = new RouteMessage("this is a message");
        route1.tell(message, ActorRef.noSender());

        /*
        final ActorRef helloGreeter = system.actorOf(Greeter.props("Hello", testProbe.getRef()));
        helloGreeter.tell(new Greeter.WhoToGreet("Akka"), ActorRef.noSender());
        helloGreeter.tell(new Greeter.Greet(), ActorRef.noSender());
        Printer.Greeting greeting = testProbe.expectMsgClass(Printer.Greeting.class);
        assertEquals("Hello, Akka", greeting.message);
        */
    }
}
