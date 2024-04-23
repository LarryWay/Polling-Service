


public class GetAnalyticsContext extends HttpContext{

    PollMaster pollmaster;

    public GetAnalyticsContext(HttpContext context, PollMaster pollmaster){
        super(context);

        ruleset.setUrlRule("/analytics/request-data");
        ruleset.setMethodRule(HttpMethod.GET);

        this.pollmaster = pollmaster;
    }







    @Override
    public void run(){

        if(request.getUrlParameters() == null) return;

        String requestedPollTitle = request.getUrlParameters()[0][1];
        Poll confirmedPoll = pollmaster.getPoll(requestedPollTitle);

        // Check to see if the requested poll exists
        if(confirmedPoll == Poll.getNullPoll()){
            return;
        }

        try{
            webpageSocket.getOutputStream().write(new HTTPResponse(confirmedPoll.getNumberResults()).toBytes());
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}
