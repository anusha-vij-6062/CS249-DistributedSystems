//package edu.dt;

import java.util.*;

/**
 * Created by tphadke on 8/29/17.
 */
class Main {
    private Map <IProcessor, List<IProcessor> > graph;
    private Integer rootId;
    private IProcessor p0,p1,p2,p3,p4,p5;
    List<IProcessor> processorsList;
    public Integer getRootId() {
        return rootId;
    }

    Main(int rootId){
        this.rootId=rootId;
        init();
    }

    public static void main ( String args[]){
        Main m = new Main(0);
        //TODO: Choose a processor as a Root
        if(m.p0.getParent()== null && m.p0.getId() == m.getRootId()) {
            m.p0.setParent(m.p0);
        }
        //TODO: Send an initial message Message.M to this processor.
        m.p0.sendMessageToMyBuffer(Message.M,m.p0);
        m.printTree();
    }

    public void printTree()
    {
        System.out.println("--------------PRINTING TREE----------------");
        for (int i=0;i<processorsList.size();i++)
        {
            System.out.printf("\nProcess ID :"+processorsList.get(i).getId()+"\t Parent ID: " +processorsList.get(i).getParent().getId() + "\t Children : ");
            processorsList.get(i).printChildren();
        }

        System.out.println("\n-----------------------------------------");
    }
    public void init(){
        graph=new HashMap<IProcessor,List<IProcessor>>();
        p0=new Processor(0);
        p1=new Processor(1);
        p2=new Processor(2);
        p3=new Processor(3);
        p4=new Processor(4);
        p5=new Processor(5);


        processorsList = new ArrayList<>(Arrays.asList(p0,p1,p2,p3,p4,p5));

        ArrayList<IProcessor> neighbour = new ArrayList<>(Arrays.asList(p1,p2,p3));
        p0.setUnexplored(neighbour);
        graph.put( p0, (List<IProcessor>) neighbour.clone());

        neighbour=new ArrayList<>(Arrays.asList(p0,p2,p4));
        p1.setUnexplored(neighbour);
        graph.put(p1, (List<IProcessor>) neighbour.clone());

        neighbour=new ArrayList<>(Arrays.asList(p0,p1,p5));
        p2.setUnexplored(neighbour);
        graph.put(p2, (List<IProcessor>) neighbour.clone());

        neighbour=new ArrayList<>(Arrays.asList(p0));
        p3.setUnexplored(neighbour);
        graph.put(p3, (List<IProcessor>) neighbour.clone());

        neighbour=new ArrayList<>(Arrays.asList(p1,p5));
        p4.setUnexplored(neighbour);
        graph.put(p4, (List<IProcessor>) neighbour.clone());

        neighbour=new ArrayList<>(Arrays.asList(p2,p4));
        p5.setUnexplored(neighbour);
        graph.put(p5, (List<IProcessor>) neighbour.clone());



    }
}
