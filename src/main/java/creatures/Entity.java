package creatures;

public class Entity {
    protected String name;
    protected int level;
    protected int strength;
    protected int perception;
    protected int endurance;
    protected int charisma;
    protected int luck;
    protected int agility;
    protected int hp;
    protected int maxHp;

    public Entity(String name, int strength,int perception,int endurance , int charisma ,int agility, int luck,int maxHp){
        this.name = name;
        this.strength = strength;
        this.perception = perception;
        this.endurance = endurance;
        this.charisma= charisma;
        this.luck = luck;
        this.agility = agility;
        this.maxHp = maxHp;
        this.hp = maxHp;

        
    }

   public int getStrength() { 
        return this.strength;
    }

    public String getName() {
        return this.name;
    }

    public int getPerception() { 
        
        return this.perception;
    }

    public int getEndurance() { 
        return this.endurance;
    }

    public int getCharisma() { 
        return this.charisma;
    }

    public int getAgility() { 
        return this.agility;
    }

    public int getLuck() { 
        return this.luck;
    }

    public int getLevel() {
        return this.level;
    }


    public void setStrength(int strength) { 
        this.strength = strength;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPerception(int perception) {
        this.perception = perception;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setLuck(int luck) {
        this.luck = luck;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
    public int getHp() {
        return this.hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return this.maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }


}
