package Helpers;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings {
    private final HashMap<String, String> properties = new HashMap<>();
    public final List<String> tableClasses = new ArrayList<>();
    private static final String props_path = "src/main/resources/META-INF/persistence.xml";

    public Settings() {
        try {
            getDataFromXml();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        return this.properties.get(key);
    }

    public void getDataFromXml() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dBfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dBfactory.newDocumentBuilder();
        Document document = builder.parse(new File(props_path));
        NodeList propertyList = document.getElementsByTagName("property");
        for (int i = 0; i < propertyList.getLength(); i++)
        {
            Node node = propertyList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                properties.put(element.getAttribute("name"), element.getTextContent());
            }
        }
        NodeList classList = document.getElementsByTagName("class");
        for (int i = 0; i < classList.getLength(); i++)
        {
            Node node = classList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;
                tableClasses.add(element.getTextContent().trim());
            }
        }
    }
}
