package items.Instances;

import items.templates.ItemTemplate;


public class ItemInstance {
    protected final ItemTemplate template;
    private String nameModifier = "";
    private long value;
    private boolean showOriginalName = true;
    private int idOverride = -1;
    public ItemInstance(ItemTemplate template) {
        this.template = template;
        this.value = template.getValue();
    } 

    public String getNameModifier() {
        return nameModifier;
    }

    public void setNameModifier(String nameModifier) {
        this.nameModifier = nameModifier;
    }

    public boolean isShowOriginalName() {
        return showOriginalName;
    }
    public void setShowOriginalName(boolean showOriginalName) {
        this.showOriginalName = showOriginalName;
    }
    public String getDisplayName() {
        if (showOriginalName) {
            if (nameModifier.isEmpty()) {
                return template.getName();
            }
            return nameModifier + " "+ template.getName();
        } else {
            return nameModifier;
        }
    }

    public String getValueAsString(){
        return(String.format("%.2f", this.value));
    }

    public String getItemID(){
        
        return(template.getItemID());
        
    }

    public void setItemIDOverride(int itemID){
        this.idOverride = itemID;
    }
    public ItemTemplate getTemplate() {
        return template;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public ItemInstance copy() {
       
        ItemInstance newInstance = new ItemInstance(this.template);
        
        
        newInstance.setValue(this.value);
        newInstance.setNameModifier(this.nameModifier);
        
        return newInstance;
    }

   
}