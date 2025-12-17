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
    protected double hp;
    protected double maxHp;

    public Entity(String name, int strength,int perception,int endurance , int charisma ,int agility, int luck,double maxHp){
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
    
    public double getHp() {
        return this.hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getMaxHp() {
        return this.maxHp;
    }

    public void setMaxHp(double maxHp) {
        this.maxHp = maxHp;
    } 

    public double addHp(double amount) {
        this.hp += amount;
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
        return this.hp;
    } 

    public double subHp(double amount) {
        this.hp -= amount;
        if (this.hp < 0) {
            this.hp = 0;
        }
        return this.hp;
    }


}
