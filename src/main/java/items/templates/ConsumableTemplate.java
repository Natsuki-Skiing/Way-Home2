package items.templates;
import enums.effectCounterType;
import enums.itemTypeEnum;
import items.Enchantment.*;
import items.Enchantment.TemporyEnchantment;

import java.math.BigDecimal;
import java.util.Vector;
import java.util.concurrent.locks.Lock; // Not sure if this is needed but I it's easire to do this no than later when a bunch of shit is built on top of it
public class ConsumableTemplate extends ItemTemplate {
    Vector<Enchantment> enchantmentVetor;
    int uses;
    public ConsumableTemplate(String name, String description,BigDecimal value, itemTypeEnum type, itemTypeEnum useType,int uses, int itemID,Vector<Enchantment> enchantments){
        super(name, description, value, type, useType, itemID);
        this.enchantmentVetor = enchantments;
        this.uses = uses;
    }

    
    public Vector<Enchantment> getEnchantments(){
        return(this.enchantmentVetor);
    }

    public int getUses(){
        return(this.uses);
    }

    // public synchronized void checkEffectTimers(effectCounterType eventType){
    //     for(Enchantment enchantment : this.enchantmentVetor){
    //         if(enchantment instanceof TemporyEnchantment tempEnchantment){

    //         }
    //     }
    // }
}
