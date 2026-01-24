package items.Instances;

import items.templates.ArmourTemplate;

public class ArmourInstance extends ConditionInstance{
    double reduction = 1.2;
    public ArmourInstance(items.templates.ArmourTemplate template){
        super(template, template.getMaxCondition());
    }

    public double getDefense(){
        ArmourTemplate armourTemplate = (ArmourTemplate) template;
        double defense = armourTemplate.getDefense();
        defense = defense * (this.reduction * (this.getConditionPercentage()/100.0));
        return(defense);
    }
}
