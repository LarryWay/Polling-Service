import java.io.*;
import java.util.Scanner;




public class Main {


    public static String[] arr(String... x){
        return x;
    }


    public static File initializeHtmlPollFile(Poll poll){
        final String FOLDER_LOCATION = "src/html/unique_polls/";
        File file = new File(FOLDER_LOCATION + poll.getTitle());
        File template = new File("src/html/poll_template.html");

        try{
            file.createNewFile();

            Scanner scan = new Scanner(template);
            StringBuffer buffer = new StringBuffer();

            while(scan.hasNextLine()){
                buffer.append(scan.nextLine()).append('\n');
            }

            String newHtml = buffer.toString();
            newHtml = newHtml.replace("{{POLL_TITLE}}", poll.getTitle());

            StringBuffer buttonCreation = new StringBuffer();
            for(String option : poll.getOptions()){
                buttonCreation.append("<input type=\"button\" value=\"" + option + "\" onclick=\"sendPostRequest('" + option + "')\">" );
            }
            newHtml = newHtml.replace("{{HTML_CONTENT}}", buttonCreation.toString());




            FileWriter writer = new FileWriter(file);
            writer.write(newHtml);
            writer.close();


        }catch(Exception e){
            e.printStackTrace();
        }

        return file;

    }




    public static File createUserHtml_Login(String[] data){
        String username = data[0];
        File file = new File("src/html/home_template.html");
        File newFile = new File("src/html/userhtmls/" + username);

        StringBuffer buffer = new StringBuffer(); 


        try{
            Scanner scan = new Scanner(file);
            while(scan.hasNextLine()){
                buffer.append(scan.nextLine());
            }
            String content = buffer.toString();
            content = content.replace("{{username}}", username);
    
            FileWriter writer = new FileWriter(newFile);
            writer.write(content);
    
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }



        return newFile;

    }




    
    public static void main(String[] args) {

        System.out.println("Loading CSV Files...");

        File registrantsFile = new File("src/data/registrants.csv");

        try{
            registrantsFile.createNewFile();
            FileWriter registrantsWriter = new FileWriter(registrantsFile);
            registrantsWriter.write("email,password");
            registrantsWriter.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        CSV registrants = new CSV(registrantsFile);





        System.out.println("Starting pre-polling process..");
        
        // Where user should have poll configurability

        PollMaster pollMaster = new PollMaster();
        Poll poll = new Poll("Presidents", Main.arr("Joe-Biden", "Donald-Trump"));
        Poll carsPoll = new Poll("Cars", Main.arr("New-Cars", "Old-Cars", "Used-Cars", "Vintage-Cars"));

        pollMaster.addPoll(poll);
        pollMaster.addPoll(carsPoll);

        for(Poll p : pollMaster.getAllPolls()){
            initializeHtmlPollFile(p);
        }





        System.out.println("Starting server...");


        HttpWebServer server = new HttpWebServer(8080);
        DefaultHtmlWebpageDisplayContext dhwpc = new DefaultHtmlWebpageDisplayContext(server.createContext());
        LoginRequestContext lrc = new LoginRequestContext(server.createContext(), registrants);
        PollRequestContext prc =  new PollRequestContext(server.createContext(), pollMaster);
        GetPollPageContext gppc = new GetPollPageContext(server.createContext(), pollMaster);

        
        server.addContext(dhwpc);
        server.addContext(lrc);
        server.addContext(prc);
        server.addContext(gppc);

        server.addDirectory("/registration", new File("src/html/registration.html"));
        server.addDirectory("/login", new File("src/html/login.html"));
        server.addDirectory("/homepagedata", new File("src/html/login.html"));
        server.addDirectory("/home", new File("src/html/home_template.html"));
        server.addDirectory("/", new File("src/html/index.html"));

        server.addPostContentBinder("/registration");
        server.addPostContentBinder("/login");
        server.addPostContentBinder("/home/polls");


        server.startServer();


        System.out.println("Beginning main loop...");


        InputStream in = server.getInputStream();
        OutputStream out = server.getOutputStream();

        while(true){

            try{


                while(in.available() == 0){ }


                StringBuffer buff = new StringBuffer();
                while(in.available() != 0){
                    buff.append((char) in.read());
                }

                ContentBlock block = new ContentBlock(buff.toString());

                if(block.getTag().equals("RegistrationQueue")){
                    String newUsername = block.getBlock("email")[1];
                    String newPassword = block.getBlock("password")[1];
                    
                    if(registrants.hasRow(arr(newUsername, newPassword))){
                        System.out.println("Registrant already registered");
                    }else{
                        System.out.println("Registering new user");
                        registrants.appendRow(arr(newUsername, newPassword));
                    }
                }



                if(block.getTag().equals("PollSubmission")){
                    
                    String castedVote = block.getBlock("vote")[1];
                    String username = block.getBlock("user")[1];
                    String pollname = block.getBlock("pollname")[1];
                    Poll submittedPoll = pollMaster.getPoll(pollname);

                    submittedPoll.addEntry(username, castedVote);
                }
                


            


            }catch(Exception e){
                e.printStackTrace();
                break;
            }

        }


    }

}
