import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Random;




class Attempt{

    private Integer id;
    private String username;
    private boolean complete = false;

    public Attempt(String username){
        this.username = username;
        Random rand = new Random();
        this.id = rand.nextInt(1_000_000, Integer.MAX_VALUE);
    }

    public Integer getId(){
        return id;
    }

    public String getUsername(){
        return username;
    }

    public boolean getStatus(){
        return complete;
    }

    public void setStatus(boolean newStatus){
        this.complete = newStatus;
    }


}


public class LoginRequestContext extends HttpContext{

    private CSV registrants;
    public static LinkedList<Attempt> attempts = new LinkedList<>();

    public LoginRequestContext(HttpContext context, CSV registrants){
        super(context);
        this.registrants = registrants;

        this.ruleset.setUrlRule("/login/process-credentials");
        this.ruleset.setMethodRule(HttpMethod.GET);
    }




    @Override
    public void run(){

        String attemptedUsername = request.getUrlParameters()[0][1];
        String attemptedPassword = request.getUrlParameters()[1][1];

        try{
            File file = new File("src/data/runtime/loginrequest.txt");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);

            if(registrants.hasRow(Main.arr(attemptedUsername, attemptedPassword))){
                Attempt a = new Attempt(attemptedUsername);
                attempts.add(a);
                writer.write("success;");
                writer.write(String.valueOf(a.getId()));
            }else{
                writer.write("fail");
            }
            writer.close();

            HTTPResponse response = new HTTPResponse(file);
            webpageSocket.getOutputStream().write(response.toBytes());
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public LinkedList<Attempt> getAttempts(){
        return attempts;
    }

    
}
