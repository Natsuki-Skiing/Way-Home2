import creatures.*;
import enums.armourSlotEnum;
import interfaces.CombatInterface;
import items.EnchantmentEffects.*;
import items.Instances.ConditionInstance;
import items.Instances.SheildInstance;

import java.util.Random;
public class CombatEncounter {
    Player player;
    //TODO 
    //Make an opp class
    Entity opp;
    CombatInterface ui;
    Random randomGen = new Random();
    public CombatEncounter(Player player, Entity opp){
        
    }

    private void reduceItemCondition(ConditionInstance item){
        item.setCondition(item.getCondition()-this.randomGen.nextInt(3));
    }
    private boolean chancePass(double chance){
        boolean pass = false;
        if(chance >1.0){
            double randomNumber = this.randomGen.nextDouble(100.0);
            if(randomNumber <= chance){
                pass = true;
            }
        }

        return(pass);
    }
    private int calculateDamage(int incomingDamage){
        //Sheild reduces incoming damage
        if(this.player.hasSheildEquipped()){
            
            if(chancePass(this.player.getBlockChance())){
                SheildInstance playerSheild = this.player.getEquippedSheild();
                incomingDamage = (int)(incomingDamage * playerSheild.getDefensePer());
                addMessagePlayer("Uses his sheild.");
            }
        }
        
        // All armour added up 
        double armourDefenseSum = 0.0;
        for(enums.armourSlotEnum slot : enums.armourSlotEnum.values()){
            if(this.player.hasArmorEquipped(slot)){
                armourDefenseSum += this.player.getEquippedArmour(slot).getDefense();
                reduceItemCondition(this.player.getEquippedArmour(slot));
            }
        }

        double armourReductionFactor = 100.0/(100.0+(armourDefenseSum+ (this.player.getEndurance() * 0.4)));
        incomingDamage = (int)(incomingDamage * armourReductionFactor);


        

        return(incomingDamage);
        

        
    }
    private void addMessageOpp(String message){
        addMessage(this.opp.getName()+" "+message);
    }
    private void addMessagePlayer(String message){
        addMessage(this.player.getName()+" "+message);
    }
    private void addMessage(String message){

    }
    
}
