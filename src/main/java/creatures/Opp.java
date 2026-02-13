package creatures;

import enums.raceEnum;
import enums.oppInfoEnum;
import items.Instances.*;
import enums.armourSlotEnum;
public class Opp extends Character {
    WeaponInstance weapon = null;
    oppInfoEnum type;
    int xp;
    public Opp(String name, int strength,int perception,int endurance,int charisma,int agility, int luck , int maxHp,raceEnum race,double statsModifier,oppInfoEnum type,int xp){
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
        this.xp = xp;
        
    }

    public int getDeathXp(){
        return(this.xp);
    }

    public void setDeathXp(int xp){
        this.xp = xp;
    }

    private static int modStat(int stat,double statsModifier){
        return((int) (stat*statsModifier));
    }

    public WeaponInstance getWeapon(){
        return(this.weapon);
    }

    public void setWeapon(WeaponInstance weapon){
        this.weapon = weapon;
    }

}
