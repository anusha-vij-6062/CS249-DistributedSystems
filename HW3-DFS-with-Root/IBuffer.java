/**
* Interfacee that encapsulates all the public methods of the Buffer or
* any alternative class that is to be compatible.
*/
public interface IBuffer {
    void setMessage(Message message, IProcessor senderProcess);
    Message getMessage();
    void addObserverToMyBuffer(IProcessor caller);
}
