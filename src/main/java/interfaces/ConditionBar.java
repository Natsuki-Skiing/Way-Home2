package interfaces;
import items.Instances.ConditionInstance;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.ProgressBar;
public class ConditionBar extends ProgressBar {
    ConditionInstance conditionItem;
    public ConditionBar(ConditionInstance conditionItem) {
        super(0, 100, 30);
        this.conditionItem = conditionItem;
        Theme conditionTheme = SimpleTheme.makeTheme(
        true, 
        TextColor.ANSI.WHITE,  // baseForeground
        TextColor.ANSI.GREEN,  // baseBackground
        TextColor.ANSI.WHITE,  // editableForeground
        TextColor.ANSI.GREEN,  // editableBackground
        TextColor.ANSI.WHITE_BRIGHT,  // selectedForeground
        TextColor.ANSI.GREEN_BRIGHT,  // selectedBackground
        TextColor.ANSI.BLUE   // guiBackground (The 8th missing parameter!)
    );

        // Apply it specifically to this component
        this.setTheme(conditionTheme);
    }

    public void update() {
        this.setValue((int)this.conditionItem.getConditionPercentage());
    }


}
