package items.Enchantment.EnchantmentEffects;
import items.Enchantment.EnchantmentEffect;
public class LifeLeachEffect implements EnchantmentEffect{
    @Override 
    public String apply(creatures.Entity attacker, creatures.Entity target, double magnitude) {
        double damageDealt = target.subHp(magnitude);
        attacker.addHp(damageDealt);
        return String.format("%s leaches %.2f health from %s.", attacker.getName(), damageDealt, target.getName());
    }
}