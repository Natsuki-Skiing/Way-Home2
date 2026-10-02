package items.templates;

import enums.itemTypeEnum;
import enums.armourSlotEnum;
import java.math.BigDecimal;
import java.util.HashMap;
public class ArmourTemplate extends ConditionTemplate {
    private itemTypeEnum armourType;
    private armourSlotEnum armourSlot;
    private double blockChance;
    private double defense;
    public ArmourTemplate(String name, String description, java.math.BigDecimal value, double defense, double blockChance, itemTypeEnum armourType,armourSlotEnum armourSlot,int maxCondition,int itemID){
        super(name,description,value,maxCondition, itemTypeEnum.ARMOR,itemTypeEnum.EQUIPPABLE,itemID);
        this.armourType = armourType;
        this.blockChance = blockChance;
        this.defense = defense;
        this.armourSlot = armourSlot;       
        
       
    }

    public ArmourTemplate(HashMap<String, Object> itemData, int itemID) {
        super(itemData, itemID);
        this.armourType = itemTypeEnum.valueOf(((String) itemData.get("armourType")).toUpperCase());
        this.armourSlot = armourSlotEnum.valueOf(((String) itemData.get("armourSlot")).toUpperCase());
        this.defense = ((Number) itemData.get("defense")).doubleValue();
        this.blockChance = ((Number) itemData.get("blockChance")).doubleValue();
    }

    public double getDefense(){
        return(this.defense);
    }

    public void setDefense(double defense){
        this.defense = defense;
    }

    public double getBlockChance(){
        return(this.blockChance);
    }

    

    public void setBlockChance(double blockChance){
        this.blockChance = blockChance;
    }

    public armourSlotEnum getArmourSlot(){
        return(this.armourSlot);
    }

    public itemTypeEnum getArmourType(){
        return(this.armourType);
    }
}
