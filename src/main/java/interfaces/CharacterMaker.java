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
public class CharacterMaker {
        private Screen screen;
        private ArrayList<String> namesList = null;
        private TextBox txtBoxName;
        private TextBox textBoxRaceDes;
        private HashMap<String, String> raceDes;
        private WindowBasedTextGUI txtGUI;
        private Player player = null;
        private Label pointsLabel;
        private final int MAX_POINTS = 30; // Total points allowed
        private List<TextBox> attributeBoxes = new ArrayList<>(); // To track values
        private ComboBox<String> comboBoxRace;
        private Window window;
        private Window raceWindow;
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
                        this.textBoxRaceDes = new TextBox(new TerminalSize(30, 10));
                        contentPanel.addComponent(this.txtBoxName
                                        .setLayoutData(GridLayout.createLayoutData(
                                                        GridLayout.Alignment.BEGINNING,
                                                        GridLayout.Alignment.CENTER)));

                        contentPanel.addComponent(new Button("Generate Name", () -> this.generateName()));

                        // --- Race Section ---
                        contentPanel.addComponent(new Label("Race:"));
                        List<String> raceList = new ArrayList<>(loadRaces());
                        this.comboBoxRace = new ComboBox<>(raceList);

                        this.textBoxRaceDes.setReadOnly(true);
                        desPanel.addComponent(this.textBoxRaceDes);
                        raceWindow.setComponent(desPanel);

                        comboBoxRace.setReadOnly(true);
                        comboBoxRace.setPreferredSize(new TerminalSize(20, 1));
                        contentPanel.addComponent(comboBoxRace);

                        comboBoxRace.addListener((selectedIndex, previousSelection, changedByUser) -> {
                                String selectedRace = comboBoxRace.getItem(selectedIndex);
                                if (this.raceDes != null && this.raceDes.containsKey(selectedRace)) {
                                        this.textBoxRaceDes.setText(this.raceDes.get(selectedRace));
                                }
                        });

                        // Fill the 3rd column of the Race row so we can move to the next line
                        contentPanel.addComponent(new EmptySpace());

                        // --- ATTRIBUTES SECTION ---

                        contentPanel.addComponent(new Separator(Direction.HORIZONTAL)
                                        .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(3)));

                        // 1. Points Label
                        this.pointsLabel = new Label("Points Remaining: " + MAX_POINTS);
                        contentPanel.addComponent(this.pointsLabel
                                        .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(3)));

                        // 2. Generate Spinners for each Attribute
                        String[] attributes = { "Strength", "Perception", "Endurance", "Charisma","Agility", "Luck" };

                        for (String attr : attributes) {
                                // Create the spinner and add it to the layout, spanning all 3 columns
                                contentPanel.addComponent(createAttributeSpinner(attr)
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

                        // --- Initialization Logic ---
                        if (!raceList.isEmpty()) {
                                comboBoxRace.setSelectedIndex(0);
                                textBoxRaceDes.setText(this.raceDes.get(raceList.get(0)));
                        }

                        raceWindow.setHints(Arrays.asList(Window.Hint.FIXED_POSITION));
                        window.setHints(Arrays.asList(Window.Hint.FIXED_POSITION));

                        // Set theme early so we can calculate size
                        window.setTheme(txtGUI.getTheme());

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

        public Player getPlayer(){
                return this.player;
        }
        private void finish(){
                if(this.getRemainingPoints() >0){
                        MessageDialog.showMessageDialog(txtGUI, "Character Not Complete", "Haven't allocated all stat points", MessageDialogButton.Close);
                }else if(this.txtBoxName.getText().length() ==0){
                        MessageDialog.showMessageDialog(txtGUI, "Character Not Complete", "Character Needs a name. You can type one or use the generate button", MessageDialogButton.Close);
                }else{
                        this.player = new Player(this.txtBoxName.getText(),
                                Integer.parseInt(this.attributeBoxes.get(0).getText()),
                                Integer.parseInt(this.attributeBoxes.get(1).getText()),
                                Integer.parseInt(this.attributeBoxes.get(2).getText()),
                                Integer.parseInt(this.attributeBoxes.get(3).getText()),
                                Integer.parseInt(this.attributeBoxes.get(4).getText()),
                                Integer.parseInt(this.attributeBoxes.get(5).getText()),
                                raceEnum.valueOf(this.comboBoxRace.getText().toUpperCase()),
                                100); 
                        try{
                                this.raceWindow.close();
                                this.window.close();
                        }catch(Exception e){
                                e.printStackTrace();
                        }

                }
                
        }


        // --- NEW HELPER METHODS ---

        private Panel createAttributeSpinner(String attributeName) {
                Panel panel = new Panel(new GridLayout(5));

                // Label (takes up 2 columns space for alignment)
                panel.addComponent(new Label(attributeName)
                                .setLayoutData(GridLayout.createHorizontallyFilledLayoutData(2)));

                // The Value Box
                TextBox valueBox = new TextBox("1");
                valueBox.setReadOnly(true);
                valueBox.setPreferredSize(new TerminalSize(3, 1));
                this.attributeBoxes.add(valueBox); // Add to our tracking list

                // Minus Button
                panel.addComponent(new Button("-", () -> {
                        int currentVal = Integer.parseInt(valueBox.getText());
                        if (currentVal > 1) {
                                valueBox.setText(String.valueOf(currentVal - 1));
                                updatePointsLabel();
                        }
                }));

                // Value Display
                panel.addComponent(valueBox);

                // Plus Button
                panel.addComponent(new Button("+", () -> {
                        int currentVal = Integer.parseInt(valueBox.getText());
                        if (currentVal < 10 && getRemainingPoints() > 0) {
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
                if (this.namesList == null) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        File namesJson = new File("src/jsons/names.json");
                        try {
                                this.namesList = objectMapper.readValue(namesJson,
                                                new TypeReference<ArrayList<String>>() {
                                                });
                        } catch (Exception e) {
                                System.err.println(e);
                        }

                }
                Random randGen = new Random();
                int randIndex = randGen.nextInt(this.namesList.size());
                return (this.namesList.get(randIndex));
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
