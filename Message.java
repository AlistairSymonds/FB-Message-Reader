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
	
	public String getStrDate(){
		return this.getStrDate();
	}
	
	public String getStrTZ(){
		SimpleDateFormat tz = new SimpleDateFormat("z");
		return tz.format(msgDate);
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
		String msgTextSplit[] = this.messageText.split(" ");
		System.out.println("############################");
		System.out.println("Sender: " + Sender);
		System.out.println("Sent at: " + msgDate);
		System.out.println();
		System.out.println();
		for (int j = 0; j < msgTextSplit.length; j++){
				System.out.print(msgTextSplit[j] + " ");
		}
		
		System.out.println();
		System.out.println("=============================");
		System.out.println();
	}
	
	public boolean isChatStarter(Message other){
		long deltaTime = this.getDate().getTime() - other.getDate().getTime();
		deltaTime = Math.abs(deltaTime);
		if (deltaTime > 54000000){
			System.out.println("Its a starter");
			return true;
		} else {
			return false;
		}
	}

}




