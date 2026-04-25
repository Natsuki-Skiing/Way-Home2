package items.Enchantment;
import enums.effectCounterType;
import items.EffectTimer;
public class TemporyEnchantment extends Enchantment{
    private EffectTimer timer;

    public TemporyEnchantment(String name, double magnitude,EffectTimer effectTimer,EnchantmentEffect effect){
        
        super(name, magnitude,-1,effect);
        this.timer = effectTimer;
    }

    public TemporyEnchantment(String name, double magnitude, int effectCharges, effectCounterType CounterType, EnchantmentEffect effect){
        this(name, magnitude,new EffectTimer(effectCharges, CounterType),effect);
    }


    public int timerEvent(effectCounterType event){
        return(this.timer.decrementCounter(event));
    }

    public int getTimerValue(){
        return(this.timer.getCounter());
    }
}
