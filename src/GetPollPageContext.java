import java.io.*;

public class GetPollPageContext extends HttpContext{

    private PollMaster polls;

    public GetPollPageContext(HttpContext context, PollMaster master){
        super(context);

        this.ruleset.setUrlRule("/home/polls");
        this.ruleset.setMethodRule(HttpMethod.GET);

        this.polls = master;
    }


    @Override
    public void run(){

        if(request.getUrlParameters() == null) return;

        String title = request.getUrlParameters()[0][1];

        Poll p = polls.getPoll(title);
        
        if(p == Poll.getNullPoll()) System.out.println("ERROR");

        try{
            File file = new File("src/html/unique_polls/" + title);
            webpageSocket.getOutputStream().write(new HTTPResponse(file).toBytes());

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    
    
}
