package items;
import creatures.*;
public class Enchantment {
    private final String name;
    private double magnitude;
    private int charges;
    private final EnchantmentEffect effect;

    public Enchantment(String name, double magnitude, int charges, EnchantmentEffect effect) {
        this.name = name;
        this.magnitude = magnitude;
        this.charges = charges;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }
    public double getMagnitude() {
        return magnitude;
    }
    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }
    public int getCharges() {
        return charges;
    }
    public void setCharges(int charges) {
        this.charges = charges;
    }
    public EnchantmentEffect getEffect() {
        return effect;
    }

    public boolean useEnchantment(Entity attacker, Entity target) {
        if (charges <= 0) {
            return false; // No charges left
        }
        effect.apply(attacker, target, magnitude);
        charges--;
        return true;
    }
}
