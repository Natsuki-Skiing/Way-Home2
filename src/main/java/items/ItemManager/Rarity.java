package items.ItemManager;

public class Rarity {
    double mainStatModifier;
    double conditionModifier;
    double valueModifier;
    String name;
    private double normMod(double modifier){
        if(modifier < 0){
            modifier += 1.0;
        }
        return(modifier);
    }
    public Rarity(String name,double mainStatModifier,double conditionModifier, double valueModifier){
        this.mainStatModifier = normMod(mainStatModifier);
        this.conditionModifier = normMod(conditionModifier);
        this.valueModifier = normMod(valueModifier);
        this.name = name;
        
    }

    public String getName(){
        return(this.name);
    }

    public double getMainModifier(){
        return(this.mainStatModifier);
    }

    public double getConditionModifier(){
        return(this.conditionModifier);
    }

    public double getValueModifier(){
        return(this.valueModifier);
    }
}
