/**
 * CS249 Final Project "Happy Patients"
 * Group 2: Rashmeet Khanuja, Anusha Vijay, Steven Yen
 * AnalyticsConsumer that observes "HPAnalyticsQueue"
 * It continues to listen for message in the queue and
 * takes appropriate action (generate popup window) when new
 * message arrives in the queue it is observing.
 */

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.*;

public class AnalyticsConsumer implements Runnable{

    ActiveMQConnectionFactory cfact = new ActiveMQConnectionFactory("tcp://localhost:61616");

    public void run() {

        Connection connection = null;

        try {
            connection = cfact.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination analyticsQ = session.createQueue("HPAnalyticsQueue");
            //lines above are the same between conumer and producer
            MessageConsumer msgConsumer = session.createConsumer(analyticsQ);

            //infinite loop continuously listen for messages
            while(true) {
                Message msg = msgConsumer.receive();
                TextMessage txtMsg = (TextMessage) msg;
                System.out.println("New Msg in AnalyticsQueue: " + txtMsg.getText());
                JOptionPane.showMessageDialog(null, "New Msg in HPAnalyticsQueue: "+txtMsg.getText()+"\nPerform Analytics!");
            }

            //session.close();
            //connection.close();
        } catch (JMSException ex){
            ex.printStackTrace();
        }

        if(connection!=null){
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

    }
}