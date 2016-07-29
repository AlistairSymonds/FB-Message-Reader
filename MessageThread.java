package fbMessageReaderGUI;
import java.text.SimpleDateFormat;
import java.util.*;


import java.io.*;


public class MessageThread {
	private ArrayList<Message> messages;
	private static int threadNumber;
	private static SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat printFt = new SimpleDateFormat("kk:mm 'on' dd MMMM yyyy z");
	private ArrayList<String> participants = new ArrayList<String>();
	private static String User;
	
	public static void setUser(String userIn){
		User = userIn;
	}
	
	public MessageThread(String partsIn[]){
		
		for(int i = 0; i < partsIn.length; i++){
			this.participants.add(partsIn[i]);
		}
		
		for(int i = 0; i < this.participants.size(); i++){
			if(this.participants.get(i).equals(User)){
				this.participants.remove(i);
			}
		}
		messages = new ArrayList<Message>();
	}
	
	public void addMessage(Message inMsg){
		messages.add(inMsg);
	}
	
	public void printThread(){
		for(int i = 0; i < messages.size(); i++){
			messages.get(i).printMsgToConsole();
		}
	}
	
	public Message getMessage(int index){
		return messages.get(index);
	}
	
	public static int getThreadNumber(){
		return threadNumber;
	}
	
	public ArrayList<String> getParticipants(){
		return this.participants;
	}
	
	public void printThreadToFile(){
		String printDate;
		String fileName;
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
			partsString = "qGroup " + partsString;
		} else {
			for (int i = 0; i < participants.size(); i++){
				partsString = partsString + " " + this.participants.get(i);
			}
		}
		
		
		if(messages.size() > 0){
			printDate = ft.format(messages.get(0).getDate());
			fileName = partsString + " " + printDate + "_" + threadNumber + ".txt";
			fileName = fileName.substring(1);
			fileName = "fb-messages/threads/" + fileName;
		} else{
			fileName = "fb-messages/threads/EMPTY THREAD " + threadNumber + ".txt";
		}
		
		
		System.out.println("File name is: " + fileName);
		threadNumber++;

		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(new File(fileName));
			if(participants.size() > 2){
				writer.println("Group conversation, participants at time of download: ");
				for(int i = 0; i < participants.size(); i++){
					writer.println(participants.get(i));
				}
				
				writer.println("(This list does not contain those who have left the group chat, "); 
				writer.println("however their messages will still appear below)");
				writer.println();
			}
			for(int i = messages.size() - 1; i >= 0; i--){
				writer.println(messages.get(i).getSender() + " --- " + printFt.format(messages.get(i).getDate()));
				writer.println(messages.get(i).getText());
				writer.println();
			}
			writer.close();
		} catch (Exception e){
			System.out.println(e.toString());
		}
	}

	public int getMessageCount() {
		return messages.size();
	}
	
}
