import java.io.File;
import java.io.*;
import java.util.ArrayList;



public class Poll {

    private final String CSV_PATH = "src/data/polls/";

    private String title;
    private ArrayList<String> options = new ArrayList<>();
    private CSV data;

    private static final Poll nullPoll = new Poll("NULL", Main.arr("NULL"));


    public Poll(String title, String[] options){
        this.title = title;

        for(String o : options){
            this.options.add(o);
        }

        File file = new File(CSV_PATH + title + ".csv");
        

        try{

            if(file.createNewFile()){
                FileWriter writer = new FileWriter(file);
                writer.write("name,option");
                writer.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        data = new CSV(file);
    }


    public void addEntry(String name, String option){
        boolean toggleDebug = true;
        boolean enableChangeability = true;


        if(options.contains(option)){
            if(data.hasRow(Main.arr(name, CSV.Flags.ANY))){
                if(enableChangeability){
                    data.modUniqueRow(Main.arr(name, CSV.Flags.ANY), Main.arr(name, option));
                }else{
                    if(toggleDebug){
                        System.out.println("Debug::Warning: \"" + name + "\" already voted in poll");
                    }
                }


            }else{
                data.appendRow(Main.arr(name, option));    
            }
        }else{
            if(toggleDebug){
                System.out.println("Debug::Warning: \"" + option + "\" is not a valid option");
            }
        }
    }


    public String getTitle(){
        return this.title;
    }

    public ArrayList<String> getOptions(){
        return options;
    }

    public static Poll getNullPoll(){
        return nullPoll;
    }

    
    public String getDetailedResults(){

        StringBuffer buff = new StringBuffer();
        int votes = data.countRows();
        buff.append(title).append(",").append(votes + 1).append(":");

        String[][] allResults = data.getAllRows(Main.arr(CSV.Flags.ANY, CSV.Flags.ANY));

        for(String[] row : allResults){
            buff.append(row[0]).append(",").append(row[1]).append(":");
        }

        if(votes > 1){
            buff.deleteCharAt(buff.length() - 1);
        }


        return buff.toString();


    }


    public String getNumberResults(){
        
        StringBuffer buffer = new StringBuffer();

        int totalVotes = data.countRows();

        buffer.append(title).append(",").append(options.size() + 1).append(":");
        
        
        for(String op : options){
            String[][] totalOptionVotes = data.getAllRows(Main.arr(CSV.Flags.ANY, op));
            buffer.append(op).append(",").append(totalOptionVotes.length).append(":");
        }

        
        buffer.deleteCharAt(buffer.length() - 1);


        return buffer.toString();

    }



}


