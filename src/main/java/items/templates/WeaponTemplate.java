package items.templates;

import enums.itemTypeEnum;
import items.Enchantment;

import java.util.List;
import java.util.ArrayList;
public class WeaponTemplate extends ConditionTemplate {
    private double damage;
    private ArrayList<Enchantment> enchantments;
    private itemTypeEnum weaponType;
    public WeaponTemplate(String name, String description, java.math.BigDecimal value, int damage, double weight, itemTypeEnum weaponType,int maxCondition,int itemID){
        super(name, description, value,maxCondition, itemTypeEnum.WEAPON,itemTypeEnum.EQUIPPABLE,itemID);
        this.damage = damage;
        this.weaponType = weaponType;
        
    }
    
    public  WeaponTemplate(String name, String description, double value, int damage, double weight, itemTypeEnum weaponType,int maxCondition,int itemID){
        this(name, description, new java.math.BigDecimal(value), damage, weight, weaponType, maxCondition,itemID);
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
