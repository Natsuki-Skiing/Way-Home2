package items.templates;
import java.math.BigDecimal;
import enums.itemTypeEnum;
import java.util.HashMap;
public class ItemTemplate {

    private String name;
    private String description;
    private BigDecimal value;
    private itemTypeEnum type;
    private itemTypeEnum useType;
    protected int itemID;
    public ItemTemplate(String name, String description, BigDecimal value, itemTypeEnum type,itemTypeEnum useType, int itemID) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.type = type;
        this.useType = useType;
        this.itemID = itemID;
    }

    public ItemTemplate(HashMap<String, Object> itemData, int itemID) {
        this.name = (String) itemData.get("name");
        this.description = (String) itemData.get("description");
        this.value = new BigDecimal((Double) itemData.get("value"));
        String typeStr = (String) itemData.get("type");
        this.type = enums.itemTypeEnum.valueOf(typeStr.toUpperCase());
        this.useType = enums.itemTypeEnum.valueOf(((String) itemData.get("useType")).toUpperCase());
        this.itemID = itemID;
    }

    public ItemTemplate(ItemTemplate other) {
        this.name = other.name;
        this.description = other.description;
        this.value = other.value;
        this.type = other.type;
    }
    public int getItemID(){
        return(this.itemID);
    }
    public ItemTemplate copy() {
        return new ItemTemplate(this);
    }
    public itemTypeEnum getUseType(){
        return(this.useType);
    }
    public void setItemUseType(itemTypeEnum useType){
        this.useType = useType;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public BigDecimal getValue() {
        return value;   
    }
    public void setItemID(int itemID){
        this.itemID = itemID;
    }
    public String getValueAsString(){
        return(String.format("%.2f", this.value));
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public itemTypeEnum getType() {
        return type;
    }
}