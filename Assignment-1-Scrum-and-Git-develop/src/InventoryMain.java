import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * The Main Inventory System
 * @author Darylle, Don, Jashan, Sahir
 *
 */

public class InventoryMain 
{
	private final int DEFAULTORDER = 50;
	private final int LOWESTSTOCK = 40;
	private final int INVALID = -1;
	private final int VALID = 0;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
	private ArrayList<Item> orderList = new ArrayList<Item>();
	private static Scanner kb = new Scanner(System.in);


	/**
	 * This method returns the index number of the item object.
	 * @param itemName
	 * @return index
	 */
	public static void main(String[] args)throws FileNotFoundException
	{
		InventoryMain test = new InventoryMain();
		test.run();
	}

	private void run()throws FileNotFoundException
	{
		readInput();
		readInput();
		checkIfLowStock();
		menu();
	}

	private void menu()throws FileNotFoundException
	{
		boolean end = false;

		while(end == false)
		{
			System.out.println("\n\nPlease choose an option");
			System.out.println("A: Search for an Item");
			System.out.println("B: Add Item Stock");
			System.out.println("C: Delete an Item");
			System.out.println("D: Print Item List");
			System.out.println("E: Order Line");
			System.out.println("F: Update Item List");
			System.out.println("G: Exit");
			String test = kb.next();
			char choice = test.toLowerCase().charAt(0);
			switch(choice) 
			{
			case 'a':
				System.out.println("Please Enter Item ID: ");
				int id = kb.nextInt();
				int index = searchItems(id);
				if (index != INVALID)
				{
					System.out.print(items.get(index).toString());
				}
				else
				{
					System.out.print("This Item Does Not Exist");
				}
				break;
			case 'b':
				addItemStock();
				break;
			case 'c':
				deleteItems();
				break;	
			case 'd':
				printItem();
				break;	
			case 'e':
				loadOrderList(false);
				break;	
			case 'f':
				updateItemsFile(items);
				break;
			case 'g':
				System.out.print("Thank you for Using this System");
				end = true;
				break;	
			}
		}

	}

	/**
	 *Check if any items have less than 40 in stock
	 **/
	private void checkIfLowStock() throws FileNotFoundException
	{
		for (int i = 0; i < items.size(); i++)
		{
			if (items.get(i).getStock() < LOWESTSTOCK)
			{
				loadOrderList(true);
				break;
			}
		}
	}

	/**
	 *Loads all the items the user wants to order. Also, automatically orders for items less than 40 stocks
	 **/
	private void loadOrderList(boolean automatic) throws FileNotFoundException
	{
		if (automatic == true)
		{
			for (int i = 0; i < items.size(); i++)
			{
				if (items.get(i).getStock() < LOWESTSTOCK)
				{
					orderList.add(items.get(i));
				}
			}

			orderLine(orderList);
		}
		else
		{
			int itemID;
			int validinput = INVALID;
			int check = 1;
			char choice;
			Item newItem;
			String text;

			do
			{
				System.out.print("To Order an Item, Please Enter its Item ID: \t ");
				itemID = kb.nextInt();
				validinput = INVALID;
				while (validinput == INVALID)
				{
					for (int i = 0; i < items.size(); i++)
					{
						if (itemID == items.get(i).getItemId())
						{
							orderList.add(items.get(i));
							check = VALID;
							break;
						}
						else
						{
							check = INVALID;
						}
					}
					if (check == VALID)
					{
						validinput = VALID;
					}
					else
					{
						System.out.print("Invalid Item ID, Please Try Again: ");
						itemID = kb.nextInt();
						validinput = INVALID;
					}
				}


				System.out.print("Do You Want to Order More Items? \n Enter:\n 'Y' for Yes \n 'N' for No");
				text = kb.next();
				choice = text.toUpperCase().charAt(0);
			}
			while (choice == 'Y');

			orderLine(orderList);
		}
	}

	/**
	 *Searches for an item and returns its index
	 **/
	private int searchItems(int itemID)
	{
		for (int i = 0; i < items.size(); i++)
		{
			if (itemID == items.get(i).getItemId())
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * This method returns the index number of the supplier object.
	 * @param supplierName
	 * @return index
	 */
	private int searchSuppliers(String supplierName)
	{
		for (int i = 0; i < suppliers.size(); i++)
		{
			if (supplierName == items.get(i).getToolName())
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * This method asks user for fileInput and throws FileNotFoundException.
	 * 
	 */
	void readInput() throws FileNotFoundException
	{
		File inFile;
		String fileName;
		Scanner test;

		System.out.print("Enter file name > ");
		fileName = kb.next();

		inFile = new File(fileName);
		test = new Scanner(inFile);

		if(fileName.equalsIgnoreCase("suppliers.txt"))
		{
			supInput(test);
		}
		else if(fileName.equalsIgnoreCase("items.txt"))
		{
			itemInput(test);
		}

	}

	/**
	 * Some parts borrowed from https://www.javatpoint.com/post/java-scanner-usedelimiter-method
	 * 
	 * This method takes item file inputs, creates Item objects and adds to items array list.
	 * 
	 * @param newIn
	 */
	void itemInput(Scanner newIn)
	{	
		int iD;
		String toolName;
		int stockNum;
		double priceAmt;
		String supID;

		newIn.useDelimiter(";");
		while(newIn.hasNext())
		{
			iD = newIn.nextInt();
			toolName = newIn.next();
			stockNum = newIn.nextInt();
			priceAmt = newIn.nextDouble();
			supID = newIn.nextLine();
			supID = supID.substring(1);

			Integer supplierIdInt = Integer.valueOf(supID);
			Item newItem = new Item(iD, toolName, stockNum, priceAmt, supplierIdInt);
			loadItems(newItem);

		}
	}

	/**
	 * Some parts borrowed from https://www.javatpoint.com/post/java-scanner-usedelimiter-method
	 * 
	 * This method takes suppliers file, creates Supplier objects and adds to suppliers array list.
	 * 
	 * @param newIn
	 */
	void supInput(Scanner newIn)
	{			
		int iD;
		String companyName;
		String addRess;
		String salesContact;

		newIn.useDelimiter(";");
		while(newIn.hasNext())
		{
			iD = newIn.nextInt();
			companyName = newIn.next();
			addRess = newIn.next();
			salesContact = newIn.nextLine();
			salesContact = salesContact.substring(1);

			Supplier newSup = new Supplier(iD, companyName, addRess, salesContact);
			addSupplier(newSup);
		}
	}

	/**
	 * This method load items to the arraylist if it exist.
	 * If not it creates a new item.
	 * @param newItem
	 */
	private void loadItems (Item newItem)
	{
		items.add(newItem);
	}

	/**
	 * This method deletes current item objects in the arraylist
	 * 
	 * @param currentItem
	 */
	private void deleteItems ()
	{
		int itemId = INVALID;
		int itemAmt;
		int index = INVALID;
		boolean valid = false;



		while(!valid)
		{
			System.out.print("Enter the ID of the Item You Would Like to Delete: ");
			itemId = kb.nextInt();
			index = searchItems(itemId);

			if(index != INVALID)
			{
				while (!valid)
				{
					System.out.print("Enter the Amount to be Removed: ");
					itemAmt = kb.nextInt();
					if (itemAmt <= items.get(index).getStock())
					{
						items.get(index).setStock(items.get(index).getStock() - itemAmt);
						valid = true;
					}
					else
					{
						System.out.println("Invalid Amount. Try again");
					}

				}
			}
			else
			{
				System.out.println("Invalid Item ID. Try again");
			}
		}
		System.out.println("Stock of " + items.get(index).getToolName() + " has been updated");

	}

	/**
	 * This method adds a supplier to the existing supplier object in the arraylist.
	 * @param newSupplier
	 */

	private void addSupplier (Supplier newSupplier)
	{
		suppliers.add(newSupplier);
	}

	/**
	 *This method prints the order to a file
	 **/
	private void orderLine(ArrayList<Item> orderItem)throws FileNotFoundException
	{
		String idAndDate;
		String order;
		int stockAmount;

		idAndDate = String.format("*************************************************\nORDER ID.:\t\t%d \nDate Ordered: \t%s",
				idRandomiser(), getDate());
		try
		{
			FileWriter writer = new FileWriter("orders.txt",true);
			BufferedWriter outfile = new BufferedWriter(writer);
			outfile.write(idAndDate);

			for (int i = 0; i < orderItem.size(); i++)
			{
				if (orderItem.get(i).getStock() < LOWESTSTOCK)
				{
					order = printOrder(DEFAULTORDER - orderItem.get(i).getStock(), orderItem.get(i));
				}
				else
				{
					stockAmount = getStockAmount(orderItem.get(i));
					order = printOrder(stockAmount, orderItem.get(i));			
				}

				outfile.write(order);

			}

			outfile.close();
			orderItem.clear();
		}
		catch (Exception e){
			System.err.println("Error while writing to file: " +
					e.getMessage());
		}
	}

	/**
	 *This method gets the user inputed stock
	 **/
	private int getStockAmount(Item orderItem)
	{
		String input;
		int stockAmount = 0;


		System.out.print("Enter the Number of " + orderItem.getToolName() + " Stocks to Order: ");	
		stockAmount = kb.nextInt();

		if(stockAmount <= 0)
		{
			do 
			{
				System.out.print("Invalid Amount, Try Again \n");
				stockAmount = kb.nextInt();
			}
			while (stockAmount <= 0);
		}

		return stockAmount;
	}

	/**
	 *This method gets the current date
	 **/
	private String getDate()
	{
		String todayDate;
		LocalDate date = LocalDate.now();
		DateTimeFormatter formattedDate = DateTimeFormatter.ofPattern("MMMM d, yyyy");
		return todayDate = date.format(formattedDate);
	}

	/**
	 *This method gets a random 5 digit number for the orderID
	 **/
	private int idRandomiser()
	{
		int orderId;
		do 
		{
			SecureRandom ranNum = new SecureRandom();
			orderId = ranNum.nextInt(100000);
		}
		while(orderId < 9999);

		return orderId;
	}

	/**
	 *This method complies all the main information of the order
	 **/
	private String printOrder (int stockAmount, Item orderItem)
	{

		String order;
		int supplierId;
		String supplierName = "";



		for(int i = 0; i < suppliers.size(); i++)
		{
			if (orderItem.getSupplierId() == suppliers.get(i).getId())
			{
				supplierName = suppliers.get(i).getName();
			}
		}

		order = String.format("\n\nItem description: \t%s"
				+ "\nAmount ordered: \t%d \nSupplier: \t\t%s\n", orderItem.getToolName(), stockAmount,
				supplierName);
		return order;	
	}

	/**
	 * Prints all the item in the list
	 */
	private void printItem()
	{
		for (int i = 0; i < items.size(); i++)
		{
			System.out.print(items.get(i).toString());
		}
	}

	/**
	 *This method updates the items.txt file with the latest contents
	 **/
	private void updateItemsFile (ArrayList<Item> items) throws FileNotFoundException
	{

		String itemline;
		int iD;
		String toolName;
		int stockNum;
		double priceAmt;
		int supID;
		String itemLine;

		try
		{
			PrintWriter writer = new PrintWriter("items.txt");

			for (int i = 0; i < items.size(); i++)
			{
				iD = items.get(i).getItemId();
				toolName = items.get(i).getToolName();
				stockNum = items.get(i).getStock();
				priceAmt = items.get(i).getPrice();
				supID = items.get(i).getSupplierId();

				itemLine = iD + ";" + toolName + ";" + stockNum + ";" + priceAmt + ";" + supID;

				writer.println(itemLine);
			}
			System.out.println("\nThe items are now updated!");
			writer.close();	
		}
		catch (Exception e){
			System.err.println("Error while writing to file: " +
					e.getMessage());
		}

	}

	private void addItemStock()
	{
		int itemID;
		int itemAmt;
		int i;
		int id;
		int stock;
		double price;
		int supId;
		String nY;

		System.out.print("Enter the ID of the Item You Would Like to Add > ");
		itemID = kb.nextInt();

		System.out.print("Enter the Amount to be Added > ");
		itemAmt = kb.nextInt();

		i = searchItems(itemID);

		if(i == INVALID)
		{
			System.out.println("Item: " + items.get(i).getToolName() + " does not exist");

			System.out.println("To Add a new item, the Item ID, Item Name, Stock, Price, and Supplier ID is Needed");
			System.out.println("Do you wish to add info along with the current name of " + items.get(i).getToolName() + " ?" + "\n input: Y for yes. N for no");
			nY = kb.next();

			nY.equalsIgnoreCase(nY);

			if (nY == "y")
			{

				System.out.println("Please enter the Name of the Item > ");
				String itemName = kb.next();

				System.out.println("Please enter the Stock of this item > ");
				stock = kb.nextInt();

				System.out.println("Please enter the Price of this item > ");
				price = kb.nextDouble();

				System.out.println("Please enter the Supplier ID of this item > ");
				supId = kb.nextInt();

				Item newItem = new Item(itemID, itemName, stock, price, supId);
				loadItems(newItem);

				System.out.println("Item " + itemName + " has been added.");
			}

			else
			{
				System.out.println("Returning to menu...");
			}

		}

		else
		{
			items.get(i).setStock(items.get(i).getStock() + itemAmt);
			System.out.println("Stock of item: " + items.get(i).getToolName() + " has been updated");

		}

	}

}
