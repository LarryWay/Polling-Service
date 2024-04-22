import java.util.ArrayList;

public class PollMaster {
    
    private ArrayList<Poll> allPolls = new ArrayList<>();

    public PollMaster(){

    }


    public void addPoll(Poll p){
        allPolls.add(p);
    }


    public synchronized String getPollStrings(){
        StringBuffer buffer = new StringBuffer();

        for(Poll poll : allPolls){
            buffer.append(poll.getTitle()).append(',');
        }
        buffer.delete(buffer.length() - 1, buffer.length());

        return buffer.toString();

    }
    
    public synchronized Integer size(){
        return allPolls.size();
    }

    public ArrayList<Poll> getAllPolls(){
        return allPolls;
    }


    public Poll getPoll(String title){
        for(Poll poll : allPolls){
            if(poll.getTitle().equals(title)){
                return poll;
            }
        }

        return Poll.getNullPoll();


    }


}
