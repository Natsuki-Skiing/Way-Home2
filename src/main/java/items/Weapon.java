package items;

import enums.itemTypeEnum;
import java.util.List;
import java.util.ArrayList;
public class Weapon extends ConditionItem {
    private double damage;
    private ArrayList<Enchantment> enchantments;
    private itemTypeEnum weaponType;
    public Weapon(String name, String description, java.math.BigDecimal value, int damage, double weight, itemTypeEnum weaponType,int maxCondition,int itemID){
        super(name, description, value,maxCondition, itemTypeEnum.WEAPON,itemTypeEnum.EQUIPPABLE,itemID);
        this.damage = damage;
        this.weaponType = weaponType;
        this.enchantments = new ArrayList<Enchantment>();
    }
    
    public  Weapon(String name, String description, double value, int damage, double weight, itemTypeEnum weaponType,int maxCondition,int itemID){
        this(name, description, new java.math.BigDecimal(value), damage, weight, weaponType, maxCondition,itemID);
    }

    public Weapon(Weapon other) {
        super(other); // Copies Item and ConditionItem fields
        if (other != null) {
            this.damage = other.damage;
            this.weaponType = other.weaponType;
            
            // DEEP COPY OF THE LIST
            // We create a NEW ArrayList containing the same enchantments.
            if (other.enchantments != null && other.enchantments.size() > 0) {
                this.enchantments = new ArrayList<>(other.enchantments);
            } else {
                this.enchantments = new ArrayList<>();
            }
        }
    }



    @Override
    public Item copy() {
        return new Weapon(this);
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

    public ArrayList<Enchantment> getEnhancements() {
        return enchantments;
    }

    public void addEnhancement(Enchantment enhancement) {
        this.enchantments.add(enhancement);
    } 

    
}
