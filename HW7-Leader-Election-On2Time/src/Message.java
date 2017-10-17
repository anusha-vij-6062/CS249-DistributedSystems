/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */

public class Message {

    MessageType messageType;
    Integer idNumber; //identifier

    /**
     * Constructor for message
     * @param t type of the message IDENTIFIER/TERMINATE
     * @param idNum identifier of sender proc when used with IDENTIFIER message,
     *              identifier of the leader proc when used with TERMINATE message
     */
    public Message(MessageType t, Integer idNum){
        messageType = t;
        idNumber = idNum;
    }

    public MessageType getMessageType(){
        return messageType;
    }

    public Integer getIdNumber(){
        return idNumber;
    }
}
