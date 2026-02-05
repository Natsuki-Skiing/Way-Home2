package Combat;
import enums.combatInfoEnum;
public class CombatInfo {
    public double damage;
    public combatInfoEnum info;
    public String itemName;

    CombatInfo(double damage, combatInfoEnum info,String itemName){
        this.damage = damage;
        this.info = info;
        this.itemName = itemName;
    }
}
