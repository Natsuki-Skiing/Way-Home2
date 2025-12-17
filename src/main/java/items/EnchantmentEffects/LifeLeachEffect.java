package items.EnchantmentEffects;
import items.Enchantment;
import items.EnchantmentEffect;
public class LifeLeachEffect implements EnchantmentEffect{
    @Override 
    public String apply(creatures.Entity attacker, creatures.Entity target, double magnitude) {
        double damageDealt = target.takeDamage(magnitude);
        attacker.heal(damageDealt);
        return String.format("%s leaches %.2f health from %s.", attacker.getName(), damageDealt, target.getName());
    }
}  
