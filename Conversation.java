package fbMessageReaderGUI;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
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
	
	public void generateMsgAvgLengths(){
		HashMap<String, MessageCounts> map = new HashMap<String, MessageCounts>();
		
		for(int i = 0; i < threads.size(); i++){
			for(int j = 0; j < threads.get(i).getMessageCount(); j++){
				Message msg = threads.get(i).getMessage(j);
				if(map.get(msg.getSender()) != null){
					MessageCounts existingCount = map.get(msg.getSender());
					existingCount.msgCount = existingCount.msgCount + 1;
					existingCount.wordTotal = existingCount.wordTotal + msg.getText().split(" ").length;
					map.put(msg.getSender(), existingCount);
				} else{
					MessageCounts newCount = new MessageCounts(msg.getSender());
					newCount.msgCount = 1;
					newCount.wordTotal = msg.getText().split(" ").length;
					map.put(msg.getSender(), newCount);
				}
			}
		}
		
		Set<String> keys = map.keySet();
		System.out.println(keys);
		
		
		
		
		String fName = createFolderName();
		AllMessages.createStatsDirs("/" + fName);
		String fileName = "fb-messages/stats/" + fName + "/" + fName + "_avg msg length.csv";
		
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(new File(fileName));
			writer.println("user, avg msg length, total messages, total word count");
			for (String s : keys) {
				writer.print(s + ",");
				writer.println(map.get(s).getAvg() + "," + map.get(s).msgCount + "," + map.get(s).wordTotal);
				
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		writer.close();
		
	}
	
	public void generateHotspots(){
		if (threads.isEmpty()){
			return;
		}

		
		HashMap<String, MessageTimes> map = new HashMap<String, MessageTimes>();
		
		for(int i = 0; i < threads.size(); i++){
			for(int j = 0; j < threads.get(i).getMessageCount(); j++){
				Message msg = threads.get(i).getMessage(j);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(msg.getStrTZ()));
				cal.setTime(msg.getDate());
				
				if(map.get(msg.getSender()) != null){
					MessageTimes existingTime = map.get(msg.getSender());
					existingTime.weekHrMsgs[cal.get(Calendar.HOUR_OF_DAY) + 24 * (cal.get(Calendar.DAY_OF_WEEK) - 1)]++;
					map.put(msg.getSender(), existingTime);
				} else{
					MessageTimes newTime = new MessageTimes(msg.getSender());
					newTime.weekHrMsgs[cal.get(Calendar.HOUR_OF_DAY) + 24 * (cal.get(Calendar.DAY_OF_WEEK) - 1)]++;
					map.put(msg.getSender(), newTime);
				}
			}
		}
		
		
		String fName = createFolderName();
		AllMessages.createStatsDirs("/" + fName);
		String fileName = "fb-messages/stats/" + fName + "/" + fName + "_weekly per hr.csv";
		
		Set<String> keys = map.keySet();
		PrintWriter writer = null;
		String daysOfWeek[] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
		try{
			writer = new PrintWriter(new File(fileName));
			writer.print("user,");

			for(int currentDay = 0; currentDay < 7; currentDay++){
				for(int hour = 0; hour < 24; hour++){
					writer.print(hour + " " + daysOfWeek[currentDay] + ",");
				}
			}
			for (String s : keys) {
				writer.println();
				writer.print(s + ",");			
				long[] msgTimes = map.get(s).weekHrMsgs;
				for(int i = 0; i < msgTimes.length; i++){
					writer.print(msgTimes[i] + ",");
				}
				
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		fileName = "fb-messages/stats/" + fName + "/" + fName + "_daily per hr.csv";
		try{
			writer = new PrintWriter(new File(fileName));
			writer.print("user,");
			for(int i = 0; i < 24; i++){
				writer.print(i + ",");
			}

			for (String s : keys) {
				writer.println();
				writer.print(s + ",");			
				long[] msgTimes = map.get(s).getDayHrMsgs();
				for(int i = 0; i < msgTimes.length; i++){
					writer.print(msgTimes[i] + ",");
				}
				
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		writer.close();
		
		
	}
	
	public String createFolderName(){
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

class MessageCounts{
	public String User;
	public long msgCount = 0;
	public long wordTotal = 0;

	
	public MessageCounts(String user){
		this.User = user;
	}
	
	public double getAvg(){
		double avg = (double)wordTotal/(double)msgCount;
		return avg;
	}
}

class MessageTimes{
	public String User;
	public long[] weekHrMsgs = new long[168];
	
	public MessageTimes(String user){
		this.User = user;
	}
	
	public long[] getDayHrMsgs(){
		long[] dayHrMsgs = new long[24];
		for(int i = 0; i < weekHrMsgs.length; i++){
			dayHrMsgs[i%24] = dayHrMsgs[i%24] + weekHrMsgs[i];
		}
		
		return dayHrMsgs;
	}
}
