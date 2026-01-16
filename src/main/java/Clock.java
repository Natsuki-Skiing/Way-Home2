import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class Clock {
    private AtomicInteger secondsSinceStart;
    private long epochTime;
    private boolean running = false;
    private int sleepTime;
    private Thread backgroundThread;
    private int multiplier;
    public Clock(int secondsStartPoint,int sleepTime,int multiplier){
        this.secondsSinceStart = new AtomicInteger( secondsStartPoint);
        this.sleepTime = sleepTime;
        this.multiplier = multiplier;
        
    }

    private long getEpoch(){
        return(Instant.now().getEpochSecond());
    }
    public void pause(){
        this.running = false;
    }

    public void start(){
        this.running = true;
    }

    public boolean isRunning(){
        return(this.running);
    }

    public boolean startClock(){
        if(this.isRunning()){
            return false;
        }
        this.running = true;
        backgroundThread = new Thread(this::mainLoop);
        backgroundThread.setDaemon(true); // Ensures the app can close even if clock is running
        backgroundThread.setName("ClockBackgroundThread");
        backgroundThread.start();
        return(true);
    }
    private void mainLoop(){
        this.epochTime = getEpoch();
        while(this.running){
            updateTime();
            try {
                Thread.sleep(this.sleepTime);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    } 

    public int getSecondsTime(){
        return(this.secondsSinceStart.get());
    }

    public String getTimeString(){
        int totalSeconds = this.secondsSinceStart.get();
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void updateTime(){
        long newTime = this.getEpoch();
        int diff =(int) (newTime - this.epochTime);
        this.epochTime = newTime;
        this.secondsSinceStart.addAndGet((diff*multiplier)); 
    }
}
