package items.templates;

import enums.itemTypeEnum;
import items.Enchantment.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
public class WeaponTemplate extends ConditionTemplate {
    private double damage;
    private ArrayList<Enchantment> enchantments;
    private itemTypeEnum weaponType;
    
    public WeaponTemplate(String name, String description, long  value, int damage, double weight, itemTypeEnum weaponType,int maxCondition,String itemID){
        super(name, description, value,maxCondition, itemTypeEnum.WEAPON,itemTypeEnum.EQUIPPABLE,itemID);
        this.damage = damage;
        this.weaponType = weaponType;
    
        
    }

    public WeaponTemplate(HashMap<String, Object> itemData) {
        super(itemData);
        this.damage = (Double) itemData.get("damage");
        this.weaponType = enums.itemTypeEnum.valueOf(((String) itemData.get("weaponType")).toUpperCase());
    }
    
    public  WeaponTemplate(String name, String description, double value, int damage, double weight, itemTypeEnum weaponType,int maxCondition,String itemID){
        this(name, description, value, damage, weight, weaponType, maxCondition,itemID);
    }

    public double getDamage() {
        return damage;
    }

    public itemTypeEnum getWeaponType(){
        return(this.weaponType);
    }

    public void setDamage(double damage){
        this.damage = damage;
    }

    

    
}
