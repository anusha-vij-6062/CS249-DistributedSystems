/**
 * CS249 Final Project "Happy Patients"
 * Group 2: Rashmeet Khanuja, Anusha Vijay, Steven Yen
 * Run the main method here to spawn the consumer threads
 * that will watch the queues in activeMQ for new messages
 */

public class Main {

    public static void main(String[] args){
        Runnable emailConsumer = new EmailConsumer();
        Thread consumerThread = new Thread(emailConsumer);

        Runnable analyticsConsumer = new AnalyticsConsumer();
        Thread analyticsThread = new Thread(analyticsConsumer);

        analyticsThread.start();
        consumerThread.start();
    }
}
