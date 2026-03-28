package interfaces;
import com.googlecode.lanterna.TextColor;
public class GameTile {
    private TextColor backgroundColor;
    private TextColor foregroundColor;
    private char displayChar;
    private boolean isWalkable;

    public GameTile(TextColor bgColor, TextColor fgColor, char displayChar,boolean isWalkable) {
        this.backgroundColor = bgColor;
        this.foregroundColor = fgColor;
        this.displayChar = displayChar;
        this.isWalkable = isWalkable;
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
