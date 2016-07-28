package fbMessageReaderGUI;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class MainFBParse {
	
	public static String scanAll (String filePath, boolean generateStats){
		
		generateStats = true;
		
		File file = new File(filePath);
		
		long startTime = System.currentTimeMillis();
		
		Path currentDir = Paths.get("");
		String strPath = currentDir.toAbsolutePath().toString();
		strPath = strPath + "/fb-messages/threads";
		Path p = Paths.get(strPath);
		
		try{
			Files.createDirectories(p);
		} catch (SecurityException e){
			System.out.println("Security Exception, try running elevated or in a different directory");
		} catch (Exception e){
			System.out.println(e.toString());
		}

		
		Scanner scan = null;
		BufferedReader br = null;
		FileReader fr = null;

		
		try{
			System.out.println(file);
			
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			scan = new Scanner(br);
			
		} catch (FileNotFoundException e){
			System.out.println("Couldn't find messages.htm in the same folder as this program");
			System.out.println("Press ENTER to close");
			
			try{
	            System.in.read();
	        }catch(Exception e2){
	        	System.exit(0);
	        }
			System.exit(0);

		}
		
		
		String input;
		boolean pTagOpen = false;
		boolean msgTagOpen = false;
		boolean threadTagOpen = false;
		boolean userTagOpen = false;
		boolean metaTagOpen = false;
		boolean participantsOpen = false;
		String User = "";
		
		input = scan.findWithinHorizon("/><title>", 1000000);
		boolean titleTagOpen = true;
		
		while(titleTagOpen){
			input = scan.next();
			User = User + " " + input;
			if(input.contains("</title>")){
				titleTagOpen = false;
			}
		}
		int cutIndex = User.indexOf(" - Messages</title><link");
		User = User.substring(1, cutIndex);
		System.out.println("User is: " + User);
		
		AllMessages allMsgs = new AllMessages(User);
		MessageThread.setUser(User);
		
		
		
		input = scan.findWithinHorizon("class=\"thread\">", 1000000);
		threadTagOpen = true;
		
		
		participantsOpen = true;
		String Startparticipants = input.substring(15);
		System.out.println();
		while(participantsOpen){
			input = scan.next();
			Startparticipants = Startparticipants + " " + input;
			
			if(input.contains("<div")){
				participantsOpen = false;
			}
		}

		
		int Startendex = Startparticipants.indexOf("<div");
		
		Startparticipants = Startparticipants.substring(0, Startendex);
		System.out.println(Startparticipants);
		
		String StartparticipantsSplit[] = Startparticipants.split(",");
		

		MessageThread thread = new MessageThread(StartparticipantsSplit);
		
		if(generateStats){
			allMsgs.addThread(thread);
		}
		
		while(scan.hasNext() == true){
			
			
			
			inThread:
			while(threadTagOpen){
				
				//user tag, meta tag, message text
				if(scan.hasNext() == false){
					thread.printThreadToFile();
					if(generateStats){
						allMsgs.addThread(thread);
					}
					break inThread;
				}
				
				String Sender = "";
				input = scan.next();
				
				if(input.contains("class=\"thread\">")){
					participantsOpen = true;
					thread.printThreadToFile();
					if(generateStats){
						allMsgs.addThread(thread);
					}
					
					
					String participants = input.substring(15);
					
					while(participantsOpen){
						input = scan.next();
						participants = participants + " " + input;
						
						if(input.contains("<div")){
							participantsOpen = false;
						}
					}
					int endex = participants.indexOf("<div");
					
					participants = participants.substring(0, endex);
					String participantsSplit[] = participants.split(",");			
	
					
					thread = new MessageThread(participantsSplit);
					break inThread;
				}
				
				
				if(input.contains("class=\"user\">")){
					userTagOpen = true;
					Sender = input.substring(13);
					while (userTagOpen == true){
						
						input = scan.next();
						Sender = Sender + " " + input;
						
						if(input.contains("</span>")){
							userTagOpen = false;
						}
					}
					int indexFoCut = Sender.indexOf("</span>");
					Sender = Sender.substring(0, indexFoCut);
					
					Message msg = new Message(Sender);
					
					input = scan.next();
					
					if(input.contains("class=\"meta\">")){
						metaTagOpen = true;
						ArrayList<String> metaDetails = new ArrayList<String>();
						
						while(metaTagOpen == true){
							input = scan.next();
							metaDetails.add(input);
							
							if(input.contains("</span>")){
								metaTagOpen = false;
								String metaString = "";
								for(int i = 0; i < metaDetails.size(); i++){
									metaString  = metaString + " " + metaDetails.get(i);
								}
								msg.setDate(metaString);
							}
						}
						
						ArrayList<String> msgText = new ArrayList<String>();
						String msgTextStart = input.substring(input.indexOf("<p>"));
						msgText.add(msgTextStart);
						pTagOpen = true;
						while (pTagOpen){						
							if (input.contains("</p>")){
								pTagOpen = false;
							} else {
								input = scan.next();
								msgText.add(input);
							}
						}
						String finalMsgText = "";
						for(int i = 0; i < msgText.size(); i++){
							finalMsgText = finalMsgText + " " + msgText.get(i);
						}
						msg.setText(finalMsgText);
						thread.addMessage(msg);
						msgTagOpen = false;		
						
						
					}
				}							
			}
		}
		
		
		
		if(generateStats){
			System.out.println("generating stats");
			allMsgs.generateStats();
		}
		
		
		long endTime = System.currentTimeMillis();
		long timeTaken = endTime - startTime;
		double timeTakenSeconds = timeTaken * Math.pow(10, -3);
		
		String output = ("Scanned and outputted " + MessageThread.getThreadNumber() + " threads in " +  String.format("%.2f", timeTakenSeconds) + " [s]");
		
		
		
		
		return output;
		
		
	}
	
}
