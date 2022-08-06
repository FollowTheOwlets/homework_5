import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.metadata.IIOMetadataNode;
import javax.swing.plaf.IconUIResource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.xml";
        List<Employee> list = parseXML(fileName);

        String json = listToJson(list);

        try (FileWriter file = new FileWriter("data.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<Employee> parseXML(String name) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(name));
        List<Employee> data = new ArrayList<>();

        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                NodeList nodeList_ =node_.getChildNodes();
                Employee emp = new Employee(
                        Integer.parseInt(nodeList_.item(1).getTextContent()),
                        nodeList_.item(3).getTextContent(),
                        nodeList_.item(5).getTextContent(),
                        nodeList_.item(7).getTextContent(),
                        Integer.parseInt(nodeList_.item(9).getTextContent())
                );
                data.add(emp);
            }
        }
        return data;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type listType = new TypeToken<ArrayList<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> data = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader).withMappingStrategy(strategy).build();
            data = csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
