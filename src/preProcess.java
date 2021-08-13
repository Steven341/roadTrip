import java.io.File;
import java.util.*;
import java.util.ArrayList;

public class preProcess {
    ArrayList<String> cities = new ArrayList<>();
    Set<String> uniqueCities = new HashSet<>();
    private String[] parse(String data){return data.split(",");}
    private void parseCities() throws Exception {
        File myObj = new File("roads.csv");

        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            uniqueCities.add(parse(data)[0]);
            uniqueCities.add(parse(data)[1]);
        }
        myReader.close();

        Iterator it = uniqueCities.iterator();
        while(it.hasNext())
            cities.add((String) it.next());
        sortArray(cities);
        for (int i = 0;i<cities.size()-1;i++){
            System.out.println(i+" "+cities.get(i));
        }
    }
    private void sortArray(ArrayList<String> a){
        for(int i = 0; i<a.size()-1; i++) {
            for (int j = i+1; j<a.size(); j++) {
                if(a.get(i).compareTo(a.get(j))>0) {
                    String temp = a.get(i);
                    a.set(i,a.get(j));
                    a.set(j,temp);
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        preProcess p = new preProcess();
        p.parseCities();
        System.out.println();
    }
}
