import java.io.*;

public class PollRequestContext extends HttpContext{

    private PollMaster pollMaster;

    public PollRequestContext(HttpContext context, PollMaster pollMaster){
        super(context);

        this.ruleset.setUrlRule("/home/poll/request-available-polls");
        this.ruleset.setMethodRule(HttpMethod.GET);

        this.pollMaster = pollMaster;
    }


    @Override
    public void run(){
        if(this.request.getUrlParameters() == null) return;

        String username = this.request.getUrlParameters()[0][1];
        File file = new File("src/data/runtime/pollrequest.txt");

        try{
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(pollMaster.getPollStrings());
            writer.close();

            this.webpageSocket.getOutputStream().write(new HTTPResponse(file).toBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
    

    }


}
