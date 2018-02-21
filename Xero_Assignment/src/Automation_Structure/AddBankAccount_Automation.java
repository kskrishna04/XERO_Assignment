package Automation_Structure;

import java.io.IOException;

import jxl.read.biff.BiffException;

public class AddBankAccount_Automation 
{
	public static void main(String[] args) throws BiffException, IOException, InterruptedException 
	{
		Common_Functionality.Settings();
		Common_Functionality.AutomationReuse("Login");
		String continueFlag = "Y";
		while(continueFlag.equalsIgnoreCase("Y"))
		{
			Common_Functionality.ReadBankAccountDetails();
			try
			{
				switch(Common_Functionality.map.get("accountType"))
				{
				case "Other":
					Common_Functionality.AutomationReuse("AddBankAccount");
					break;
				case "Credit Card":
					Common_Functionality.AutomationReuse("AddCreditCard");
					break;
				default:
					System.out.println("You have not entered a valid account type.");
					break;
				}
				System.out.println("Would you like to continue adding new account?");
				continueFlag = Common_Functionality.inputReader.readLine();
			}
			catch(Exception e)
			{
				System.out.println("Exception Occrured: \t\t"+e.getMessage()+"\n\nWould you like to try again? Y or N");
				continueFlag = Common_Functionality.inputReader.readLine();
			}
		}
		
	}


}
