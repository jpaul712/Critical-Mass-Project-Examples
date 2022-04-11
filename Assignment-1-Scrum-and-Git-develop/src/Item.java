/**
 * The item class
 * @author Darylle, Don, Jashan, Sahir
 *
 */
public class Item
{

	private int itemId;
	private String toolName;
	private int stock;
	private double price;
	private int supplierId;


	Item(int itemId, String toolName, int stock, double price, int supplierId)
	{
		this.itemId = itemId;
		this.toolName = toolName;
		this.stock = stock;
		this.price = price;
		this.supplierId = supplierId;
	}

	public int getStock()
	{
		return stock;
	}

	public String getToolName()
	{
		return toolName;
	}
	
	public int getSupplierId()
	{
		return supplierId;
	}

	public void setStock(int stock)
	{
		this.stock = stock;
	}
	
	public double getPrice() 
	{
		return price;
	}
	
	public int getItemId()
	{
		return itemId;
	}
	

	public String toString()
	{
		String text;
		text = "\n\nID: " + this.itemId + "\nToolName: " + this.toolName + "\nStock: " + this.stock + "\nPrice: " + this.price + "\nSupplierID: "
		+ this.supplierId;
		return text;
	}
}
