import java.util.ArrayList;

public class ContentBlock {

    private ArrayList<String[]> content = new ArrayList<>();
    private Integer contentAmount = 0;
    private String tag = null;



    public ContentBlock(String rawText){
        String[] blocks = rawText.split(":");
        String[] headerBlock = blocks[0].split(",");

        contentAmount = blocks.length;
        tag = headerBlock[0];

        if(blocks.length == 1){
            return;
        }

        for(int i = 1 ; i < blocks.length ; i++){
            content.add(blocks[i].split(","));
        }
    }


    public Integer contentAmount(){
        return contentAmount;
    }

    
    public String[] getBlock(String identifier){
        for(String[] block : content){
            if(block[0].equals(identifier)){
                return block;
            }
        }
        return new String[0];
    }


    public String toString(){

        StringBuffer buff = new StringBuffer();
        buff.append(tag).append(",").append(contentAmount).append(":");
        for(String[] block : content){

            for(String s : block){
                buff.append(s).append(",");
            }
            buff.deleteCharAt(buff.length() - 1);
            buff.append(":");
        }

        buff.deleteCharAt(buff.length() - 1);
        return buff.toString();

    }


    public String getTag(){
        return this.tag;
    }


    
}
