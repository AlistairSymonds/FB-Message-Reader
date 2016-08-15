package fbMessageReaderGUI;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

public class Conversation {
	private ArrayList<String> participants = new ArrayList<String>();
	private ArrayList<MessageThread> threads = new ArrayList<MessageThread>();
	private static String user;
	private HashMap<String, MessageTimes> hotspotMap = new HashMap<String, MessageTimes>();
	private HashMap<String, MessageCounts> msgLenMap = new HashMap<String, MessageCounts>();
	
	public Conversation(ArrayList<String> partsIn){
		for (int i = 0; i < partsIn.size(); i++){
			this.participants.add(partsIn.get(i));
		}
		user = AllMessages.getUser();
	}
	
	public HashMap<String, MessageTimes> getHotspotMap(){
		return this.hotspotMap;
	}
	
	public HashMap<String, MessageCounts> getMsgLenMap(){
		return this.msgLenMap;
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
	public void whoStartedIt(){
		HashMap<String, Long> convosStarted = new HashMap<String, Long>();
		for(int i = 0; i < threads.size(); i++){
			for(int j = 1; j < threads.get(i).getMessageCount(); j++){
				if(threads.get(i).getMessage(j).isChatStarter(threads.get(i).getMessage(j - 1))){
					if (convosStarted.get(threads.get(i).getMessage(j).getSender()) != null){
						long currentVal = convosStarted.get(threads.get(i).getMessage(j).getSender()).longValue();
						currentVal++;
						Long value = new Long(currentVal);
						convosStarted.put(threads.get(i).getMessage(j).getSender(), value);
					} else {
						Long value = new Long(1);
						convosStarted.put(threads.get(i).getMessage(j).getSender(), value);
					} 
				}
			}
		}
		System.out.println("Someone started it");
		
		Set<String> keys = convosStarted.keySet();
		System.out.println(keys);
		
		
		
		
		String fName = createFolderName();
		AllMessages.createStatsDirs("/" + fName);
		String fileName = "fb-messages/stats/" + fName + "/" + fName + "_convos started.csv";
		
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(new File(fileName));
			writer.println("user, convos started");
			for (String s : keys) {
				writer.println(s + "," + convosStarted.get(s));
				
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		writer.close();
		
	}
	
	
	public void generateMsgAvgLengths(){
		
		for(int i = 0; i < threads.size(); i++){
			for(int j = 0; j < threads.get(i).getMessageCount(); j++){
				Message msg = threads.get(i).getMessage(j);
				if(msgLenMap.get(msg.getSender()) != null){
					MessageCounts existingCount = msgLenMap.get(msg.getSender());
					existingCount.msgTotal = existingCount.msgTotal + 1;
					existingCount.wordTotal = existingCount.wordTotal + msg.getText().split(" ").length;
					for(int k = 0; k < msg.getText().length(); k++){
						if(!Character.isWhitespace(msg.getText().charAt(k))){
							existingCount.charTotal++;
						}
					}
					msgLenMap.put(msg.getSender(), existingCount);
				} else{
					MessageCounts newCount = new MessageCounts(msg.getSender());
					newCount.msgTotal = 1;
					newCount.wordTotal = msg.getText().split(" ").length;
					for(int k = 0; k < msg.getText().length(); k++){
						if(!Character.isWhitespace(msg.getText().charAt(k))){
							newCount.charTotal++;
						}
					}
					msgLenMap.put(msg.getSender(), newCount);
				}
			}
		}
		
		Set<String> keys = msgLenMap.keySet();
		System.out.println(keys);
		
		
		
		
		String fName = createFolderName();
		AllMessages.createStatsDirs("/" + fName);
		String fileName = "fb-messages/stats/" + fName + "/" + fName + "_avg msg length.csv";
		
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(new File(fileName));
			writer.println("user, avg msg length, total messages, total word count, total chars");
			for (String s : keys) {
				writer.print(s + ",");
				writer.println(msgLenMap.get(s).getAvg() + "," + msgLenMap.get(s).msgTotal + "," + msgLenMap.get(s).wordTotal + "," + msgLenMap.get(s).charTotal);
				
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
		for(int i = 0; i < threads.size(); i++){
			for(int j = 0; j < threads.get(i).getMessageCount(); j++){
				Message msg = threads.get(i).getMessage(j);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(msg.getStrTZ()));
				cal.setTime(msg.getDate());
				
				if(hotspotMap.get(msg.getSender()) != null){
					MessageTimes existingTime = hotspotMap.get(msg.getSender());
					existingTime.weekHrMsgs[cal.get(Calendar.HOUR_OF_DAY) + 24 * (cal.get(Calendar.DAY_OF_WEEK) - 1)]++;
					hotspotMap.put(msg.getSender(), existingTime);
				} else{
					MessageTimes newTime = new MessageTimes(msg.getSender());
					newTime.weekHrMsgs[cal.get(Calendar.HOUR_OF_DAY) + 24 * (cal.get(Calendar.DAY_OF_WEEK) - 1)]++;
					hotspotMap.put(msg.getSender(), newTime);
				}
			}
		}
		
		
		String fName = createFolderName();
		AllMessages.createStatsDirs("/" + fName);
		String fileName = "fb-messages/stats/" + fName + "/" + fName + "_weekly per hr.csv";
		
		Set<String> keys = hotspotMap.keySet();
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
				long[] msgTimes = hotspotMap.get(s).weekHrMsgs;
				for(int i = 0; i < msgTimes.length; i++){
					writer.print(msgTimes[i] + ",");
				}
				
			}
			
			writer.close();
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
				long[] msgTimes = hotspotMap.get(s).getDayHrMsgs();
				for(int i = 0; i < msgTimes.length; i++){
					writer.print(msgTimes[i] + ",");
				}
				
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		writer.close();
		
		
	}
	
	public void generateAllTimeActivity(){
		if(threads.isEmpty()){
			return;
		}
		
		HashMap<String, HashMap<String, taggedAL>> msgDateMap = new HashMap<String, HashMap<String, taggedAL>>();
		Set<String> ConsistentKeySet = new TreeSet<String>();
		for(int i = 0; i < threads.size(); i++){
			for(int j = 0; j < threads.get(i).getMessageCount(); j++){
				
				Message msg = threads.get(i).getMessage(j);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(msg.getStrTZ()));
				cal.setTime(msg.getDate());
				String dateKey = (cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH)+1) +"-" + cal.get(Calendar.YEAR));
				HashMap<String, taggedAL> messagesOnDay = new HashMap<String, taggedAL>();
				
				if(msgDateMap.get(dateKey) == null){					
					
					taggedAL newTaggedAL = new taggedAL(msg.getSender());
					newTaggedAL.messages.add(msg);
					messagesOnDay.put(msg.getSender(), newTaggedAL);
					msgDateMap.put(dateKey, messagesOnDay);
					
				} else {
					messagesOnDay = msgDateMap.get(dateKey);
					if(messagesOnDay.get(msg.getSender()) == null){
						taggedAL newTaggedAL = new taggedAL(msg.getSender());
						newTaggedAL.messages.add(msg);
						messagesOnDay.put(msg.getSender(), newTaggedAL);
						
					} else{
						taggedAL existingTaggedAL = messagesOnDay.get(msg.getSender());
						existingTaggedAL.messages.add(msg);
						messagesOnDay.put(msg.getSender(), existingTaggedAL);
					}
					msgDateMap.put(dateKey, messagesOnDay);
					
				}
				
				ConsistentKeySet.addAll(messagesOnDay.keySet());
				
			}
		}
		

		Calendar cal = null;
		boolean calSet = false;
		for(int i = 0; i < threads.size(); i++){
			if(threads.get(i).getMessageCount() > 0){
				cal = Calendar.getInstance(TimeZone.getTimeZone(threads.get(i).getMessage(0).getStrTZ()));
				calSet = true;
			}
		}
		
		if(!calSet){
			cal = Calendar.getInstance(TimeZone.getDefault());
		}
		
		
		
		
		
		boolean foundFirstMessage = false;
		cal.setTimeInMillis(1000);
		
		findFirst:
		while(cal.getTime().getTime() < System.currentTimeMillis()){
			cal.add(Calendar.DATE, 1);
			String dateKey = (cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH)+1) +"-" + cal.get(Calendar.YEAR));
			if(msgDateMap.get(dateKey) != null){
				break findFirst;
			}
		}
		final Date firstMsgDate = cal.getTime();
		
		String fName = createFolderName();
		AllMessages.createStatsDirs("/" + fName);
		String fileName = "fb-messages/stats/" + fName + "/" + fName + "_day activity.csv";
		
		PrintWriter writer;
		try{
			writer = new PrintWriter(new File(fileName));
			while(cal.getTime().getTime() < System.currentTimeMillis()){
				
				String dateKey = (cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH)+1) +"-" + cal.get(Calendar.YEAR));
				writer.print(","+dateKey);
				cal.add(Calendar.DATE, 1);
			}
		
			for(String s : ConsistentKeySet){
				cal.setTime(firstMsgDate);
				writer.println();
				writer.print(s);
				while(cal.getTime().getTime() < System.currentTimeMillis()){
					String dateKey = (cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH)+1) +"-" + cal.get(Calendar.YEAR));
					
					if(msgDateMap.get(dateKey) != null){
						if(msgDateMap.get(dateKey).get(s) != null){
							writer.print("," + msgDateMap.get(dateKey).get(s).messages.size());
						} else {
							writer.print(",0");
						}
					} else {
						writer.print(",0");
					}
					cal.add(Calendar.DATE, 1);
				}
			}
			writer.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		
		
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

class taggedAL{
	public String User;
	public ArrayList<Message> messages;
	
	public taggedAL(String user){
		this.User = user;
		messages = new ArrayList<Message>();
	}
}


