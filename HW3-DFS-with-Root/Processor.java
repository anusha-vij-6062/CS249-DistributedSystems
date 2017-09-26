import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by tphadke on 8/29/17.
 */
class Processor implements Observer,IProcessor{
    //Each processsor has a message Buffer to store messages
    private IBuffer messageBuffer;
    private IProcessor parent;
    private Integer id;
    private List<IProcessor> unexplored;
    private List<IProcessor> children;

	/**
	* Constructor that initalizes all the fields of this Processor
	* @param pid the id of this process
	*/
    public Processor(int pid) {
        messageBuffer = new Buffer();
        id = pid; //This is an invalid value. Since only +ve values are acceptable as processor Ids.
        children = new ArrayList<>();
        //Initially it will be all the neighbors of a Processor. When a graph is created this list is populated
        unexplored = new ArrayList<>();
        messageBuffer.addObserverToMyBuffer(this);
        parent = null;
    }

	/**
	* Getter for the unexplored field
	* @return the processors in the unexplored variable of this Processor.
	*/
    public List<IProcessor> getUnexplored() {
        return unexplored;
    }

	/**
	* Gets a specific processor from the unexplored list
	* @param processId id/index of the process to select from the this.
	* @return the selected process from the unexplored list
	*/
    public IProcessor getUnexplored(int processId) {
        return unexplored.get(processId);
    }

	/**
	* Getter for the "parent" field of this processor
	* @return parent of this processor
	*/
    public IProcessor getParent() {
        return parent;
    }

	/**
	* Prints the children of this process
	* for each children it prints the id.
	*/
    public void printChildren() {
        for (int i = 0; i < children.size(); i++)
            System.out.print(children.get(i).getId() + " ");
    }

	/**
	* Adds a process to the children field of this process
	* @param p process to be added to the children field of this process
	*/
    public void addChildren(IProcessor p) {
        this.children.add(p);
    }

	/**
	* Prints the unexplored list.
	*/
    public void printUnexplored() {
        for (int i = 0; i < unexplored.size(); i++)
            System.out.print(unexplored.get(i).getId() + "  ");
    }

	/**
	* Getter for the id field of this process.
	*@return id of the current process
	*/
    public Integer getId() {
        return id;
    }

	/**
	* Setter for the unexplored field of this process 
	* @param unexplored a list of processes to be set as the unexplored field.
	*/
    public void setUnexplored(List<IProcessor> unexplored) {
        this.unexplored = unexplored;
    }

	/**
	* Setter method for the parent field.
	* @param parent process that will be this process' parent
	*/
    public void setParent(IProcessor parent) {
        this.parent = parent;
    }

	/**
	* Removes the process with the specified id from the unexplored set of this process.
	* @param processid id of the process to be removed.
	*/
    public void removeUnexplored(int processid) {
        for (int i = 0; i < unexplored.size(); i++) {
            if (processid == unexplored.get(i).getId()) {
                unexplored.remove(unexplored.get(i));
            }
        }

    }

	/**
	* Removes a specific process from the unexplored set of this process.
	* @param p the process object to be removed.
	*/
    public void removeUnexplored(IProcessor p){
        unexplored.remove(p);
    }

    private void removeFromUnexplored(IProcessor processId) {


        if (unexplored.isEmpty())
            System.out.println("Error: Removing from an empty List. Unexplored of process " + this.id + " is Empty!");
        else {
            //TODO: implement removing one processor from the list of Children
            removeUnexplored(processId);
        }
    }


	/**
	* Checks if the unexplored list is empty before removing.
	*/
    //This method will only be used by the Processor
    private void removeFromUnexplored(int processId) {


        if (unexplored.isEmpty())
            System.out.println("Error: Removing from an empty List. Unexplored of process " + this.id + " is Empty!");
        else {
            //TODO: implement removing one processor from the list of Children
            removeUnexplored(processId);
        }
    }

    //This method will add a message to this processors buffer.
    //Other processors will invoke this method to send a message to this Processor
    public void sendMessageToMyBuffer(Message message, IProcessor sender) {
        messageBuffer.setMessage(message, sender);
    }

	
    //This is analogous to recieve method.Whenever a message is dropped in its buffer this Pocesssor will respond
    //TODO: implement the logic of receive method here
    // Hint: Add switch case for each of the conditions given in receive
    /**
	* This method is invoked on the current process when the buffer notifies its observers
	* of which this process is one. The pseudocode provided in the book is implemented here.
	* @param arg we pass in the senderProcess here so this process knows who sent the message.
	*/
	@Override
    public void update(Observable observable, Object arg) {
        messageBuffer = (Buffer) observable;
        Message text = messageBuffer.getMessage();
        //Processor pj=messageBuffer.getSenderProcess();

        Processor pj = (Processor)arg;

        switch (text) {
            case M:
                if (this.parent == null)//Integer.MIN_VALUE)
                {
                    this.parent = pj;
                    System.out.println("Added Parent Process: " + this.parent.getId() + " to Process " + this.id);
                    removeFromUnexplored(pj.getId());
                    explore();
                } else {
                    removeFromUnexplored(pj.getId());
                    pj.sendMessageToMyBuffer(Message.ALREADY,this);
                }
                break;

            case PARENT:
                System.out.println("Proccess " + this.id + " Received Message Parent from " + pj.id);
                System.out.println("Adding Process " + pj.id + " to children of " + this.id);
                addChildren(pj);
                explore();
                break;
            case ALREADY:
                System.out.println("Process: " + this.id + " Received Message Already from Process " + pj.id);
                explore();
                break;
        }
    }

	/**
	* If the unexplored list is noto empty it picks a process from 
	* the unexplored set and sends the message M to it.
	* This method also leads to the termination of the algo if the 
	* unexplored list is empty and this process is not the parent.
	*/
    public void explore() {
        //TODO: implement this method.
        System.out.println("\nProcess " + this.id + " Exploring ....");
        if (!unexplored.isEmpty()) {
            IProcessor Pk = getUnexplored(0);
            removeUnexplored(Pk.getId());
            System.out.println("Process: " + this.id + " Sending message M to Process: " + Pk.getId());
            Pk.sendMessageToMyBuffer(Message.M, this);
        } else {
            if (this.parent.getId() != this.id) {
                System.out.println("Process:" + this.id + " Sending a Parent Message to Process: " + this.parent.getId());
                this.parent.sendMessageToMyBuffer(Message.PARENT, this);
            } else {
                System.out.println("Exiting..");

            }
        }

    }

}

