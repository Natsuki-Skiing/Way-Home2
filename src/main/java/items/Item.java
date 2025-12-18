package items;
import java.math.BigDecimal;
import enums.itemTypeEnum;
public class Item {

    private String name;
    private String description;
    private BigDecimal value;
    private itemTypeEnum type;

    public Item(String name, String description, BigDecimal value, itemTypeEnum type) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.type = type;
    }

    public Item(Item other) {
        this.name = other.name;
        this.description = other.description;
        this.value = other.value;
        this.type = other.type;
    }

    public Item copy() {
        return new Item(this);
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