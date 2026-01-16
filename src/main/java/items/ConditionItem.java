package items;

public class ConditionItem extends Item {
    private int condition;
    private int maxCondition;
    public ConditionItem(String name, String description, java.math.BigDecimal value, int condition, enums.itemTypeEnum type , enums.itemTypeEnum useType,int itemID) {
        super(name, description, value, type,useType,itemID);
        this.condition = condition;
        this.maxCondition = condition;
    }


    public ConditionItem(ConditionItem other) {
        super(other);
        if (other != null) {
            this.condition = other.condition;
            this.maxCondition = other.maxCondition;
        }
    }

    @Override
    public Item copy() {
        return new ConditionItem(this);
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    } 

    public int getMaxCondition() {
        return maxCondition;
    }

    public void setMaxCondition(int maxCondition) {
        this.maxCondition = maxCondition;
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
        if (this.condition > this.maxCondition) {
            this.condition = this.maxCondition;
        }
    }

    public double getConditionPercentage() {
        if (maxCondition == 0) {
            return 0.0;
        }
        return ((double) condition / maxCondition) * 100.0;
    }

    public int pointsToFull() {
        return maxCondition - condition;
    }
    
}
