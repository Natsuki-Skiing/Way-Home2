package items.Instances;

import items.templates.ConditionTemplate;

public class ConditionInstance extends ItemInstance {
    protected int condition;
    protected int baseMaxCondition;
    protected int maxModifier;
    public ConditionInstance(ConditionTemplate template, int condition) {
        super(template);
        
        this.condition = condition;
        this.baseMaxCondition = template.getMaxCondition();
        this.maxModifier = 0;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        if(condition < 0){
            this.condition = 0;
        } else if (condition > getMaxCondition()){
            this.condition = getMaxCondition();
        } else {
            this.condition = condition;
        }
    } 

    public int getMaxCondition() {
        return baseMaxCondition + this.maxModifier;
    }

    public void setMaxModifier(int maxModifier) {
        this.maxModifier = maxModifier;
    }
    public int getMaxModifier() {
        return maxModifier;
    }
    public void reduceConditionByPercent(double percent) {
        int reduction = (int)(this.condition * (percent / 100.0));
        this.condition -= reduction;
        if (this.condition < 0) {
            this.condition = 0;
        }
    }

    public boolean isBroken() {
        return this.condition <= 0;
    }

    public void addCondition(int amount) {
        this.condition += amount;
        if (this.condition > this.getCondition()) {
            this.condition = this.getCondition();
        }
    }

    public double getConditionPercentage() {
        if (getMaxCondition() == 0) {
            return 0.0;
        }
        return ((double) condition / getMaxCondition()) * 100.0;
    }

    public int pointsToFull() {
        return getMaxCondition() - condition;
    }

    @Override
    public ItemInstance copy() {
        
        ConditionInstance newInstance = new ConditionInstance((ConditionTemplate) getTemplate(), this.getCondition());

       
        newInstance.setValue(this.getValue());
        newInstance.setNameModifier(this.getNameModifier());

        
        newInstance.setMaxModifier(this.maxModifier); 
        
        return newInstance;
    }
    
}
