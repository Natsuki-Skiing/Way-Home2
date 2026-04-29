package creatures;

import enums.raceEnum;
import enums.oppInfoEnum;
import items.Instances.*;
import enums.armourSlotEnum;
import java.math.BigDecimal;
public class Opp extends Character {
    WeaponInstance weapon = null;
    oppInfoEnum type;
    int xp;
    BigDecimal deathGold;

    public Opp(Opp baseOpp, double modifier) {
        this(baseOpp.getName(),baseOpp.getStrength(),baseOpp.getPerception(),baseOpp.getEndurance(),baseOpp.getCharisma(),baseOpp.getAgility(),baseOpp.getLuck(),(int)baseOpp.getMaxHp(),baseOpp.getRace(),baseOpp.getType(),baseOpp.getDeathXp(),baseOpp.getDeathGold(),modifier);
    }

    public Opp(String name, int strength, int perception, int endurance, int charisma, int agility, int luck, int maxHp, raceEnum race, oppInfoEnum type, int xp,BigDecimal goldDrop) {
    // ugly quick to write 
    this(name, strength, perception, endurance, charisma, agility, luck, maxHp, race,  type, xp,goldDrop,1.0);
    }   
    public Opp(String name, int strength,int perception,int endurance,int charisma,int agility, int luck , int maxHp,raceEnum race,oppInfoEnum type,int xp,BigDecimal goldDrop,double statsModifier){
        super(name, 
              modStat(strength, statsModifier), 
              modStat(perception, statsModifier), 
              modStat(endurance, statsModifier), 
              modStat(charisma, statsModifier), 
              modStat(agility, statsModifier), 
              modStat(luck, statsModifier), 
              race, 
              modStat(maxHp,statsModifier));

        this.type = type;
        this.hp = this.maxHp;
        this.xp = modStat(xp, statsModifier);
        this.deathGold = goldDrop.multiply(new BigDecimal(statsModifier)).max(BigDecimal.ONE);
        
    }
    public oppInfoEnum getType(){
        return(this.type);
    }
    public int getDeathXp(){
        return(this.xp);
    }

    public BigDecimal getDeathGold(){
        return(this.deathGold);
    }

    public void setDeathGold(BigDecimal newGoldAmount){
        this.deathGold = newGoldAmount;
    }

    public void setDeathXp(int xp){
        this.xp = xp;
    }

    private static int modStat(int stat,double statsModifier){
        if(statsModifier != 1.0){
            return((int) (stat*statsModifier));
        }
        
        return(stat);
        
    }

    public WeaponInstance getWeapon(){
        return(this.weapon);
    }

    public void setWeapon(WeaponInstance weapon){
        this.weapon = weapon;
    }

}
