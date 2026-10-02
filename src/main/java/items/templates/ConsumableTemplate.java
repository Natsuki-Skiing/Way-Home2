package items.templates;
import enums.effectCounterType;
import enums.itemTypeEnum;
import items.Enchantment.*;
import items.Enchantment.TemporyEnchantment;


import java.util.Vector;

public class ConsumableTemplate extends ItemTemplate {
    Vector<Enchantment> enchantmentVetor;
    int uses;
    public ConsumableTemplate(String name, String description,long value, itemTypeEnum type, itemTypeEnum useType,int uses, String itemID,Vector<Enchantment> enchantments){
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
