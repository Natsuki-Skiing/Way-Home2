package items.Instances;

import java.util.Vector;

import enums.raceEnum;
import items.Enchantment.Enchantment;
import items.templates.*;
public class ConsumableInstance extends ItemInstance {
    int uses;
    
    public ConsumableInstance(items.templates.ConsumableTemplate template){
        super(template);
        this.uses = template.getUses();
    }

    public Vector<Enchantment> use(){
        this.uses --;
        ConsumableTemplate template = (ConsumableTemplate) this.template;
        return(template.getEnchantments());
    }

    public int getUses(){
        return(this.uses);
    }
}
