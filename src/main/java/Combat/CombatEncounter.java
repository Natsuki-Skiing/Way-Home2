package Combat;
import creatures.*;
import enums.armourSlotEnum;
import interfaces.CombatInterface;
import items.EnchantmentEffects.*;
import items.Instances.ConditionInstance;
import items.Instances.SheildInstance;
import items.Instances.WeaponInstance;
import items.templates.WeaponTemplate;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.terminal.Terminal;
import java.util.Random;
import java.util.Vector;
import java.lang.Math;
public class CombatEncounter {
    Player player;
    //TODO 
    //Make an opp class
    Opp opp;
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

    public double attack(){
        WeaponInstance playerWeapon;
        double damage = 0.0;
        if(this.player.hasWeaponEquipped()){
            playerWeapon = player.getEquippedWeapon();

        }else{
            //Unarmed so like using fists? 
            //TODO If have races with claws account for this 
            int unarmedDamage =2 + this.player.getStrength();
            if(player.hasSheildEquipped()){
                unarmedDamage = (int) (unarmedDamage *0.70);
            }
            playerWeapon = new WeaponInstance(new WeaponTemplate("Unarmed", null, 0, unarmedDamage, 0, null, 0, 0), 999999);
        }

        if(!chancePass(this.opp.getEvasionChance())){
            damage = calculateOppDamage(playerWeapon.getDamage());

            //TODO  
            //Weapon effects

            opp.subHp(damage);

        
        }
        return(damage);
    }

    public double oppAttack(){
        WeaponInstance oppWeapon = opp.getWeapon();
        double damage = 0.0;

        

        if(!chancePass(this.opp.getEvasionChance())){
            damage = calculateDamage(oppWeapon.getDamage());

            //TODO  
            //Weapon effects

            player.subHp(damage);

        
        }

        return(damage);
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
        }else if(chance > 0.0 && chance < 1.0){
            double randomNumber = this.randomGen.nextDouble(100.0);
            if(randomNumber <= chance){
                pass = true;
            }
        }

        return(pass);
    }
    private double calculateDamage(double incomingDamage){
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
        incomingDamage = (incomingDamage * armourReductionFactor);


        

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
    private double calculateOppDamage(double incomingDamage){
        double armourReductionFactor = 100.0/(100.0+((this.opp.getEndurance() * 0.4)));
        armourReductionFactor =(int) armourReductionFactor * ((this.player.getLevel()*0.3));
        incomingDamage = (int)(incomingDamage * armourReductionFactor);
        return(incomingDamage);
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
