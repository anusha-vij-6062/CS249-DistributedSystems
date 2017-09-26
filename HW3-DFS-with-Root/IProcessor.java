import java.util.List;

public interface IProcessor {
     void printChildren(); //Needed
     Integer getId(); //needed
     void setUnexplored(List<IProcessor> unexplored); //needed
     void sendMessageToMyBuffer(Message message,IProcessor p); //needed
     IProcessor getParent(); //Needed
     void setParent(IProcessor parent); //needed
}
