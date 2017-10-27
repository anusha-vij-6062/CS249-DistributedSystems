/**
 * Leader Election in Asyn Ring O(n^2) Algorithm
 * CS 249 Team #2 Rashmeet Khanuja, Anusha Vijay, Steven Yen
 */

public class Message {

    private MessageType messageType;
    private Integer idNumber; //identifier
    private Integer phase; //the k value
    private Integer distance; //the hop/distance

    /**
     * Constructor for message
     * @param t type of the message IDENTIFIER/TERMINATE
     * @param idNum identifier of sender proc when used with IDENTIFIER message,
     *              identifier of the leader proc when used with TERMINATE message
     */
    public Message(MessageType t, Integer idNum, Integer k, Integer d){
        messageType = t;
        idNumber = idNum;
        phase = k;
        distance = d;
    }

    public MessageType getMessageType(){
        return messageType;
    }

    public Integer getIdNumber(){
        return idNumber;
    }


    public Integer getPhase() {
        return phase;
    }


    public Integer getDistance() {
        return distance;
    }

}
