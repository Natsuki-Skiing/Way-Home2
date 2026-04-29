package items.Enchantment;
import creatures.*;
import java.io.Serializable;
public interface EnchantmentEffect extends Serializable {
    String apply(Entity attacker,Entity target,double magnitude);
}

