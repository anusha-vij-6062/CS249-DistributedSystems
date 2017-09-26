//package edu.dt;

import java.util.Observable;

/**
 * Created by tphadke on 8/29/17.
 */
class Buffer extends Observable implements IBuffer {
    private Message message;

    public Buffer() {
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message,IProcessor senderProcess) {
        this.message = message;
        setChanged();
        notifyObservers(senderProcess);
    }

    public void addObserverToMyBuffer(IProcessor caller)
    {
        addObserver((Processor)caller);

    }

//    public Processor getSenderProcess() {
//        return senderProcess;
//    }


}

