package fbMessageReaderGUI;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

public class AllMessages {
	private ArrayList<Conversation> allConvos = new ArrayList<Conversation>();
	private static String user;
	
	public AllMessages (String userIn){
		user = userIn;
	}
	
	public static String getUser(){
		return user;
	}
	
	public void generateStats(){
		printConvoNumbers();
		createStatsDirs();	
		
		generateDailyHotspots();
		generateWeeklyHotspots();
		
		generateAvgMsgLength();
		for(int i = 0; i< allConvos.size(); i++){
			allConvos.get(i).generateMsgAvgLengths();
		}
		
		for(int i = 0; i < allConvos.size(); i++){
			allConvos.get(i).generateHotspots();
		}
		
		for(int i = 0; i< allConvos.size(); i++){
			allConvos.get(i).whoStartedIt();
		}
		
		for(int i = 0; i < allConvos.size(); i++){
			allConvos.get(i).generateAllTimeActivity();
		}
		
		generateTopPeople();
	}
	
	public ArrayList<Conversation> getConvos(){
		return this.allConvos;
	}
	
	public void addThread(MessageThread thread){
		boolean convoExists = false;
		
		for(int i = 0; i < thread.getParticipants().size(); i++){
			if(thread.getParticipants().get(i).equals(user)){
				thread.getParticipants().remove(i);
			}
		}
		
		
		if(allConvos.isEmpty()){
			Conversation con = new Conversation(thread.getParticipants());
			allConvos.add(con);
			return;
		}
		
		for(int i = 0; i < allConvos.size();  i++){
			if(compareArrayListString(thread.getParticipants(), allConvos.get(i).getParticipants())&&convoExists == false){
				System.out.println("Convos match " + allConvos.get(i).getParticipants() + thread.getParticipants());
				allConvos.get(i).addMessageThread(thread);
				convoExists = true;
			}
		}
		
		if(convoExists == false){
			System.out.println("Creating new convo for " + thread.getParticipants());
			Conversation con = new Conversation(thread.getParticipants());
			con.addMessageThread(thread);
			allConvos.add(con);
		}
	}
	
	public static void createStatsDirs(){
		Path currentDir = Paths.get("");
		String strPath = currentDir.toAbsolutePath().toString();
		strPath = strPath + "/fb-messages/stats";
		Path p = Paths.get(strPath);
		try{
			Files.createDirectories(p);
		} catch (SecurityException e){
			System.out.println("Security Exception, try running elevated or in a different directory");
		} catch (Exception e){
			System.out.println(e.toString());
		}
		strPath = strPath + "/fb-messages/stats/";
		p = Paths.get(strPath);
	}
	
	public static void createStatsDirs(String input){
		Path currentDir = Paths.get("");
		String strPath = currentDir.toAbsolutePath().toString();
		strPath = strPath + "/fb-messages/stats" + input;
		System.out.println("Making dir " + strPath);
		Path p = Paths.get(strPath);
		try{
			Files.createDirectories(p);
		} catch (SecurityException e){
			System.out.println("Security Exception, try running elevated or in a different directory");
		} catch (Exception e){
			System.out.println(e.toString());
		}
		
	}
	
	@SuppressWarnings("unused")
	private void printConvos(){
		System.out.println("Printing convos below...");
		for (int i = 0; i < allConvos.size(); i++){
			System.out.println(allConvos.get(i).getParticipants());
		}
	}
	
	private void printConvoNumbers(){
		System.out.println(MessageThread.getThreadNumber() + " threads were put into " + allConvos.size() + " conversations");
	}
	
	public static boolean compareArrayListString(ArrayList<String> list1, ArrayList<String> list2){
		
		if(list1.isEmpty() && list2.isEmpty()){
			return true;
		}
		if(list1.isEmpty() || list2.isEmpty()){
			return false;
		}
		if(list1.size() != list2.size()){
			return false;
		}
		

		for(int i = 0; i < list1.size(); i++){
			if(list1.get(i).contains(list2.get(i))){
				//uh memes
			} else {
				return false;
			}
		}
		return true;
	}
	public void generateAvgMsgLength(){
		long totalUserMsgWords = 0;
		long totalUserMsgs = 0;
		
		long totalOtherMsgWords = 0;
		long totalOtherMsgs = 0;
		
		for(int i = 0; i < allConvos.size(); i++){// iterate through conversations
			for(int j = 0; j < allConvos.get(i).getThreads().size(); j++){//iterate through threads in i'th conversation
				for(int k = 0; k < allConvos.get(i).getThreads().get(j).getMessageCount(); k++){
					Message msg = allConvos.get(i).getThreads().get(j).getMessage(k);
					if(msg.getSender().contains(user)){
						totalUserMsgWords = totalUserMsgWords + msg.getText().split(" ").length;
						totalUserMsgs++;
					} else {
						totalOtherMsgWords = totalOtherMsgWords + msg.getText().split(" ").length;
						totalOtherMsgs++;
					}
					
				}
			}
		}
		
		PrintWriter writer = null;
		createStatsDirs("/ALL MESSAGES/");
		String fileName = "fb-messages/stats/ALL MESSAGES/Message Length Averages.csv";
		try{
			writer = new PrintWriter(new File(fileName));
			writer.print(user + "'s average message length," + (totalUserMsgWords/totalUserMsgs));
			
			writer.println();
			writer.print("Other's average message length," + totalOtherMsgWords/totalUserMsgs);
		} catch (Exception e){
			e.printStackTrace();
		}
		writer.close();
	}
	
	public void generateDailyHotspots(){
		Calendar cal; 
		long userMsgsHr[] = new long[24];
		long otherMsgsHr[] = new long[24];

		
		for(int i = 0; i < allConvos.size(); i++){// iterate through conversations
			for(int j = 0; j < allConvos.get(i).getThreads().size(); j++){//iterate through threads in i'th conversation
				for(int k = 0; k < allConvos.get(i).getThreads().get(j).getMessageCount(); k++){
					Message msg = allConvos.get(i).getThreads().get(j).getMessage(k);
					cal = Calendar.getInstance(TimeZone.getTimeZone(msg.getStrTZ()));
					
					cal.setTime(msg.getDate());
					if(msg.getSender().contains(user)){
						userMsgsHr[cal.get(Calendar.HOUR_OF_DAY)]++;
					} else {
						otherMsgsHr[cal.get(Calendar.HOUR_OF_DAY)]++;
					}
				}
			}
		}
		System.out.println("Your messages over 24hrs");
		for(int i = 0; i < 24; i++){
			System.out.println(userMsgsHr[i]);
		}
		System.out.println("Others messages over 24hrs");
		for(int i = 0; i < 24; i++){
			System.out.println(otherMsgsHr[i]);
		}
		
		PrintWriter writer = null;
		createStatsDirs("/ALL MESSAGES/");
		String fileName = "fb-messages/stats/ALL MESSAGES/Day Hourly Hotspot.csv";
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
	
	public void generateWeeklyHotspots(){
		Calendar cal;
		long userMsgsHr[] = new long[24*7];
		long otherMsgsHr[] = new long[24*7];
		for(int i = 0; i < allConvos.size(); i++){// iterate through conversations
			for(int j = 0; j < allConvos.get(i).getThreads().size(); j++){//iterate through threads in i'th conversation
				for(int k = 0; k < allConvos.get(i).getThreads().get(j).getMessageCount(); k++){
					Message msg = allConvos.get(i).getThreads().get(j).getMessage(k);
					cal = Calendar.getInstance(TimeZone.getTimeZone(msg.getStrTZ()));
					
					cal.setTime(msg.getDate());
					if(msg.getSender().contains(user)){
						userMsgsHr[cal.get(Calendar.HOUR_OF_DAY) + 24 * (cal.get(Calendar.DAY_OF_WEEK) - 1)]++;
					} else {
						otherMsgsHr[cal.get(Calendar.HOUR_OF_DAY) + 24 * (cal.get(Calendar.DAY_OF_WEEK) - 1)]++;
					}
				}
			}
		}
		PrintWriter writer = null;
		createStatsDirs("/ALL MESSAGES/");
		String fileName = "fb-messages/stats/ALL MESSAGES/Week Hourly Hotspot.csv";
		try{
			writer = new PrintWriter(new File(fileName));
			writer.print(user.split(" ")[0] + "'s messages per hour over 24hr");
			for(int i = 0; i < userMsgsHr.length; i++){
				writer.print(","+userMsgsHr[i]);
			}
			writer.println();
			writer.print("Other's messages per hour over 24hr");
			for(int i = 0; i < otherMsgsHr.length; i++){
				writer.print(","+otherMsgsHr[i]);
			}
			
			
			
		} catch (Exception e){
			e.printStackTrace();
		}
		writer.close();
		
	}
	
	public void generateTopPeople(){
		
		HashMap<String, MessageCounts> map = new HashMap<String, MessageCounts>();
		for(int i = 0; i < allConvos.size(); i++){
			
			
			HashMap<String, MessageCounts> tempMap = allConvos.get(i).getMsgLenMap();
			Set<String> keys = tempMap.keySet();
			
			//combine maps
			for (String s : keys) {
				if(map.get(s) == null){
					map.put(s, tempMap.get(s));
				} else {
					map.put(s, MessageCounts.addCounts(map.get(s), tempMap.get(s)));
				}
			}
		}
		Set<String> keys = map.keySet();
		ArrayList<MessageCounts> counts = new ArrayList<MessageCounts>();
		for(String s : keys){
			counts.add(map.get(s));
		}
		Collections.sort(counts, new MsgTotalComparator());
		Collections.sort(counts, new MsgTotalComparator());
		Collections.sort(counts, new MsgTotalComparator());
		Collections.sort(counts, new MsgTotalComparator());
		Collections.sort(counts, new MsgTotalComparator());
		
		String fileName = "fb-messages/stats/ALL MESSAGES/total msgs by person.csv";
		PrintWriter writer = null;
		try{
			writer = new PrintWriter(new File(fileName));
			writer.println("user,number of messages");
			for(int i = 0; i < counts.size(); i++){
				writer.println(counts.get(i).User+","+counts.get(i).msgTotal);
			}
			

		} catch (Exception e){
			e.printStackTrace();
		}
		writer.close();
		
		
	}
	private int dayOfWeekToInt(String dayStr){
		if (dayStr.equals("MONDAY")){
			return 0;
		} else if (dayStr.equals("TUESDAY")){
			return 1;
		}else if (dayStr.equals("WEDNESDAY")){
			return 2;
		}else if (dayStr.equals("THURSDAY")){
			return 3;
		}else if (dayStr.equals("FRIDAY")){
			return 4;
		}else if (dayStr.equals("SATURDAY")){
			return 5;
		} else{
			return 6;
		}
	}
}

