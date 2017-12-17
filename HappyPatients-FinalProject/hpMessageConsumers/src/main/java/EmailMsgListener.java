import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.swing.*;

/**
 * CS249 - Happy Patient Project, Consumer Subsystem
 * MessageListener for the EmailConsumer. It asychronously receives
 * messages from the queue "HPEmailQueue" (as opposed to blocking receive in loop)
 */
public class EmailMsgListener implements MessageListener {

    /**
     * This method is called automatically when a message arrives in the queue.
     * It then performs the appropriate action. In this case simply spawn a popup window
     * representing the action of sending an email.
     * @param msg The message retrieved from the queue
     */
    public void onMessage(Message msg){
        TextMessage txtMsg = (TextMessage) msg;
        try{
            String displayText = "New Msg in HPEmailQueue: "+txtMsg.getText()+"\nSend Email!";
            displayText += "\n(triggered using MessageListener)\n";
            System.out.println(displayText);
            JOptionPane.showMessageDialog(null,displayText );
        }catch(JMSException ex){
            ex.printStackTrace();
        }
    }

}
