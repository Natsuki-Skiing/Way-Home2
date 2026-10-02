package items.templates;
import enums.itemTypeEnum;

public class SheildTemplate extends ConditionTemplate{
    double damageDefensePer;
    public SheildTemplate(String name, String description, long  value, double damageDefensePer, double weight, int maxCondition,String itemID){
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
