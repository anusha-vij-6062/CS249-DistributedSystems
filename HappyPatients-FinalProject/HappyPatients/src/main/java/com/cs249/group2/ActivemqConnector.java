package com.cs249.group2;

//imports for activemq
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.jms.*;

public class ActivemqConnector {

    /**
     * Sends a msgToSend to the queue destQueue in ActiveMQ
     * If the specified queue does not exist in ActiveMQ, this method will create a new
     * queue with that name. If the specified queue exists, then it will add to it.
     * @param destQueue the queue in ActiveMQ to send our message to
     * @param msgToSend the message to be sent to the queue
     */
    public static void sendMessageToQueue(String destQueue, String msgToSend){

        ActiveMQConnectionFactory connectFact = new ActiveMQConnectionFactory("tcp://localhost:61616");

        try {
            // First create a connection
            Connection connection = connectFact.createConnection();
            connection.start();

            // Now create a Session
            javax.jms.Session session = connection.createSession(false,
                    javax.jms.Session.AUTO_ACKNOWLEDGE);

            // Let's create a queue. If the queue exist,
            // it will return that
            Destination destination = session.createQueue(destQueue);

            // Create a MessageProducer from
            // the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            // Create a messages for the current queue
            //String text = "New req to endpoint! with param= "+msgToSend;
            TextMessage message = session.createTextMessage(msgToSend);

            // Send the message to queue
            producer.send(message);

            // Do the cleanup
            session.close();
            connection.close();

        } catch (JMSException jmse) {
            System.out.println("!!!JMS Exception: " + jmse.getMessage());
        }
    }

}

