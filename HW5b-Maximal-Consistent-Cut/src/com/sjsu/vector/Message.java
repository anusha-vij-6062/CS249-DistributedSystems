package com.sjsu.vector;

public class Message {
    private MessageType messageType;
    private VectorClock vc;
    private int []cut;

    public Message(MessageType mt, VectorClock vc) {
        this.messageType=mt;
        this.vc = vc;
    }

    public Message(MessageType mt,int[]k){
        this.messageType=mt;
        this.cut=k;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public VectorClock getVc() {
        return vc;
    }

    public int[] getCut() {
        return cut;
    }
}
