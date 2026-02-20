package clock;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class Clock {
    private AtomicInteger secondsSinceStart;
    private long lastNano;
    private boolean running = false;
    private int sleepTime;
    private Thread backgroundThread;
    private int multiplier;
    public Clock(int secondsStartPoint,int sleepTime,int multiplier){
        this.secondsSinceStart = new AtomicInteger( secondsStartPoint);
        this.sleepTime = sleepTime;
        this.multiplier = multiplier;
        
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
    public boolean isNight(){
        int hours = (this.secondsSinceStart.get() / 3600) % 24;
        return(hours >= 18 || hours < 6);
    }

    public boolean isDay(){
        return !isNight();
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
        this.lastNano = System.nanoTime();
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
        long now = System.nanoTime();
        long diffSeconds = (now - this.lastNano) / 1_000_000_000L;
        this.lastNano = now;
        if(diffSeconds > 0){
            this.secondsSinceStart.addAndGet((int)(diffSeconds * this.multiplier));
            
        }
    }
}
