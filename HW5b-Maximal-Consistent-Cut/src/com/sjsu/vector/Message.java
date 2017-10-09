package com.sjsu.vector;

public class Message {
    MessageType messageType;
    VectorClock vc;
    int []cut;

    public Message(MessageType mt, VectorClock vc) {
        this.messageType=mt;
        this.vc = vc;
    }

    public Message(MessageType mt,int[]k){
        this.messageType=mt;
        this.cut=k;
    }

    public int[] getCut() {
        return cut;
    }
}
