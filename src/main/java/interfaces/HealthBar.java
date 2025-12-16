package interfaces;
import com.googlecode.lanterna.gui2.ProgressBar;
import creatures.Entity;
public class HealthBar extends ProgressBar {
    Entity entity;
    public HealthBar(Entity entity) {
        super(0, 100, 30);
        this.entity = entity;
    }

    public void update() {
        int healthPercentage = (int) (((double) entity.getHp()) / entity.getMaxHp()) * 100;
        this.setValue(healthPercentage);
    }
    
}
