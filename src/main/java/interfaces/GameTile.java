package interfaces;
import com.googlecode.lanterna.TextColor;
public class GameTile {
    private TextColor backgroundColor;
    private TextColor foregroundColor;
    private char displayChar;

    public GameTile(TextColor bgColor, TextColor fgColor, char displayChar) {
        this.backgroundColor = bgColor;
        this.foregroundColor = fgColor;
        this.displayChar = displayChar;
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
