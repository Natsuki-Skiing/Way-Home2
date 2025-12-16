package creatures;

import enums.raceEnum;

public class Player extends Character {
    int x = 0;
    int y = 0;
    int xp = 0;
    int xpToNextLevel = 100;
    public Player( String name, int strength,int perception,int endurance , int charisma ,int agility, int luck,raceEnum race, int maxHp) {
        super(name, strength, perception, endurance, charisma, agility, luck,race,maxHp);
    } 
    

}
