package simulation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * @author Julia Saveliff, Yunhao Qing, Haotian Wang
 */
public class ReadXML {
    private String name;
    private int row;
    private int column;
    private int[][] cellState;
    private List<Double> extraParameters;
    public final static Random rand = new Random();
    Document document;
    public final static String XMLFileOpenException = "The system is unable to open the file, the file may be damaged." +
            "Please select a valid XML file";
    public final static String XMLFileSimException = "Invalid or no simulation type given.";
    public final static String XMLFileGridException = "Grid Configuration not given or incorrectly formatted.";
    public final static String XMLFileCellStateException = "The cell configuration in the XML is missing or " +
            "incorrect or not supported at this point of time.";
    public final static String XMLFileParaException = "The extra parameters in the XML files are missing or incorrectly" +
            "formatted.";



    /**
     * Constructor for ReadXML with the file, the xml file.
     * It reads the type of simulation, grid and initial states configuration
     * and extra parameters for each specific simulation.
     */
    public ReadXML (File file) throws Exception {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
        } catch (Exception e ) {
            throw new Exception(XMLFileOpenException);
        }
        try {
            this.name = returnString("name");
        }
        catch (Exception e ) {
            throw new Exception(XMLFileSimException);
        }
        this.extraParameters = new ArrayList<>();
        readGrid();
        readState();
        readExtraParameters();
    }
    
    /**
     * Read in initial state for each cell and update the 2D array cellState.
     */
    private void readState() throws Exception {
        try {
            NodeList typeList = document.getElementsByTagName("cellState");
            String dataType = typeList.item(0).getAttributes().getNamedItem("dataType").getNodeValue();
            if (dataType.equals("list")) {
                readStateList();
            } else if (dataType.equals("ratio")) {
                readStateRatio();
            } else if (dataType.equals("random")) {
                readStateRandom();
            }
            System.out.print(cellState[0].toString());
        }
        catch (Exception e){
            throw new Exception(XMLFileCellStateException);
        }

    }

    private void readStateList() {
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

    private void readStateRatio() {
        NodeList nodeList = document.getElementsByTagName("state");
        double[] myStateRatio = new double[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            int stateNumber = Integer.parseInt(node.getAttributes().getNamedItem("stateNumber").getNodeValue());
            String temp = node.getTextContent();
            double stateRatio = Double.parseDouble(temp);
            if (stateNumber == 0){
                myStateRatio[stateNumber] = stateRatio;
            }
            else{
                myStateRatio[stateNumber] = stateRatio + myStateRatio[stateNumber - 1];
            }
        }

        for (int i = 0 ; i < row; i++){
            for (int j = 0; j < column; j++){
                double randNum  = rand.nextDouble();
                for (int k = 0; k < myStateRatio.length; k++){
                    if (randNum <= myStateRatio[k]){
                        cellState[i][j] = k;
                        break;
                    }
                }
            }
        }
    }

    private void readStateRandom() {
        NodeList nodeList = document.getElementsByTagName("state");
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                cellState[i][j] = rand.nextInt(nodeList.getLength());
            }
        }
    }

    /**
     * Return the extra parameters specified in the XML file if there is any.
     */
    private void readExtraParameters() throws Exception{
        try {
            String parameters = returnString("extraParameters");
            if (!parameters.equals("")) {
                String[] parameterList = parameters.split(" ");
                for (String para : parameterList) {
                    this.extraParameters.add(returnDouble(para));
                }
            }
        }
        catch (Exception e){
            throw new Exception(XMLFileParaException);
        }
    }
    /**
     * Read in the grid configuration and initialise the 2D array cellState.
     */

    private void readGrid() throws Exception{
        try{
        // width = returnInt("width");
        // height = returnInt("height");
        row = returnInt("row");
        column = returnInt("col");
        cellState = new int[row][column];}
        catch (Exception e){
            throw new Exception(XMLFileGridException);
        }
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


    public int[][] getCellState(){return cellState;}
}


