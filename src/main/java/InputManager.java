import enums.inputActionEnum;
import java.util.HashMap;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
public class InputManager {
    private Terminal terminal;
    private HashMap<KeyType, inputActionEnum> specialBindings;
    private HashMap<Character, inputActionEnum> charBindings;
    public InputManager(Terminal terminal) {
        this.terminal = terminal;
        this.specialBindings = new HashMap<>();
        this.charBindings = new HashMap<>();
        loadKeyBindings("src/jsons/keyBindings.json");
        // Default key bindings
       
    }

    private void loadKeyBindings(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        File mapJson = new File(filePath);

        try {
            HashMap<String, String> bindings = mapper.readValue(mapJson, new TypeReference<HashMap<String, String>>(){});
            for (HashMap.Entry<String, String> entry : bindings.entrySet()) {
                String key = entry.getKey();
                String action = entry.getValue();
                inputActionEnum actionEnum = inputActionEnum.valueOf(action);
                if (key.length() == 1) {
                    charBindings.put(key.charAt(0), actionEnum);
                } else {
                    KeyType keyType = KeyType.valueOf(key);
                    specialBindings.put(keyType, actionEnum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            }
    }

    public inputActionEnum getInput(){
        inputActionEnum action = null;
        try{
            KeyStroke input = this.terminal.readInput();
            if(input.getKeyType() == KeyType.Character){
                action = charBindings.get(input.getCharacter());
            }else{
                action = specialBindings.get(input.getKeyType());
            }
            return action;
        }catch(Exception e){
            e.printStackTrace();
            return null;
    }

    }
}