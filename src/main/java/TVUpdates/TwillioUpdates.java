package TVUpdates;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwillioUpdates {
	
	  public static final String ACCOUNT_SID = "*******************";
	  public static final String AUTH_TOKEN = "*******************";

	  public void sendSMS(String Content) {
		  if(Content!=null&&Content.length()!=0) {
			  System.out.println("Todays Content:"+ Content.length());
	    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	    Message message = Message.creator(new PhoneNumber("+1585*****"),
	        new PhoneNumber("+12******"), Content).create();
		  }
		  else
			  System.out.println("Nothing new today!!!");
	}
}