package interfaces;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TerminalTextUtils;
import java.util.regex.Pattern;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Random;
import java.util.HashMap;
import creatures.Player;
import enums.raceEnum;
import enums.skillEnum;
public class CharacterMaker {
        private Screen screen;
        private HashMap<String,ArrayList<String>> namesMap = null;
        private TextBox txtBoxName;
        private TextBox textBoxRaceDes;
        private HashMap<String, String> raceDes;
        private WindowBasedTextGUI txtGUI;
        private Player player = null;
        private Label pointsLabel;
        private final int MAX_POINTS = 30; // Total points allowed
        private List<TextBox> attributeBoxes = new ArrayList<>(); // To track values
        private ComboBox<String> comboBoxRace;
        private HashMap<String, HashMap<skillEnum, Integer>> racialStats;
        private Window window;
        private Window raceWindow;
        private Random randGen = new Random();
        public CharacterMaker(Screen screen, WindowBasedTextGUI txtGUI) {
                this.screen = screen;

                try {
                        
                        this.txtGUI = txtGUI;   
                        this.window = new BasicWindow("Character Creation");
                        
                        this.raceWindow = new BasicWindow("Race Description");

                        Panel desPanel = new Panel(new LinearLayout());
                        Panel contentPanel = new Panel(new GridLayout(3));
                        GridLayout gridLayout = (GridLayout) contentPanel.getLayoutManager();
                        gridLayout.setHorizontalSpacing(3);

                        // --- Name Section ---
                        contentPanel.addComponent(new Label("Name:"));
                        this.txtBoxName = new TextBox(new TerminalSize(25, 1));
                        this.textBoxRaceDes = new TextBox(new TerminalSize(45, 20));
                        contentPanel.addComponent(this.txtBoxName
                                        .setLayoutData(GridLayout.createLayoutData(
                                                        GridLayout.Alignment.BEGINNING,
                                                        GridLayout.Alignment.CENTER)));

                        contentPanel.addComponent(new Button("Generate Name", () -> this.generateName()));

                        // --- Race Section ---
                        contentPanel.addComponent(new Label("Race:"));
                        loadRacialSkills();
                        List<String> raceList = new ArrayList<>(loadRaces());
                        this.comboBoxRace = new ComboBox<>(raceList);

                        this.textBoxRaceDes.setReadOnly(true);
                        desPanel.addComponent(this.textBoxRaceDes);
                        raceWindow.setComponent(desPanel);

                        comboBoxRace.setReadOnly(true);
                        comboBoxRace.setPreferredSize(new TerminalSize(20, 1));
                        contentPanel.addComponent(comboBoxRace);

                        comboBoxRace.addListener((selectedIndex, previousSelection, changedByUser) -> {
                                String selectedItem = comboBoxRace.getItem(selectedIndex);
                                String selectedRace = selectedItem.toUpperCase();
                                this.updateRaceDescription(selectedItem);
                                

                                // Reset all boxes to the new racial minimums from raceSkill.json[cite: 2]
                                HashMap<skillEnum, Integer> stats = racialStats.get(selectedRace);
                                skillEnum[] attributes = { skillEnum.STR, skillEnum.PER, skillEnum.END, skillEnum.CHR, skillEnum.AGL, skillEnum.LUK };

                                for (int i = 0; i < attributeBoxes.size(); i++) {
                                        int newMin = stats.getOrDefault(attributes[i], 0);
                                        attributeBoxes.get(i).setText(String.valueOf(newMin));
                                }
                                updatePointsLabel(); // Refresh the points label based on new totals[cite: 3]
                        });

                        
                        contentPanel.addComponent(new EmptySpace());

                       

                        contentPanel.addComponent(new Separator(Direction.HORIZONTAL)
                                        .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(3)));

                        
                        this.pointsLabel = new Label("Points Remaining: " + MAX_POINTS);
                        contentPanel.addComponent(this.pointsLabel
                                        .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(3)));

                        skillEnum[] attributeEnums = { 
                                skillEnum.STR, skillEnum.PER, skillEnum.END, 
                                skillEnum.CHR, skillEnum.AGL, skillEnum.LUK 
                        };

                        String[] attributeLabels = { 
                                "Strength", "Perception", "Endurance", 
                                "Charisma", "Agility", "Luck" 
                        };

                        for (int i = 0; i < attributeEnums.length; i++) {
                        // Pass both the Label (String) and the Stat (skillEnum)
                                contentPanel.addComponent(createAttributeSpinner(attributeLabels[i], attributeEnums[i])
                                .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(3)));
                        }

                        // Calculate initial points (since all start at 1)
                        updatePointsLabel();

                        // --- Footer / Close Section ---
                        contentPanel.addComponent(
                                        new EmptySpace()
                                                        .setLayoutData(GridLayout
                                                                        .createHorizontallyFilledLayoutData(3)));
                        contentPanel.addComponent(
                                        new Separator(Direction.HORIZONTAL)
                                                        .setLayoutData(GridLayout
                                                                        .createHorizontallyFilledLayoutData(3)));

                        contentPanel.addComponent(
                                        new Button("Finish", ()->this.finish()).setLayoutData(
                                                        GridLayout.createHorizontallyEndAlignedLayoutData(3)));

                        window.setComponent(contentPanel);

                        
                        if (!raceList.isEmpty()) {
                                comboBoxRace.setSelectedIndex(0);
                                textBoxRaceDes.setText(this.raceDes.get(raceList.get(0)));
                        }

                        raceWindow.setHints(Arrays.asList(Window.Hint.FIXED_POSITION));
                        window.setHints(Arrays.asList(Window.Hint.FIXED_POSITION));

                        
                        window.setTheme(txtGUI.getTheme());
                        if (!raceList.isEmpty()) {
                                comboBoxRace.setSelectedIndex(0);
                                this.updateRaceDescription(raceList.get(0));
                        }
                        TerminalSize raceSize = contentPanel.getPreferredSize();
                        int estWidth = raceSize.getColumns() + 5;

                        raceWindow.setPosition(new TerminalPosition(estWidth, 0));
                        window.setPosition(new TerminalPosition(0, 0));

                        txtGUI.addWindow(raceWindow);
                        txtGUI.addWindowAndWait(window);

                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private void updateRaceDescription(String raceName) {
                if (this.raceDes != null && this.raceDes.containsKey(raceName)) {
                        String rawDescription = this.raceDes.get(raceName);
                        // Wrap at 28 to leave room for the scrollbar inside the 30-width box
                        java.util.List<String> wrappedLines = TerminalTextUtils.getWordWrappedText(43, rawDescription);
                        this.textBoxRaceDes.setText(String.join("\n", wrappedLines));
                }
        }

        private void loadRacialSkills() {
                ObjectMapper objectMapper = new ObjectMapper();
                File skillJson = new File("src/jsons/raceSkill.json"); // Referencing raceSkill.json verbatim
                try {
                        this.racialStats = objectMapper.readValue(skillJson, 
                        new TypeReference<HashMap<String, HashMap<skillEnum, Integer>>>() {});
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
        public Player getPlayer(){
                return this.player;
        }
    
        private void finish() {
                if (this.getRemainingPoints() > 0) {
                        MessageDialog.showMessageDialog(txtGUI, "Character Not Complete", "Allocate all points.",
                                        MessageDialogButton.Close);
                }else if(this.txtBoxName.getText().trim().isEmpty()){
                      MessageDialog.showMessageDialog(txtGUI, "Character Not Complete", "Character Must have a name.\nIf you can't think of one, use the name generator!",
                                        MessageDialogButton.Close);  
                } else {
                        // Pull the racial HP from the loaded stats[cite: 2]
                        String selectedRace = this.comboBoxRace.getSelectedItem().toUpperCase();
                        int racialHp = this.racialStats.get(selectedRace).getOrDefault(skillEnum.HP, 100);

                        this.player = new Player(
                                        this.txtBoxName.getText(),
                                        Integer.parseInt(this.attributeBoxes.get(0).getText()), // STR
                                        Integer.parseInt(this.attributeBoxes.get(1).getText()), // PER
                                        Integer.parseInt(this.attributeBoxes.get(2).getText()), // END
                                        Integer.parseInt(this.attributeBoxes.get(3).getText()), // CHR
                                        Integer.parseInt(this.attributeBoxes.get(4).getText()), // AGL
                                        Integer.parseInt(this.attributeBoxes.get(5).getText()), // LUK
                                        raceEnum.valueOf(selectedRace),
                                        racialHp // Pass the racial HP here[cite: 2, 8]
                        );

                        //WorkAround
                        this.player.setMaxHp(this.player.getMaxHp()+racialHp);
                        this.player.setHp(this.player.getMaxHp());

                        this.raceWindow.close();
                        this.window.close();
                }
        }


        private Panel createAttributeSpinner(String attributeName,skillEnum skill) {
                Panel panel = new Panel(new GridLayout(5));
                panel.addComponent(new Label(attributeName).setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));
                String selectedRace = comboBoxRace.getText().toUpperCase();
                int racialMin = racialStats.get(selectedRace).getOrDefault(skill, 0);
               

                TextBox valueBox = new TextBox(String.valueOf(racialMin));
                valueBox.setReadOnly(true);
                valueBox.setPreferredSize(new TerminalSize(3, 1));
                this.attributeBoxes.add(valueBox); 

                panel.addComponent(new Button("-", () -> {
                        int currentVal = Integer.parseInt(valueBox.getText());
                        // Dynamically resolve the current race's minimum at click time,
                        // so the floor always reflects the selected race (fixes Vivique CHR etc.)
                        String currentRace = comboBoxRace.getSelectedItem().toUpperCase();
                        int currentMin = racialStats.get(currentRace).getOrDefault(skill, 0);
                        if (currentVal > currentMin) {
                        valueBox.setText(String.valueOf(currentVal - 1));
                        updatePointsLabel();
                        }
                }));

                panel.addComponent(valueBox);

                panel.addComponent(new Button("+", () -> {
                        int currentVal = Integer.parseInt(valueBox.getText());
                        // Max limit of 20 and check point pool
                        if (currentVal < 20 && getRemainingPoints() > 0) {
                        valueBox.setText(String.valueOf(currentVal + 1));
                        updatePointsLabel();
                        }
                }));

                return panel;
        }
        

        private int getRemainingPoints() {
                int used = 0;
                for (TextBox box : this.attributeBoxes) {
                        try {
                                used += Integer.parseInt(box.getText());
                        } catch (NumberFormatException e) {
                                used += 0;
                        }
                }
                return MAX_POINTS - used;
        }

        private void updatePointsLabel() {
                int remaining = getRemainingPoints();
                this.pointsLabel.setText("Points Remaining: " + remaining);
        }

        private void generateName() {
                        this.txtBoxName.setText(getName());
        }

        private String getName() {
                if (this.namesMap == null) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        File namesJson = new File("src/jsons/names.json");
                        try {
                                this.namesMap = objectMapper.readValue(namesJson,
                                                new TypeReference<HashMap<String,ArrayList<String>>>() {
                                                });
                        } catch (Exception e) {
                                System.err.println(e);
                                return("Joe Biden");
                        }

                }
                
                int randIndex = this.randGen.nextInt(this.namesMap.get("forenames").size());
                String name = this.namesMap.get("forenames").get(randIndex);

                randIndex = this.randGen.nextInt(this.namesMap.get("surenames").size());
                String surname = this.namesMap.get("surenames").get(randIndex);
                name = name +" "+ surname;

                return (name);
        }

        private ArrayList<String> loadRaces() {
                ObjectMapper objectMapper = new ObjectMapper();
                File raceJson = new File("src/jsons/raceDes.json");
                try {
                        this.raceDes = objectMapper.readValue(raceJson, new TypeReference<HashMap<String, String>>() {
                        });
                        return (new ArrayList<>(this.raceDes.keySet()));
                } catch (Exception e) {
                        System.err.println(e);
                        return (null);
                }
        }
}