import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParseException {
        String json = readString("data.json");
        List<Employee> list = jsonToList(json);
    }

    private static List<Employee> jsonToList(String json) throws ParseException {
        JSONArray array = (JSONArray) new JSONParser().parse(json);
        Gson gson = new GsonBuilder().create();
        List<Employee> list = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            Employee emp = gson.fromJson(array.get(i).toString(), Employee.class);
            System.out.println(emp);
            list.add(emp);
        }

        return list;
    }

    private static String readString(String file) {
        StringBuilder json = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String s;
            while ((s = br.readLine()) != null) {
                json.append(s);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return json.toString();
    }
}
