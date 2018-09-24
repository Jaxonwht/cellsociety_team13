package simulation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author Julia Saveliff, Yunhao Qing, Haotian Wang
 */
public class ReadXML {
    private String name;
    private int row;
    private int column;
    private int width;
    private int height;
    private int[][] cellState;
    private List<Double> extraParameters;
    Document document;
    
    /**
     * Constructor for ReadXML with the file, the xml file.
     * It reads the type of simulation, grid and initial states configuration
     * and extra parameters for each specific simulation.
     */
    public ReadXML (File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(file);
        document.getDocumentElement().normalize();
        this.name = returnString("name");
        this.extraParameters = new ArrayList<>();
        readGrid();
        readState();
        readExtraParameters();
    }
    
    /**
     * Read in initial state for each cell and update the 2D array cellState.
     */
    private void readState() {
        NodeList nodeList = document.getElementsByTagName("state");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            int stateNumber = Integer.parseInt(node.getAttributes().getNamedItem("stateNumber").getNodeValue());
            String temp = node.getTextContent();
            for (String s : temp.split(" ")){
                int index = Integer.parseInt(s);
                cellState[index/column][index%column] = stateNumber;
            }
        }
    }

    /**
     * Return the extra parameters specified in the XML file if there is any.
     */
    private void readExtraParameters() {
        String parameters = returnString("extraParameters");
        if (!parameters.equals("")) {
            String[] parameterList = parameters.split(" ");
            for (String para : parameterList) {
                this.extraParameters.add(returnDouble(para));
            }
        }
    }
    /**
     * Read in the grid configuration and initialise the 2D array cellState.
     */

    private void readGrid(){
        width = returnInt("width");
        height = returnInt("height");
        row = returnInt("row");
        column = returnInt("col");
        cellState = new int[row][column];
    }
    
    private int returnInt(String tag) {
        return Integer.parseInt(returnString(tag));
    }
    
    private double returnDouble(String tag) {
        return Double.parseDouble(returnString(tag));
    }

    private String returnString(String tag) {
        return document.getElementsByTagName(tag).item(0).getTextContent();
    }

    public List<Double> getExtraParameters() {
        return this.extraParameters;
    }

    public String getName(){return name;}

    public int getRow(){return row;}

    public int getColumn(){return column;}

    public int getWidth(){return width;}

    public int getHeight(){return height;}

    public int[][] getCellState(){return cellState;}
}


