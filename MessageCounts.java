package fbMessageReaderGUI;

import java.util.Comparator;

public class MessageCounts{
	public String User;
	public long msgTotal = 0;
	public long wordTotal = 0;
	public long charTotal = 0;
	
	public MessageCounts(String user){
		this.User = user;
	}

	
	public double getAvg(){
		double avg = (double)wordTotal/(double)msgTotal;
		return avg;
	}
	
	public static MessageCounts addCounts(MessageCounts count1, MessageCounts count2){
		MessageCounts countOut = new MessageCounts("adding counts");
		if(count1.User.equals(count2.User)){
			countOut.User = count1.User;
			countOut.msgTotal = count1.msgTotal + count2.msgTotal;
			countOut.charTotal = count1.charTotal + count2.charTotal;
			countOut.wordTotal = count1.wordTotal + count2.wordTotal;
		} else{
			countOut.msgTotal = -1;
			countOut.charTotal = -1;
			countOut.wordTotal = -1;
			countOut.User = count1.User + count2.User;
		}
		return countOut;
	}
}

class MsgTotalComparator implements Comparator<MessageCounts> {
    public int compare(MessageCounts count1, MessageCounts count2) {
    	return (int) (count2.msgTotal - count1.msgTotal);
    }
}
