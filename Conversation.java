package fbMessageReaderGUI;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class Conversation {
	private ArrayList<String> participants = new ArrayList<String>();
	private ArrayList<MessageThread> threads = new ArrayList<MessageThread>();
	private static String user;
	
	
	
	public Conversation(ArrayList<String> partsIn){
		for (int i = 0; i < partsIn.size(); i++){
			this.participants.add(partsIn.get(i));
		}
		user = AllMessages.getUser();
	}
	
	
	public ArrayList<String> getParticipants(){
		return this.participants;
	}
	
	public void addMessageThread(MessageThread ThreadIn){
		threads.add(ThreadIn);
	}
	
	public ArrayList<MessageThread> getThreads(){
		return this.threads;
	}
	
	public void generateDailyHotspots(){
		
		if (threads.isEmpty()){
			return;
		}
		long userMsgsHr[] = new long[24];
		long otherMsgsHr[] = new long[24];
		String fName = createFileName();
		AllMessages.createStatsDirs("/" + fName);
		String fileName = "fb-messages/stats/" + fName + "/Hourly Daily Hotspots.csv";
		
		for(int i = 0; i < threads.size(); i++){
			for(int k = 0; k < threads.get(i).getMessageCount(); k++){
				Message msg = threads.get(i).getMessage(k);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(msg.getStrTZ()));
				
				cal.setTime(msg.getDate());
				if(msg.getSender().contains(user)){
					userMsgsHr[cal.get(Calendar.HOUR_OF_DAY)]++;
				} else {
					otherMsgsHr[cal.get(Calendar.HOUR_OF_DAY)]++;
				}
			}
		}
		
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(new File(fileName));
			writer.print(user + "'s messages per hour over 24hr");
			for(int i = 0; i < 24; i++){
				writer.print(","+userMsgsHr[i]);
			}
			writer.println();
			writer.print("Other's messages per hour over 24hr");
			for(int i = 0; i < 24; i++){
				writer.print(","+otherMsgsHr[i]);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		writer.close();
		
		
	}
	
	public String createFileName(){
		String partsString = "";
		for(int i = 0; i < participants.size(); i++){
			participants.set(i,  participants.get(i).trim());
		}
		
		if (participants.size() > 2){
			for (int i = 0; i < participants.size(); i++){
				int lastNameIndex = participants.get(i).indexOf(" ") + 1;
				partsString = partsString + this.participants.get(i).charAt(0) + this.participants.get(i).charAt(lastNameIndex) + " ";
			}
			partsString = partsString.trim();
			partsString = "Group " + partsString;
		} else {
			for (int i = 0; i < participants.size(); i++){
				partsString = partsString + " " + this.participants.get(i);
			}
			partsString = partsString.substring(1);
		}
		
		return partsString;
	}
}
