package graph.project.bacon;

public class PrintThread extends Thread {
    boolean isRunning;

    PrintThread(){
        isRunning = true;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        while(isRunning){
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 1000) {
                System.out.print("\rGenerating for " + elapsedTime / 1000.0 + " seconds");
            } else {
                System.out.print("\rGenerating for " + elapsedTime + " milliseconds");
            }
            try {
                sleep(10);
//                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println();
    }

    @Override
    public void interrupt() {
        isRunning = false;
    }
}
