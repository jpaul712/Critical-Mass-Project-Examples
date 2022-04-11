/**
 * The Supplier class
 * @author Darylle, Don, Jashan, Sahir
 *
 */
public class Supplier
{
	private int id;
	private String companyName;
	private String address;
	private String salesContact;

	Supplier(int id, String companyName, String address, String salesContact)
	{
		this.id = id;
		this.companyName = companyName;
		this.address = address;
		this.salesContact = salesContact;
	}

	public int getId()
	{
		return id;
	}
	
	public String getName()
	{
		return companyName;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public String getSalesContact() 
	{
		return salesContact;
	}
}
