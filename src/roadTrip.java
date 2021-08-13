import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.ArrayList;

public class roadTrip {
    ArrayList<String[]> cities = new ArrayList<>();
    Set<String> uniqueCities = new HashSet<>();
    Hashtable<String, String> attractionsHashtable = new Hashtable<>();
    ArrayList<String> attractionsCities = new ArrayList<>();
    String startingCity;
    String endingCity;
    String attractionPlace;
    ArrayList<Vertex> v = new ArrayList<>();


    private String[] parse(String data){return data.split(",");}
    
    
    //read roads.csv
    private void parseCities() throws Exception {
        File myObj = new File("roads.csv");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            cities.add(new String[] {parse(data)[0],parse(data)[1],parse(data)[2]});
            uniqueCities.add(parse(data)[0]);
            uniqueCities.add(parse(data)[1]);
        }
        myReader.close();
    }
    
    //read attractions.csv
    private void parseAttraction() throws Exception{
        File myObj = new File("attractions.csv");
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            attractionsHashtable.put(parse(data)[0],parse(data)[1]);
        }

    }

    private void graph(){

        Iterator<String> i = uniqueCities.iterator();
        while (i.hasNext()){
            v.add(new Vertex(i.next()));
        }


        for (int j = 0;j<cities.size()-1;j++){
            String city1 = cities.get(j)[0];
            String city2 = cities.get(j)[1];
            Integer mile = Integer.parseInt(cities.get(j)[2]);

            Vertex b = v.get(find(city1));
            b.addNeighbour(new Edge(mile,b,v.get(find(city2))));
            v.set(find(city1),b);

            Vertex c = v.get(find(city2));
            c.addNeighbour(new Edge(mile,c,v.get(find(city1))));
            v.set(find(city2),c);

        }
    }
    private int find(String city){
        for (int i = 0;i<v.size()-1;i++){
            if (v.get(i).getName().equals(city)){
                return i;
            }
        }
        return 0;
    }
    private Boolean validCity(String city){
        return uniqueCities.contains(city);
    }
    public void menu() throws Exception {
        parseCities();
        parseAttraction();
        graph();
        while (true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Name of starting city (or EXIT to quit): ");
            startingCity = reader.readLine();
            if (startingCity.equals("EXIT")){
                graph();
                break;
            }
            while (!validCity(startingCity)){
                System.out.println("Invalid City");
                System.out.println("Name of starting city (or EXIT to quit): ");
                startingCity = reader.readLine();
            }
            System.out.println("Name of ending city: ");
            endingCity = reader.readLine();
            while (!validCity(endingCity)){
                System.out.println("Invalid City");
                System.out.println("Name of ending city (or EXIT to quit): ");
                endingCity = reader.readLine();
            }

            while (true){
                Boolean isAdded = false;
                System.out.println("List an attraction along the way (or ENOUGH to stoplisting): ");
                attractionPlace = reader.readLine();
                if (attractionPlace.equals("ENOUGH")){
                    break;
                }

                while (!attractionsHashtable.containsKey(attractionPlace)){
                    System.out.println("Attraction "+ attractionPlace+ " unknown.");
                    System.out.println("List an attraction along the way (or ENOUGH to stoplisting): ");
                    attractionPlace = reader.readLine();
                    if (attractionsHashtable.containsKey(attractionPlace)){
                        attractionsCities.add(attractionsHashtable.get(attractionPlace));
                        isAdded = true;
                        break;
                    }
                    if (attractionPlace.equals("ENOUGH")){
                        break;
                    }
                }
                if (isAdded)
                    continue;
                else {
                    attractionsCities.add(attractionsHashtable.get(attractionPlace));
                }
            }

            System.out.println("Here is the best route for your trip: ");
            System.out.println(route(startingCity,endingCity,attractionsCities));
            v.clear();
            attractionsCities.clear();
        }
    }

    public String route(String start,String end,ArrayList<String> attractions) throws Exception {
        ArrayList<ArrayList> road = new ArrayList<>();
        parseAttraction();
        parseCities();
        graph();
        String prev = start;
        for (String city: attractions){
            graph();
            road.add(compute(v.get(find(prev)),v.get(find(city))));
            prev = city;
            v.clear();
        }
        graph();
        road.add(compute(v.get(find(prev)),v.get(find(end))));
        return road.toString();

    }
    private ArrayList<String> compute(Vertex start, Vertex end){
        dpq(start);
        return trace(end);
    }
    private void dpq(Vertex start){
        Vertex actualVertex;
        Vertex v;
        PriorityQueue<Vertex> pq = new PriorityQueue<>();
        start.setDistance(0);
        pq.add(start);
        start.setVisited(true);
        while( !pq.isEmpty() ){

            actualVertex = pq.poll(); // Get head of priorityQueue and delete

            for(Edge edge : actualVertex.getAdjacenciesList()){
                v = edge.getTargetVertex();
                //if v has not been visited
                if(!v.isVisited()){
                    double newDistance = actualVertex.getDistance() + edge.getWeight();
                    if( newDistance < v.getDistance()){
                        pq.remove(v);
                        v.setDistance(newDistance);
                        v.setPredecessor(actualVertex);
                        pq.add(v);
                    }
                }
            }
            actualVertex.setVisited(true);
        }
    }

    private ArrayList<String> trace(Vertex targetVertex){
        ArrayList<String> path = new ArrayList<>();
        //iterate through vertex and add to arraylist
        for(Vertex vertex=targetVertex;vertex!=null;vertex=vertex.getPredecessor()){
            path.add(vertex.getName());
        }
        //reverse arraylist
        Collections.reverse(path);
        return path;
    }



    public static void main(String[] args) throws Exception {

        roadTrip r = new roadTrip();
        r.menu();
        ArrayList<String> cities = new ArrayList<>();
        cities.add("Boston MA");
        cities.add("Cleveland OH");
        r.route("San Jose CA","San Francisco CA",cities);

    }
}