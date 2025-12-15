package creatures;
import enums.raceEnum;
public class Character extends Entity {
    protected raceEnum race;
    public Character(String name, int strength,int perception,int endurance , int charisma ,int agility, int luck,raceEnum race) {
        super(name, strength, perception, endurance, charisma, agility, luck);
        this.race = race;
    }

    public raceEnum getRace() {
        return this.race;
    }
}
