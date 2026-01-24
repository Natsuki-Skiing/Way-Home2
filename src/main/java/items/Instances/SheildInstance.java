package items.Instances;

import items.templates.SheildTemplate;

public class SheildInstance extends ConditionInstance {
    double reductionRate = 1.25;
    public SheildInstance(SheildTemplate template,int baseMaxCondition){
        super(template, baseMaxCondition);
    }

    public double getDefensePer(){

        SheildTemplate sheildTemplate = (SheildTemplate) template;
        double defensePer = sheildTemplate.getDefensePer();

        defensePer = defensePer * (this.reductionRate * (getConditionPercentage()/100.0));
        return(defensePer);
    }
    
}
