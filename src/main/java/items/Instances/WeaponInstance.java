package items.Instances;

import java.util.ArrayList;

import items.Enchantment;
import items.templates.WeaponTemplate;

public class WeaponInstance extends ConditionInstance{
    private ArrayList<Enchantment> enchantments;
    private double damageModifier =1.0;
    public WeaponInstance(items.templates.WeaponTemplate template) {
        super(template, template.getMaxCondition());
        this.enchantments = new ArrayList<Enchantment>();
    } 

    public WeaponInstance(items.templates.WeaponTemplate template,int condition) {
        super(template, condition);
        this.enchantments = new ArrayList<Enchantment>();
    } 
    public double getDamageModifier() {
        return damageModifier;
    }

    public void setDamageModifier(double damageModifier) {
        this.damageModifier = damageModifier;
    }

    public double getDamage() {
        items.templates.WeaponTemplate weaponTemplate = (items.templates.WeaponTemplate) this.getTemplate();
        return weaponTemplate.getDamage() * this.damageModifier;
    }
    public ArrayList<Enchantment> getEnhancements() {
        return enchantments;
    }

    public void addEnhancement(Enchantment enhancement) {
        this.enchantments.add(enhancement);
    } 


    @Override
    public ItemInstance copy() {
      
        WeaponInstance newInstance = new WeaponInstance((WeaponTemplate) getTemplate(),getCondition());

        
        newInstance.setValue(this.getValue());
        newInstance.setNameModifier(this.getNameModifier());
        newInstance.setMaxModifier(this.getMaxModifier());

        
        newInstance.setDamageModifier(this.damageModifier);
        
        
        for (Enchantment e : this.enchantments) {
            newInstance.addEnhancement(e);
        }

        return newInstance;
    }
}
