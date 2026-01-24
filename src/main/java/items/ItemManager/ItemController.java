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
import java.math.BigDecimal;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.atomic.AtomicInteger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.NavigableMap;
public class ItemController {
    private HashMap<enums.itemTypeEnum,Vector<ItemTemplate>> mainItemMap;
    private HashMap<String,Integer> rarityItemMap;
    private AtomicInteger itemIDCounter = new AtomicInteger(0);
    private static final Random randomGenerator = new Random();
    private final NavigableMap<Integer, Rarity> dropTable = new TreeMap<>();
    // Grey will just be the base item
    private Rarity green;
    private Rarity blue;
    private Rarity purple;
    private Rarity gold;
    private int totalWeight = 0;
    public ItemController(){
        
        this.mainItemMap = new HashMap<enums.itemTypeEnum,Vector<ItemTemplate>>();
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
    private void registerItem(ItemTemplate item){
        if(!this.mainItemMap.containsKey(item.getType())){
            this.mainItemMap.put(item.getType(), new Vector<ItemTemplate>());
        }
        this.mainItemMap.get(item.getType()).add(item);
    }

    private void populateItemMap(String filePath){
        Vector<ItemTemplate> items = loadItemsFromJson(filePath);
        if(items != null){
            for(ItemTemplate item : items){
                registerItem(item);
            }
        }

    }

    private Vector<ItemTemplate> loadItemsFromJson(String filePath){
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
        Vector<ItemTemplate> items = new Vector<ItemTemplate>();

        for(HashMap<String, Object> itemData : rawData){
            String typeStr = (String) itemData.get("type");
            enums.itemTypeEnum type = enums.itemTypeEnum.valueOf(typeStr.toUpperCase());
            ItemTemplate itemTemplate = null;
            
            switch(type){
                case WEAPON:
                    itemTemplate = new WeaponTemplate(itemData,getNewItemID());
                    break;
                default:
                    itemTemplate = new ItemTemplate(itemData,getNewItemID());
                    break;
            }
            if(itemTemplate != null){
                items.add(itemTemplate);
            }
        }

        return(items);
    }

    public ItemInstance applyRarity(Rarity rarity, ItemInstance item) {
        
        

        BigDecimal currentValue = item.getValue();
        BigDecimal multiplier = new BigDecimal(rarity.getValueModifier());
        item.setValue(currentValue.multiply(multiplier));

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
    

 
    
    public int getNewItemID(){
        return(this.itemIDCounter.getAndIncrement());
    }


    public ItemInstance getItem(){
        Vector<enums.itemTypeEnum> types = new Vector<>(this.mainItemMap.keySet());
        enums.itemTypeEnum type = types.get(this.randomGenerator.nextInt(types.size()));

        return(getItem(type));
    }
    public ItemInstance getItem(enums.itemTypeEnum itemType){
        
        ItemInstance item = this.retreiveInstanceFromMap(itemType);
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
    public ItemInstance instanceFromTemplate(ItemTemplate template){
       
        if(template instanceof WeaponTemplate weaponTemplate){
            return(new WeaponInstance(weaponTemplate,weaponTemplate.getMaxCondition()));
        } else if(template instanceof ConditionTemplate conditionTemplate){
            return(new ConditionInstance(conditionTemplate,conditionTemplate.getMaxCondition()));
        } else {
            return(new ItemInstance(template));
        }
        
        
    }
    private ItemInstance retreiveInstanceFromMap(enums.itemTypeEnum itemType){
        ItemTemplate template = null;
        Vector<ItemTemplate> itemVector = this.mainItemMap.get(itemType);
        Random randomGenerator = new Random();

        int randIndex = randomGenerator.nextInt(itemVector.size());

        template = itemVector.get(randIndex);
        if(template != null){
            return(instanceFromTemplate(template));
        }

        return(null);
    }
}
