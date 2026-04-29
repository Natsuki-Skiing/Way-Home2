package interfaces;
import com.googlecode.lanterna.TextColor;
import java.io.Serializable;
public class GameTile implements Serializable {
    private transient TextColor backgroundColor;
    private transient TextColor foregroundColor;
    private String fgColorHex;
    private String bgColorHex;
    private char displayChar;
    private boolean isWalkable;

    public GameTile(TextColor bgColor, TextColor fgColor, char displayChar,boolean isWalkable) {
        this.backgroundColor = bgColor;
        this.foregroundColor = fgColor;
        this.bgColorHex = bgColor.toString();
        this.fgColorHex = fgColor.toString();
        this.displayChar = displayChar;
        this.isWalkable = isWalkable;
    }

    public void restoreColors() {
        this.foregroundColor = TextColor.Factory.fromString(fgColorHex);
        this.backgroundColor = TextColor.Factory.fromString(bgColorHex);
    }
    public boolean isWalkable(){
        return this.isWalkable;
    }

    public void setWalkable(boolean walkable){
        this.isWalkable = walkable;
    }
    public TextColor getBackgroundColor() {
        return backgroundColor;
    }

    public TextColor getForegroundColor() {
        return foregroundColor;
    }

    public char getDisplayChar() {
        return displayChar;
    }

    public void setBackgroundColor(TextColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public void setForegroundColor(TextColor foregroundColor) {
        this.foregroundColor = foregroundColor;
    }
    public void setDisplayChar(char displayChar) {
        this.displayChar = displayChar;
    }
}
