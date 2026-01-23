package items.templates;

import enums.itemTypeEnum;

public class ArmourTemplate extends ConditionTemplate {
    private itemTypeEnum armourType;
    public ArmourTemplate(String name, String description, java.math.BigDecimal value, int damage, double weight, itemTypeEnum armourType,int maxCondition,int itemID){
        super(name, description, value, maxCondition,itemTypeEnum.ARMOR,itemTypeEnum.EQUIPPABLE, itemID);
    }
}
