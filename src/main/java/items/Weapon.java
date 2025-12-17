package items;

import enums.itemTypeEnum;
import java.util.List;
import java.util.ArrayList;
public class Weapon extends ConditionItem {
    private int damage;
    private ArrayList<Enchantment> enchantments;
    public Weapon(String name, String description, java.math.BigDecimal value, int damage, double weight, itemTypeEnum weaponType,int maxCondition) {
        super(name, description, value,maxCondition, weaponType);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    

    public ArrayList<Enchantment> getEnhancements() {
        return enchantments;
    }

    public void addEnhancement(Enchantment enhancement) {
        this.enchantments.add(enhancement);
    } 
}
