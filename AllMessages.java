package fbMessageReaderGUI;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AllMessages {
	private ArrayList<Conversation> allConvos = new ArrayList<Conversation>();
	private String user;
	
	public AllMessages (String userIn){
		this.user = userIn;
	}
	
	public void generateStats(){
		//printConvos();
		printConvoNumbers();
		createStatsDirs();	
		generateDailyHotspots();
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
			allConvos.add(con);
		}
	}
	
	private void createStatsDirs(){
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
	
	private void createStatsDirs(String input){
		Path currentDir = Paths.get("");
		String strPath = currentDir.toAbsolutePath().toString();
		strPath = strPath + "/fb-messages/stats" + input;
		Path p = Paths.get(strPath);
		try{
			Files.createDirectories(p);
		} catch (SecurityException e){
			System.out.println("Security Exception, try running elevated or in a different directory");
		} catch (Exception e){
			System.out.println(e.toString());
		}
		
	}
	
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
	
	public void generateDailyHotspots(){
		Calendar cal; 
		long userMsgs15min[] = new long[96];
		long userMsgsHr[] = new long[24];
		long userMsgs5min[] = new long[288];
		long otherMsgs15min[] = new long[96];
		long otherMsgsHr[] = new long[24];
		long otherMsgs5min[] = new long[288];
		
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
		
	}
	
	public void generateWeeklyHotspots(){
		
	}
}
