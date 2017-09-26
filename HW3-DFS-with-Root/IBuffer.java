public interface IBuffer {
    void setMessage(Message message, IProcessor senderProcess);
    Message getMessage();
    void addObserverToMyBuffer(IProcessor caller);
}
