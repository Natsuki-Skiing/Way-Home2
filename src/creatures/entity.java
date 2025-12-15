package creatures;

public class entity {
    protected String name;
    protected int level;
    protected int strength;
    protected int perception;
    protected int endurance;
    protected int chraisma;
    protected int luck;

    public entity(String name, int strength,int perception,int endurance , int chraisma , int luck){
        this.name = name;
        this.strength = strength;
        this.perception = perception;
        this.endurance = endurance;
        this.chraisma = chraisma;
        this.luck = luck;
    }
}
