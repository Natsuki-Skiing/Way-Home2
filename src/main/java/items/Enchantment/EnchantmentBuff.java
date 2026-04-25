package items.Enchantment;
import enums.skillEnum;


import creatures.*;
public class EnchantmentBuff implements EnchantmentEffect{
    private skillEnum statToBuff;
    private int buffAmount;
    public EnchantmentBuff(skillEnum statToBuff,int buffAmount) {
        this.buffAmount = buffAmount;
        this.statToBuff = statToBuff;
    }

    @Override
    public String apply(Entity attacker, Entity target, double magnitude) {
        
        return ""; 
    }

    public skillEnum getStatToBuff() {
        return statToBuff;
    } 

    public int getBuffAmount(){
        return(this.buffAmount);
    }
}
