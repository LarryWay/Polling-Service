





public class ValidateLoginRequestContext extends HttpContext{

    public ValidateLoginRequestContext(HttpContext context){
        super(context);

        this.ruleset.setUrlRule("/home");
        this.ruleset.setMethodRule(HttpMethod.GET);

    }


    @Override
    public void run(){

        if(this.request.getUrlParameters() == null) return;

        String username = this.request.getUrlParameters()[0][1];
        Integer id = Integer.valueOf(this.request.getUrlParameters()[1][1]);

        for(Attempt attempt : LoginRequestContext.attempts){
            if(attempt.getUsername().equals(username) && attempt.getId().equals(id)){
                LoginRequestContext.attempts.remove(attempt);

                try{
                    HTTPResponse response = new HTTPResponse(Main.createUserHtml_Login(Main.arr(username)));
                    webpageSocket.getOutputStream().write(response.toBytes());
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }

    }

}
