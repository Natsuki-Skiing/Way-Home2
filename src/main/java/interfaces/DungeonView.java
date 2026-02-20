package interfaces;


import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;

import java.util.Vector;
public class DungeonView extends AbstractComponent<DungeonView> {
    
    private final int width;
    private final int height;
   
    private Vector<String> image = new Vector<String>();
    public DungeonView(int width, int height) {
        this.width = width;
        this.height = height;

    }

    public void setImage(Vector<String> image){
        this.image = image;
    }
    

    @Override
    protected ComponentRenderer<DungeonView> createDefaultRenderer() {
        return new ComponentRenderer<DungeonView>() {
            @Override
            public TerminalSize getPreferredSize(DungeonView component) {
                return new TerminalSize(width, height);
            }

            @Override
            public void drawComponent(TextGUIGraphics graphics, DungeonView component) {
                // Iterate over the grid and draw every character
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        
                        graphics.setForegroundColor(TextColor.ANSI.WHITE);
                        graphics.setBackgroundColor(TextColor.ANSI.BLACK);
                        if(image.size() > y){
                            String line = image.get(y);
                            if(line.length() > x){
                                char displayChar = line.charAt(x);
                                graphics.setCharacter(x,y,displayChar);
                            }
                        }
                    }
                }

            }
        };
    } 
    
}

