package nameGen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NameGenerator {
    private HashMap<String,ArrayList<String>> namesMap = null;
    private Random randGen = new Random();
    public String genName(){
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
}
