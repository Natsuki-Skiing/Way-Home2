package items;
import enums.effectCounterType;
public class EffectTimer {
    private int effectCounter;
    private effectCounterType type;


    public EffectTimer(int counterValue, effectCounterType timerType){
        this.effectCounter = counterValue;
        this.type = timerType;
    }
    public int getCounter(){
        return(this.effectCounter);
    }

    public int decrementCounter(){
        if(this.effectCounter > 0){
            this.effectCounter --;
        }

        return(this.effectCounter);
    }

    public int decrementCounter(effectCounterType type){
        if(this.type == type){
            return(decrementCounter());
        }
        return(-1);
    }

    public effectCounterType getType(){
        return(this.type);
    }

    public boolean active(){
        if(this.effectCounter >0){
            return(true);
        }

        return(false);
    }
}
