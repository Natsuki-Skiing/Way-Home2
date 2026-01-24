package items.ChestClasses;
import items.ItemManager.*;
import java.util.Random;
public class ChestMaker {
    ItemController itemManager;
    Random randomGen = new Random();
    public ChestMaker(ItemController itemManager){
        this.itemManager = itemManager;
    }

    public Chest makeAndPopulateChest(int maxNumberOfItems){
        Chest chest = new Chest();
        populateChest(chest,maxNumberOfItems);
        return(chest);
    }


    public void populateChest(Chest chest,int maxNumberOfItems){
        int numberOfItems = this.randomGen.nextInt(maxNumberOfItems);
        for(int itemNo = 0; itemNo <numberOfItems; itemNo++){
            chest.addRegularItem(this.itemManager.getItem());
        }
    }
}
