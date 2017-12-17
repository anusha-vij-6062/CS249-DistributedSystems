import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * CS249 Final Project "Happy Patients", Consumer Subsystem
 * Group 2: Rashmeet Khanuja, Anusha Vijay, Steven Yen
 * Run the main method here to start the EmailConsumer and AnalyticsConsumer
 * each consumer is attached to a messageListener that receives messsage from the
 * appropriate queue in an Asynchronous fashion (instead of a blocking, infinite loop)
 * once the message arrives, the consumer takes appropriate action such as send email
 * or perform analytics (these are now represented by pop-up windows).
 */
public class Main {

    public static void main(String[] args){

        Connection connection = null;

        try{
            ActiveMQConnectionFactory cfact = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = cfact.createConnection();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //create email consumer and add listener
            Destination emailQ = session.createQueue("HPEmailQueue");
            MessageConsumer emailQConsumer = session.createConsumer(emailQ);
            emailQConsumer.setMessageListener(new EmailMsgListener());

            //create analytics consumer and add listener
            Destination analyticsQ = session.createQueue("HPAnalyticsQueue");
            MessageConsumer analyticsConsumer = session.createConsumer(analyticsQ);
            analyticsConsumer.setMessageListener(new AnalyticsMsgListener());

            connection.start();
            //if connection and session left open (and not stopped) then consumers will
            //listen continuously for messages from the queue.


        }catch(JMSException ex){
            ex.printStackTrace();
        }

    }
}
