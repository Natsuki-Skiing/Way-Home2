package Combat;
import creatures.*;
import enums.armourSlotEnum;
import interfaces.CombatInterface;
import items.EnchantmentEffects.*;
import items.Instances.ConditionInstance;
import items.Instances.SheildInstance;
import items.Instances.WeaponInstance;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.terminal.Terminal;
import java.util.Random;
import java.util.Vector;
import java.lang.Math;
public class CombatEncounter {
    Player player;
    //TODO 
    //Make an opp class
    Entity opp;
    CombatInterface ui;
    Random randomGen = new Random();
    Boolean isPlayerTurn = false;
    Vector<String> messageBuffer = new Vector<>();
    
    public CombatEncounter(Player player, Entity opp,WindowBasedTextGUI textGUI){
        this.player = player;
        this.opp = opp;

        this.ui = new CombatInterface(this,textGUI);
    }

    public Vector<String> getMessageBuffer(){
        return(this.messageBuffer);
    }

    public void attack(){
        if(this.player.hasWeaponEquipped()){
            WeaponInstance playerWeapon = player.getEquippedWeapon();
            
        }
        

    }

    public void clearMessageBuffer(){
        this.messageBuffer.clear();
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
    public Player getPlayer(){
        return(this.player);
    }
    public boolean flee(){
        boolean success = false;
        int playerAg = this.player.getAgility();
        int oppAg = this.opp.getAgility();

        double chance = 12.0 +playerAg;
        chance += this.player.getLuck()*0.5;

        chance += playerAg - oppAg;
        

        
        if (chance >= 100){
            success = true;
        }else if(chance >0){
            success = chancePass(chance);
        }
        

        return(success);
    }

    public Entity getOpp(){
        return(this.opp);
    }
    private void addMessageOpp(String message){
        addMessage(this.opp.getName()+" "+message);
    }
    private void addMessagePlayer(String message){
        addMessage(this.player.getName()+" "+message);
    }
    private void addMessage(String message){
        this.messageBuffer.add(message);
    }
    
}
