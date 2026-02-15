package Combat;
import creatures.*;
import enums.armourSlotEnum;
import enums.combatInfoEnum;
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
    double totalHealthLost = 0.0;
    CombatInterface ui;
    Random randomGen = new Random();
    Boolean isPlayerTurn = false;
    Vector<String> messageBuffer = new Vector<>();
    
    public CombatEncounter(Player player, Opp opp,WindowBasedTextGUI textGUI){
        this.player = player;
        this.opp = opp;

        this.ui = new CombatInterface(this,textGUI);
    }

    public Vector<String> getMessageBuffer(){
        return(this.messageBuffer);
    }

    public CombatInfo attack(){
        CombatInfo retInfo = new CombatInfo(0, null,"");
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
            if(damage > 0.0){
                
                retInfo.damage = damage;
                opp.subHp(damage);
                if(opp.getHp() <= 0.0){
                    retInfo.info = combatInfoEnum.DEATH;
                }else{
                    retInfo.info = combatInfoEnum.HIT;
                }
            }else{
                retInfo.info = combatInfoEnum.NO_DAMAGE;
            }

            if(!playerWeapon.getDisplayName().equals("Unarmed")){
                reduceItemCondition(playerWeapon);
            }

            

        
        }else{
            retInfo.info = combatInfoEnum.DOGE;
        }
        return(retInfo);
    }
    public CombatInfo oppTurn(){
        CombatInfo returnInfo = new CombatInfo(0, null,"");

        //TODO 
        //Add more complex decicison making here 
        // Use items and that 

        returnInfo = oppAttack();


        return(returnInfo);



    }
    public CombatInfo oppAttack(){
        CombatInfo returnInfo = new CombatInfo(0, null, null);
        WeaponInstance oppWeapon = opp.getWeapon();
        

        if(!chancePass(this.opp.getEvasionChance())){
            returnInfo = calculateDamage(oppWeapon.getDamage());

            //TODO  
            //Weapon effects

            player.subHp(returnInfo.damage);
            this.totalHealthLost += returnInfo.damage;

            if(player.getHp() <=0.0){
                returnInfo.info = combatInfoEnum.DEATH;
            }

        
        }else{
            returnInfo.info = combatInfoEnum.DOGE;
        }

        return(returnInfo);
    }

    public double getTotalHealthLost(){
        return(this.totalHealthLost);
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
    private CombatInfo calculateDamage(double incomingDamage){

        CombatInfo returnInfo = new CombatInfo(incomingDamage, null, null);
        //Sheild reduces incoming damage
        if(this.player.hasSheildEquipped()){
            
            if(chancePass(this.player.getBlockChance())){
                SheildInstance playerSheild = this.player.getEquippedSheild();
                incomingDamage = (incomingDamage * playerSheild.getDefensePer());
                returnInfo.info = combatInfoEnum.BLOCK;
                

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
        
        double armourReductionFactor = 100.0/(100.0+(armourDefenseSum+ (this.player.getEndurance() )));
        incomingDamage = (incomingDamage * armourReductionFactor);

        if(incomingDamage > 0.0){
            returnInfo.damage = incomingDamage * armourReductionFactor;
        }

        if(returnInfo.info != combatInfoEnum.BLOCK){
            if(returnInfo.damage <= 0.0){
                returnInfo.info = combatInfoEnum.NO_DAMAGE;
            }else{
                returnInfo.info = combatInfoEnum.HIT;
            }
        }
        

        return(returnInfo);
        

        
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
        //armourReductionFactor = armourReductionFactor * ((this.player.getLevel()*0.3));
        incomingDamage = (int)(incomingDamage * armourReductionFactor);
        return(incomingDamage);
    }
    public Opp getOpp(){
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

    public void showInterface(){
        this.ui.mainLoop();
    }

    
}
