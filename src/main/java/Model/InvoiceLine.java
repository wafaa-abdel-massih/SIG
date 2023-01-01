package Model;

public class InvoiceLine{

    private int itemNo;
    private String itemName;
    private double itemPrice;
    private int count;
    private double itemTotal;

    public InvoiceLine(){};
    public InvoiceLine (String itemName,double itemPrice, int count){
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.itemTotal = 0;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }
    public int getItemNo() { return itemNo; }

    public void setItemNo(int itemNo) { this.itemNo = itemNo; }
}
