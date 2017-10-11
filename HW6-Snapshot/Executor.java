
/**
 * CS249 Chanty-Lampoer
 * Class we added in addition to classes provided in skeleton
 * Support class for Algorithm class used to create threads for Processors.
 * @author Rashmeet Khanuja, Anusha Vijay, Steven Yen
 *
 */
public class Executor implements Runnable{

    Processor proc;
    Algorithm algo;

    public Executor(Algorithm algo, Processor proc){
        this.proc = proc;
        this.algo = algo;
    }

    public void run(){
        if(proc.getId()==1){
            try {
                algo.executionPlanP1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(proc.getId()==2){
            try {
                algo.executionPlanP2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            try {
                algo.executionPlanP3();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}