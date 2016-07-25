package fbMessageReaderGUI;
import java.text.*;
import java.util.*;
public class Message {

	private SimpleDateFormat ft = new SimpleDateFormat("dd MMMM yyyy 'at' kk:mm z");
	private String messageText;
	private String Sender;
	private String strDate;
	private Date msgDate;
	
	
	public Message(String Sender){
		this.Sender = Sender;
	}
	
	public String getSender(){
		return this.Sender;
	}
	
	public void setText(String text){
		int lastIndex = text.indexOf("</p>");
		text = text.substring(4, lastIndex);
		
		String apostrophe = "&#039;";
		String quoteMark = "&quot;";
		text = text.replaceAll(apostrophe, "'");
		text = text.replaceAll(quoteMark, "\"");
		
		this.messageText = text;
	}
	
	public void setDate(String dateIn){
		int trimFrom = dateIn.indexOf("</span>");
		dateIn = dateIn.substring(0, trimFrom);
		
		this.strDate = dateIn;
		
		try { 
	          msgDate = ft.parse(dateIn);  
	      } catch (ParseException e) { 
	          System.out.println("Unparseable using " + ft); 
	      }
	}
	public Date getDate(){
		return this.msgDate;
	}
	
	public String getText(){
		return this.messageText;
	}
	
	public void printMsgToConsole(){
		int width = 6;
		String msgTextSplit[] = this.messageText.split(" ");
		System.out.println("############################");
		System.out.println("Sender: " + Sender);
		System.out.println("Sent at: " + msgDate);
		System.out.println();
		System.out.println();
		for (int j = 0; j < msgTextSplit.length; j++){
			//for (int i = 0; i < width; i++){
				System.out.print(msgTextSplit[j] + " ");
			//}
		//System.out.println();
		}
		
		System.out.println();
		System.out.println("=============================");
		System.out.println();
	}

}




