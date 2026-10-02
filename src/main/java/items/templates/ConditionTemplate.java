package items.templates;

public class ConditionTemplate extends ItemTemplate {
    private int condition;
    private int maxCondition;
    public ConditionTemplate(String name, String description, long value, int condition, enums.itemTypeEnum type , enums.itemTypeEnum useType,String itemID) {
        super(name, description, value, type,useType,itemID);
        this.maxCondition = condition;
    }


    public ConditionTemplate(ConditionTemplate other) {
        super(other);
        if (other != null) {
            this.condition = other.condition;
            this.maxCondition = other.maxCondition;
        }
    }

    public int getMaxCondition() {
        return maxCondition;
    }
    
    public ConditionTemplate(java.util.HashMap<String, Object> itemData) {
        super(itemData);
        this.maxCondition = (Integer) itemData.get("maxCondition");
    }
}
