package items;
import creatures.*;
public interface EnchantmentEffect {
    String apply(Entity attacker,Entity target,double magnitude);
}

