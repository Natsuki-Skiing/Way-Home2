package items.ItemManager;
import enums.*;
import items.*;
import items.Instances.ConditionInstance;
import items.Instances.ItemInstance;
import items.Instances.WeaponInstance;
import items.templates.ConditionTemplate;
import items.templates.ItemTemplate;
import items.templates.WeaponTemplate;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.atomic.AtomicInteger;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.NavigableMap;
import items.ChestClasses.Chest;
public class ItemController {
    private int maxTier = 3;
    private HashMap<enums.itemTypeEnum,HashMap<Integer,Vector<ItemTemplate>>> mainItemMap;
    private HashMap<String,ItemTemplate> idItemMap;
    private HashMap<String,Integer> rarityItemMap;
    private static final Random randomGenerator = new Random();
    private final NavigableMap<Integer, Rarity> dropTable = new TreeMap<>();
    // Grey will just be the base item
    private Rarity green;
    private Rarity blue;
    private Rarity purple;
    private Rarity gold;
    private int totalWeight = 0;


    public class regStruct{
        public ItemTemplate template;
        public int tier;
    }

    public ItemController(){
        this.idItemMap = new HashMap<String,ItemTemplate>();
        this.mainItemMap = new HashMap<>();
        this.rarityItemMap = new HashMap<String,Integer>();
        this.green = new Rarity("Green",1.15,1.1,1.05);
        this.blue = new Rarity("Blue",1.25,1.25,1.11);
        this.purple = new Rarity("Purple",1.5,1.35,1.2);
        this.gold = new Rarity("Gold",1.8,1.55,1.5);

        addRarity(68, null); 
        addRarity(15, green);
        addRarity(10, blue);
        addRarity(5, purple);
        addRarity(2, gold);

        populateItemMap("src/jsons/items/weapons.json");
        populateItemMap("src/jsons/items/armour.json");
        populateItemMap("src/jsons/items/food.json");
    }
    private void addRarity(int weight, Rarity rarity){
        if(weight > 0){
            this.totalWeight += weight;
            this.dropTable.put(this.totalWeight, rarity);
        }
    }

    public Rarity getRarity(){
        int value = randomGenerator.nextInt(totalWeight);
        
        
        return dropTable.higherEntry(value).getValue();
    }
    private void registerItem(regStruct itemStruct){
        ItemTemplate item = itemStruct.template;
        int tier = itemStruct.tier;
        // Ensure the top-level key (Type) exists
        if (!this.mainItemMap.containsKey(item.getType())) {
            this.mainItemMap.put(item.getType(), new HashMap<>());
        }

        //Get the inner map for that type
        HashMap<Integer, Vector<ItemTemplate>> tierMap = this.mainItemMap.get(item.getType());

        //  Ensure the tier key exists in the inner map
        if (!tierMap.containsKey(tier)) {
            tierMap.put(tier, new Vector<>());
        }
            
        this.mainItemMap.get(item.getType()).get(tier).add(item);
    }

    private void populateItemMap(String filePath){
        Vector<regStruct> items = loadItemsFromJson(filePath);
        if(items != null){
            for(regStruct item : items){
                registerItem(item);
            }
        }

    }

    private Vector<regStruct> loadItemsFromJson(String filePath){
        ObjectMapper mapper = new ObjectMapper();
        File itemsJson = new File(filePath);
        Vector<HashMap<String, Object>> rawData = null; 
        try {
            rawData = mapper.readValue(itemsJson, new TypeReference<Vector<HashMap<String, Object>>>() {});
            
        } catch (IOException e) {
            e.printStackTrace();
            return(null);
        }

        if(rawData == null){
            return(null);
        }
        Vector<regStruct> items = new Vector<regStruct>();

        for(HashMap<String, Object> itemData : rawData){
            String typeStr = (String) itemData.get("type");
            enums.itemTypeEnum type = enums.itemTypeEnum.valueOf(typeStr.toUpperCase());
            ItemTemplate itemTemplate = null;
            
            switch(type){
                case WEAPON:
                    itemTemplate = new WeaponTemplate(itemData);
                    break;
                default:
                    if(itemData.containsKey("armourSlot")){
                        itemTemplate = new items.templates.ArmourTemplate(itemData);
                    } else {
                        itemTemplate = new ItemTemplate(itemData);
                    }
                    break;
            }
            if(itemTemplate != null){
                regStruct struct = new regStruct();
                struct.template = itemTemplate;
                struct.tier =(int) itemData.get("tier");
                items.add(struct);
            }
        }

        return(items);
    }

    public ItemInstance applyRarity(Rarity rarity, ItemInstance item) {
        
        

        long currentValue = item.getValue();
        double multiplier = rarity.getValueModifier();
        item.setValue(Math.round(currentValue *multiplier));

        item.setNameModifier(rarity.getName());
 
    
        if (item instanceof ConditionInstance) {
            ConditionInstance conItem = (ConditionInstance) item;
            
            // Calculate  max condition
            ConditionTemplate conTemplate = (ConditionTemplate) conItem.getTemplate();
            int baseMax = conTemplate.getMaxCondition();
            int targetMax = (int) (baseMax * rarity.getConditionModifier());
            
        
            int bonusCondition = targetMax - baseMax;
            conItem.setMaxModifier(bonusCondition);
            
        
            conItem.setCondition(conItem.getMaxCondition());
        }

        
        if (item instanceof WeaponInstance) {
            WeaponInstance weapon = (WeaponInstance) item;
            double newModifier = weapon.getDamageModifier() * rarity.getMainModifier();
            weapon.setDamageModifier(newModifier);
        }

        return item;
    }
    

 
    
    



    private int getRandomTier(){
        int number = this.randomGenerator.nextInt(101);
        int tier = 1;
        if(number > 40 && number < 75){
            tier = 2;
        }else if(number > 75){
            tier =3;
        }


        return(tier);
    }
    public ItemTemplate getTemplate(String itemID){
        return this.idItemMap.get(itemID);
    }
    public ItemInstance getItem(int tier){
        Vector<enums.itemTypeEnum> types = new Vector<>(this.mainItemMap.keySet());
        enums.itemTypeEnum type = types.get(this.randomGenerator.nextInt(types.size()));

        return(getItem(type,tier));
    }

    public ItemInstance getItem(enums.itemTypeEnum itemType){
        Vector<enums.itemTypeEnum> types = new Vector<>(this.mainItemMap.keySet());
        enums.itemTypeEnum type = types.get(this.randomGenerator.nextInt(types.size()));

        return(getItem(type,getRandomTier()));
    }

    public ItemInstance getItem(){
        Vector<enums.itemTypeEnum> types = new Vector<>(this.mainItemMap.keySet());
        enums.itemTypeEnum type = types.get(this.randomGenerator.nextInt(types.size()));

        return(getItem(type,getRandomTier()));
    }
    public ItemInstance getItem(enums.itemTypeEnum itemType,int tier){
        ItemInstance item = this.retreiveInstanceFromMap(itemType,tier);
        if (item != null){
            Rarity itemRarity = this.getRarity();
            if(itemRarity != null){

                item = this.applyRarity(itemRarity,item);
                // To help with chest systems we need to assign a unique item ID to each different rarity version of an item
                int itemID = -1;
                if(this.rarityItemMap.containsKey(item.getTemplate().getName())){
                    itemID = this.rarityItemMap.get(item.getTemplate().getName());
                }else{
                    itemID = getNewItemID();
                    this.rarityItemMap.put(item.getTemplate().getName(),itemID);
                }
                item.setItemIDOverride(getNewItemID());
               
            }
            
            
        }
        return(item);

    }
    public void populateChest(Chest chest, int numberOfItems){
        for(int i = 0; i < numberOfItems; i++){
            ItemInstance item = getItem();
            if(item != null){
                chest.addRegularItem(item);
            }
        }
    }
    public void addItemToChest(Chest chest, enums.itemTypeEnum itemType, int tier, int quantity){
        ItemInstance item = this.retreiveInstanceFromMap(itemType,tier);
        if(item != null){
            Rarity itemRarity = this.getRarity();
            if(itemRarity != null){
                item = this.applyRarity(itemRarity,item);
            }
            chest.addRegularItem(item,quantity);
        }
    }

    public ItemInstance getItemByID(int itemID){
        if(this.idItemMap.containsKey(itemID)){
            ItemTemplate template = this.idItemMap.get(itemID);
            return(instanceFromTemplate(template));
        }
        return(null);
        
    }
    public ItemInstance instanceFromTemplate(ItemTemplate template){
        if(template instanceof items.templates.ArmourTemplate armourTemplate){
            return(new items.Instances.ArmourInstance(armourTemplate));
        } else if(template instanceof WeaponTemplate weaponTemplate){
            return(new WeaponInstance(weaponTemplate,weaponTemplate.getMaxCondition()));
        } else if(template instanceof ConditionTemplate conditionTemplate){
            return(new ConditionInstance(conditionTemplate,conditionTemplate.getMaxCondition()));
        } else {
            return(new ItemInstance(template));
        }
        
        
    }
    private ItemInstance retreiveInstanceFromMap(enums.itemTypeEnum itemType,int tier){
        ItemTemplate template = null;
        if(!this.mainItemMap.containsKey(itemType)){
          return null;  
        }
        HashMap<Integer,Vector<ItemTemplate>> tierMap = this.mainItemMap.get(itemType);
        if(!tierMap.containsKey(tier)) return null;

        Vector<ItemTemplate> itemVector = tierMap.get(tier);
        Random randomGenerator = new Random();

        int randIndex = randomGenerator.nextInt(itemVector.size());

        template = itemVector.get(randIndex);
        if(template != null){
            return(instanceFromTemplate(template));
        }

        return(null);
    }
}
