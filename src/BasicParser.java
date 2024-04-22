

public class BasicParser {

    public static String[][] parse(String rawInput){

        Integer baseSize = rawInput.length() - rawInput.replace(":","").length() + 1;
        String[][] returnArray = new String[baseSize][];

        String[] splits = rawInput.split(":");
        int count = 0;

        for(String split : splits){
            returnArray[count] = split.split(",");
            count++;
        }

        return returnArray;

    }

}
