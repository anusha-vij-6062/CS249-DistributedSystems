/**
* Interface that encapsulates all the public methods of Processors or any 
* class that is to be compatabile.
*/

import java.util.List;

public interface IProcessor {
     void printChildren(); 
     Integer getId(); 
     void setUnexplored(List<IProcessor> unexplored); 
     void sendMessageToMyBuffer(Message message,IProcessor p);
     IProcessor getParent();
     void setParent(IProcessor parent);
}
