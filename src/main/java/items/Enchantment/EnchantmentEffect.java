package items.Enchantment;
import creatures.*;

public interface EnchantmentEffect extends Serializable {
    String apply(Entity attacker,Entity target,double magnitude);
}

