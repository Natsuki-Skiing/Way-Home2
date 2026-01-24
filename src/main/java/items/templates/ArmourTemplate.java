package items.templates;

import enums.itemTypeEnum;
import enums.armourSlotEnum;
public class ArmourTemplate extends ConditionTemplate {
    private itemTypeEnum armourType;
    private armourSlotEnum armourSlot;
    public ArmourTemplate(String name, String description, java.math.BigDecimal value, int damage, double weight, itemTypeEnum armourType,int maxCondition,int itemID){
        super(name, description, value, maxCondition,itemTypeEnum.ARMOR,itemTypeEnum.EQUIPPABLE, itemID);
    }


    public armourSlotEnum getArmourSlot(){
        return(this.armourSlot);
    }
}
