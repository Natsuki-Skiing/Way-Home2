package items.templates;
import enums.itemTypeEnum;
import java.math.BigDecimal;
public class SheildTemplate extends ConditionTemplate{

    public SheildTemplate(String name, String description, java.math.BigDecimal value, int damage, double weight, int maxCondition,int itemID){
        super(name, description, value, maxCondition, itemTypeEnum.SHEILD, itemTypeEnum.EQUIPPABLE, itemID);
    }
}
