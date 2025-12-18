package items;

public class ChestItem {
    private final Item item;
    private  int quantity = 1;

    public ChestItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
    //**
    // @return newChestItem with new item. Null if amount is invalid */
    public ChestItem takeAmount(int amount){
        if (amount <= 0 || amount > this.quantity) {
            return null;
        }
        this.quantity -= amount;
        return new ChestItem(this.item.copy(), amount);
    }
}
