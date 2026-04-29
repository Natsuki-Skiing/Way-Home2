package creatures;
import enums.raceEnum;
import items.Enchantment.*;
import enums.effectCounterType;
import java.util.Vector;
import enums.skillEnum;
import java.io.Serializable;
public class Character extends Entity{
    protected raceEnum race;
    protected Vector<Enchantment> currentEnchantments;
    public Character(String name, int strength,int perception,int endurance , int charisma ,int agility, int luck,raceEnum race,int maxHp) {
        super(name, strength, perception, endurance, charisma, agility, luck,maxHp);
        this.race = race;
        this.currentEnchantments = new Vector<>();
    }

    public void addEnchantment(Enchantment newEnchantment){
        this.currentEnchantments.add(newEnchantment);
    }

    public void addEnchantments(Vector<Enchantment> enchantmentsVector){
        this.currentEnchantments.addAll(enchantmentsVector);
    }
    //Not sure if needs to be thread safe but eaire to do now than run into problems later 
    public synchronized Vector<String> applyEnchantEvent(effectCounterType event){
        Vector<String> expiredEffects = new Vector<String>();
        for(Enchantment enchantment: this.currentEnchantments){
            if(enchantment instanceof TemporyEnchantment temporyEnchantment){
                
                if(temporyEnchantment.timerEvent(event) ==0){
                    expiredEffects.add(enchantment.getName());
                    this.currentEnchantments.remove(enchantment);

                }
            }
        }

        return(expiredEffects);
    }

    public synchronized Vector<Enchantment> getCurrEnchantments(){
        return(this.currentEnchantments);
    }
    public raceEnum getRace() {
        return this.race;
    }


    private int getStatModifier(skillEnum skill){
        int buffSum = 0;
        for(Enchantment enchantment : this.currentEnchantments){
            if(enchantment.getEffect() instanceof EnchantmentBuff buff){
                if(buff.getStatToBuff() == skill){
                    buffSum += buff.getBuffAmount();
                }
            }
        }

        return(buffSum);
    }


    public int getStrength() { 
        return this.strength + getStatModifier(skillEnum.STR);
    }


    public int getPerception() { 
        
        return this.perception+ getStatModifier(skillEnum.PER);
    }

    public int getEndurance() { 
        return this.endurance+ getStatModifier(skillEnum.END);
    }

    public int getCharisma() { 
        return this.charisma+ getStatModifier(skillEnum.CHR);
    }

    public int getAgility() { 
        return this.agility+ getStatModifier(skillEnum.AGL);
    }

    public int getLuck() { 
        return this.luck+ getStatModifier(skillEnum.LUK);
    }
}
