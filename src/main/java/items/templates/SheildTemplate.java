package items.templates;
import enums.itemTypeEnum;
import java.math.BigDecimal;
public class SheildTemplate extends ConditionTemplate{
    double damageDefensePer;
    public SheildTemplate(String name, String description, java.math.BigDecimal value, double damageDefensePer, double weight, int maxCondition,int itemID){
        super(name, description, value, maxCondition, itemTypeEnum.SHEILD, itemTypeEnum.EQUIPPABLE, itemID);
        this.damageDefensePer = damageDefensePer;
    }


    public double getDefensePer(){
        return(this.damageDefensePer);
    }

    public void setDefensePer(double defensePer){
        this.damageDefensePer = defensePer;
    }
}
