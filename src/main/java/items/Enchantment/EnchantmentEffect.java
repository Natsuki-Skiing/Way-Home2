package items.Enchantment;
import creatures.*;
public interface EnchantmentEffect {
    String apply(Entity attacker,Entity target,double magnitude);
}

