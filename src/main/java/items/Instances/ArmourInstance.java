package items.Instances;

public class ArmourInstance extends ConditionInstance{
    public ArmourInstance(items.templates.ArmourTemplate template){
        super(template, template.getMaxCondition());
    }
}
