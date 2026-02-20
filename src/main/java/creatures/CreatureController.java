package creatures;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Random; 
import enums.*;
import items.ItemManager.ItemController.regStruct;
public class CreatureController {
    private static final Random randomGenerator = new Random();
    private HashMap<Integer,HashMap<enums.oppInfoEnum,Vector<Opp>>> aboveOppMap;
    private HashMap<Integer,HashMap<enums.oppInfoEnum,Vector<Opp>>> dunOppMap;
    public class oppRegStruct{
        public Opp opp;
        public int rank;
        public boolean dungeonType;
    }
    private void applyVetrancy(Opp opp){
        int number = randomGenerator.nextInt(100)+1;
        double modifier =0.0;
        String name = null;
        if(number > 75 && number <=85 ){
            modifier = 0.65;
            name = "Weaker";
        }else if(number >85 && number <=95 ){
            modifier = 1.50;
            name = "Enranged";
        }else if(number > 95){
            modifier = 2.0;
            name = "Mythic";
        }else{
            return;
        }

        opp.setName(name +" "+opp.getName());

        opp.setStrength((int) (opp.getStrength() * modifier));
        opp.setPerception((int) (opp.getPerception() * modifier));
        opp.setEndurance((int) (opp.getEndurance() * modifier));
        opp.setCharisma((int) (opp.getCharisma() * modifier));
        opp.setAgility((int) (opp.getAgility() * modifier));
        opp.setLuck((int) (opp.getLuck() * modifier));
        opp.setMaxHp((int) (opp.getMaxHp() * modifier));

        opp.setDeathXp((int)(opp.getDeathXp()*modifier));


        
        
    }
    private int getRank(){
        //TODO DEBUG Only have rats for rank 1 so just return 1 for now
        // int number = randomGenerator.nextInt(100);
        // int rank;
        // if(number < 40){
        //     rank =1;
        // }else if(number >40 && number <=65 ){
        //     rank =2;
        // }else if(number >65 && number <= 80){
        //     rank =3;
        // }else if(number >80 && number <= 92 ){
        //     rank = 4;
        // }else{
        //     rank = 5;
        // }

        // return(rank);
        return(1);
    }

    private boolean isMonster(){
        //TODO DEBUG honly have rat which is a monster so just return true for now
        // if(randomGenerator.nextInt(1) == 1){
        //     return(true);
        // }

        // return(false);
        return(true);
    }

    private boolean isDungeonType(){
        if(randomGenerator.nextInt(1) == 1){
            return(true);
        }

        return(false);
    }
    public Opp getOpp(boolean dungeonType,double modifier){
        Opp opp = getOpp(getRank(), dungeonType, isMonster(),modifier);

        return(opp);
    }
    public Opp getOpp(int rank,boolean dungeonType,boolean monster,double modifier){
        Opp opp = null;
        HashMap<Integer,HashMap<enums.oppInfoEnum,Vector<Opp>>> oppMap =null;

        if(dungeonType){
            oppMap = this.dunOppMap;
        }else{
            oppMap = this.aboveOppMap;
        }
        if(oppMap.containsKey(rank)){
            HashMap<oppInfoEnum,Vector<Opp>> rankMap = oppMap.get(rank);
            oppInfoEnum type = oppInfoEnum.HUMANOID;
            if(monster){
                type = oppInfoEnum.MONSTER;
            }

            Vector<Opp> oppVector = rankMap.get(type);

            Opp baseOpp = oppVector.get(this.randomGenerator.nextInt(oppVector.size()));
            opp = new Opp(baseOpp,modifier);
            applyVetrancy(opp);

        }



        return(opp);
    }


    private Vector<oppRegStruct>  loadOpps(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        File oppsJson = new File(filePath);
        Vector<HashMap<String, Object>> rawData = null; 
        try {
            rawData = mapper.readValue(oppsJson, new TypeReference<Vector<HashMap<String, Object>>>() {});
            
        } catch (IOException e) {
            e.printStackTrace();
            return(null);
        }

        if(rawData == null){
            return(null);
        }
        Vector<oppRegStruct> oppVector = new Vector<oppRegStruct>();


        for(HashMap<String,Object> oppData :rawData){
            raceEnum race = null;
            String oppTypeString = (String) oppData.get("type");
            
            enums.oppInfoEnum type = enums.oppInfoEnum.valueOf(oppTypeString);

            if(type != enums.oppInfoEnum.MONSTER){
                // Monsters don't have a race
                String raceString = (String) oppData.get("race");
                race = raceEnum.valueOf(raceString);
            }

            Opp opp = new Opp(
                (String) oppData.get("name"),
                (int) oppData.get("strength"),
                (int) oppData.get("perception"),
                (int) oppData.get("endurance"),
                (int) oppData.get("charisma"),
                (int) oppData.get("agility"),
                (int) oppData.get("luck"),
                (int) oppData.get("maxHp"),
                race,
                type,
                (int) oppData.get("deathXp")
            );

           
            oppRegStruct struct = new oppRegStruct();
            struct.opp = opp;
            struct.rank = (int) oppData.get("rank");
            struct.dungeonType = (boolean) oppData.get("dungeon");
            oppVector.add(struct);
        }

        return(oppVector);
    }
    public CreatureController(String aboveOppFilePath){
        this.aboveOppMap = new HashMap<Integer,HashMap<enums.oppInfoEnum,Vector<Opp>>>();
        this.dunOppMap = new HashMap<Integer,HashMap<enums.oppInfoEnum,Vector<Opp>>>();

        Vector<oppRegStruct> oppsVector = loadOpps(aboveOppFilePath);
       
        for(oppRegStruct struct : oppsVector){
            Opp opp = struct.opp;
            if(struct.dungeonType){
                if(!this.dunOppMap.containsKey(struct.rank)){
                    this.dunOppMap.put(struct.rank, new HashMap<enums.oppInfoEnum,Vector<Opp>>());
                }
                if(!this.dunOppMap.get(struct.rank).containsKey(opp.getType())){
                    this.dunOppMap.get(struct.rank).put(opp.getType(), new Vector<Opp>());
                }
                this.dunOppMap.get(struct.rank).get(opp.getType()).add(opp);
           
            }else{
                if(!this.aboveOppMap.containsKey(struct.rank)){
                    this.aboveOppMap.put(struct.rank, new HashMap<enums.oppInfoEnum,Vector<Opp>>());
                }
                if(!this.aboveOppMap.get(struct.rank).containsKey(opp.getType())){
                    this.aboveOppMap.get(struct.rank).put(opp.getType(), new Vector<Opp>());
                }
                this.aboveOppMap.get(struct.rank).get(opp.getType()).add(opp);
            }

        
    }           
}}
