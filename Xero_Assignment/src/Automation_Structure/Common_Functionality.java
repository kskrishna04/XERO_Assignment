package Automation_Structure;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Common_Functionality 
{
	private static WebDriver driver;
	private static Logger Log = Logger.getLogger(AddBankAccount_Automation.class.getName());
	static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	static Map<String, String> map = new HashMap<String, String>();
	private static String value;
	private static Workbook wb;
	
	public static void ReadBankAccountDetails() throws BiffException, InterruptedException, IOException
	{
		map.clear();
		System.out.println("Enter Name of your bank\n");
		String value = inputReader.readLine();
		map.put("accountName", value);
		System.out.println("Enter type of the account\n");
		value = inputReader.readLine();
		map.put("accountType", value);
		System.out.println("Enter account/credit card number\n");
		value = inputReader.readLine();
		map.put("number", value);
	}

	public static void Settings() throws BiffException, IOException
	{
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver\\chromedriver.exe"); 
		driver =new ChromeDriver();
		
		//Reading Input file which contains details of the web elements and values to be entered, along with identifiers
		String FilePath = System.getProperty("user.dir") + "\\Input.xls";
		FileInputStream fs = new FileInputStream(FilePath);
		Log.info("File Input is read");		
		wb = Workbook.getWorkbook(fs);
	}
	
	public static void AutomationReuse(String logicName) throws IOException
	{
		//Retrieving the workbooks and reading the sheet having the same name as logicName		       
		Sheet sh = wb.getSheet(logicName); // We can also provide sheet name here and iterate using multiple sheets
		
		int colft=5;	//holds column number for field type in file
		int colval=7;	//holds column number for values in file
		
		int totalNoOfRows = sh.getRows();
		int totalNoOfCols = sh.getColumns();
		
		//A multi dimensional array that we use to iterate and achieve Key-word driven
		String[][] arr = new String[totalNoOfCols][totalNoOfRows];
		Log.info("Total No of Rows and columns are read");
		for (int row = 0; row <totalNoOfRows; row++)
		{ 
			for (int col = 0; col <totalNoOfCols; col++)
			{				
				 arr[col][row] = sh.getCell(col, row).getContents();
			}
		}
		
		Log.info("All the excel sheet values are taken into an array");
		
		//Main implementation where iteration through the values fetched happens and actions as well.
		try
		{
			for(int row = 1; row < totalNoOfRows; row++)
			{
				String FieldType = arr[colft][row];		//String that retrieves FieldType from file	
				
				if(FieldType.equals("url"))		
				{										
					Thread.sleep(9000L);
					driver.get((arr[colval][row]) + " ");
					System.out.println("opened url"+arr[colval][row]+"driver:"+driver);
					Log.info("Opened URL: " + arr[colval][row]);
				}
				
				else if(FieldType.equals("TextField") || FieldType.equals("TextArea"))		
	            {
					value = arr[colval][row];
					
					try
					{
						WebDriverWait myWaitVar=new WebDriverWait(driver,200);
						myWaitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(arr[colft-1][row])));
						if(arr[colft+1][row].equalsIgnoreCase("account name") && value.equalsIgnoreCase("NA"))
						{
							driver.findElement(By.xpath(arr[colft-1][row])).clear();
							driver.findElement(By.xpath(arr[colft-1][row])).sendKeys(map.get("accountName"));
							System.out.println("entered text"+arr[colft-1][row]);
							Log.info(map.get("accountName")+" value is entered in "+driver.findElement(By.xpath(arr[colft-1][row])).getTagName());
						}
						else if(arr[colft+1][row].equalsIgnoreCase("account number") && value.equalsIgnoreCase("NA"))
						{
							driver.findElement(By.xpath(arr[colft-1][row])).clear();
							driver.findElement(By.xpath(arr[colft-1][row])).sendKeys(map.get("number"));
							System.out.println("entered text"+arr[colft-1][row]);
							Log.info(map.get("number")+" value is entered in "+driver.findElement(By.xpath(arr[colft-1][row])).getTagName());
						}
						else
						{
							System.out.println(arr[colft][row]+" "+arr[colft-1][row]+" " + value+" " + arr[5][row]);  
							driver.findElement(By.xpath(arr[colft-1][row])).clear();
							driver.findElement(By.xpath(arr[colft-1][row])).sendKeys(value);
							System.out.println("entered text"+arr[colft-1][row]);
							Log.info(value+" value is entered in "+driver.findElement(By.xpath(arr[colft-1][row])).getTagName());
						}
					}
					catch(WebDriverException we)
					{
						System.out.println("Exception Occurred:"+we);
						we.printStackTrace();
						throw we;
					}
	            }
	                                       
	            else if(FieldType.equals("Button"))		
	            {
	            	try
	            	{
	            		WebDriverWait myWaitVar=new WebDriverWait(driver,200);
	            		myWaitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(arr[colft-1][row])));
	            		System.out.println(arr[colft][row]+" "+arr[colft-1][row]+" " + arr[colval][row]+" " + arr[5][row]); 
	            		driver.findElement(By.xpath(arr[colft-1][row])).click();
	            		Thread.sleep(5000L);
	            	}
	            	catch(WebDriverException we)
	            	{
	            		System.out.println("Exception Occurred:"+we);
						we.printStackTrace();
						throw we;
					}
	            }
				
	            else if(FieldType.equalsIgnoreCase("link"))
	            {
	            	try
	            	{
	            		WebDriverWait myWaitVar=new WebDriverWait(driver,500);
		          	  	myWaitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(arr[colft-1][row])));
		          	  	System.out.println(arr[colft][row]+" "+arr[colft-1][row]+" " + arr[colval][row]+" " + arr[5][row]); 
		          	  	driver.findElement(By.xpath(arr[colft-1][row])).click();
		          	  	System.out.println("link"+arr[colft-1][row]);
		          	  	Thread.sleep(2000);
	            	}
	            	catch(WebDriverException we)
	            	{
	            		System.out.println("Exception Occurred:"+we);
						we.printStackTrace();
						throw we;
					}
	            }
				
	            else if(FieldType.equalsIgnoreCase("link text"))
	            {
	            	try
	            	{
	            		WebDriverWait myWaitVar=new WebDriverWait(driver,500);
		          	  	myWaitVar.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(arr[colft-1][row])));
		          	  	System.out.println(arr[colft][row]+" "+arr[colft-1][row]+" " + arr[colval][row]+" " + arr[5][row]); 
		          	  	driver.findElement(By.linkText(arr[colft-1][row])).click();
		          	  	System.out.println("link"+arr[colft-1][row]);
		          	  	Thread.sleep(2000);
	            	}
	            	catch(WebDriverException we)
	            	{
	            		System.out.println("Exception Occurred:"+we);
						we.printStackTrace();
						throw we;
					}
	            }
				
	            else if(FieldType.equalsIgnoreCase("element"))
	            {
	            	try
	            	{
	            		WebDriverWait myWaitVar=new WebDriverWait(driver,500);
		          	  	myWaitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(arr[colft-1][row])));
		          	  	System.out.println(arr[colft][row]+" "+arr[colft-1][row]+" " + arr[colval][row]+" " + arr[5][row]); 
		          	  	driver.findElement(By.xpath(arr[colft-1][row])).click();
		          	  	System.out.println("link"+arr[colft-1][row]);
		          	  	Thread.sleep(2000);
	            	}
	            	catch(WebDriverException we)
	            	{
	            		System.out.println("Exception Occurred:"+we);
						we.printStackTrace();
						throw we;
					}
	            }
				
	            else if(FieldType.equalsIgnoreCase("ul-li"))
	            {
	            	value = arr[colval][row];
	            	try
	            	{
	            		WebDriverWait myWaitVar=new WebDriverWait(driver,500);
		          	  	myWaitVar.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(arr[colft-1][row])));
	            		WebElement accountTypeUL= driver.findElement(By.xpath(arr[colft-1][row]));
	            		List<WebElement> accountTypeList=accountTypeUL.findElements(By.tagName("li"));
	            		
	            		
	            		if(value.equalsIgnoreCase("NA"))
	            		{
	            			for (WebElement li : accountTypeList) 
		            		{
		            			if (li.getText().equals(map.get("accountType"))) 
		            			{
		            				System.out.println(li.getText());
		            				li.click();
		            			}
		            		}
	            		}
	            		else
	            		{
	            			for (WebElement li : accountTypeList) 
		            		{
		            			if (li.getText().equals(value)) 
		            			{
		            				li.click();
		            			}
		            		}
	            		}
	            	}
	            	catch(WebDriverException we)
	            	{
	            		System.out.println("Exception Occurred:"+we);
						we.printStackTrace();
						throw we;
					}
	            }
			}
		}
		catch(NoSuchElementException e)
		{
			Log.info("Exception occured:" + e);
			Log.info("Exception occured:" + e.getAdditionalInformation());
			Log.info("Exception occured:" + WebDriverException.DRIVER_INFO);
			Log.info("Exception occured:" + e.getStackTrace());
			System.out.println("Exception occurred:" + e);		
		}
		catch(Exception e)
		{
			Log.info("Exception occured:" + e);
			Log.info("Exception occured:" + WebDriverException.DRIVER_INFO);
			Log.info("Exception occured:" + e.getStackTrace());
			System.out.println("Exception occurred:" + e);	
		}
	}
}
