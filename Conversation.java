package fbMessageReaderGUI;

import java.util.ArrayList;

public class Conversation {
	private ArrayList<String> participants = new ArrayList<String>();
	private ArrayList<MessageThread> threads = new ArrayList<MessageThread>();

	
	
	
	public Conversation(ArrayList<String> partsIn){
		for (int i = 0; i < partsIn.size(); i++){
			this.participants.add(partsIn.get(i));
		}
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
	
	
}
