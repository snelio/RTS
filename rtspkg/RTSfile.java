package rtspkg;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import static rtspkg.RTSdebug.showArray;

/*
 *
 *
 */

public class RTSfile {

    /*
     * Get first element of given name
     */
    static private Node findFirstNamedElement(Node parent,String tagName)
    {
        NodeList children = parent.getChildNodes();

        for (int i = 0, in = children.getLength() ; i < in ; i++) {
            Node child = children.item(i);

            if (child.getNodeType() != Node.ELEMENT_NODE)
                continue;

            if (child.getNodeName().equals(tagName)) 
                return child;
        }

        return null;
    }

    /*
     * Get data
     */
    static private String getCharacterData(Node parent)
    {
        StringBuilder text = new StringBuilder();

        if ( parent == null )
            return text.toString();

        NodeList children = parent.getChildNodes();

        for (int k = 0, kn = children.getLength() ; k < kn ; k++) {
            Node child = children.item(k);

            if (child.getNodeType() != Node.TEXT_NODE)
                break;

            text.append(child.getNodeValue());
        }

        return text.toString();
    }    

    /*
     * Load text for each menu screen
     */
    static public String[][] loadXMLcountries (Boolean debugMode){

        // Declarations and instances
        String FILE_XML_COUNTRIES = null;   // countries configuration file
        String[][] country_array = null;    // countries array to be created
        RTSxmlMap xmlmap = new RTSxmlMap(new File("./config/config.xml")); // fixed path for main XML config file

        // Get main XML config file for all countries
        try { 
            //FILE_XML_COUNTRIES = RTSxmlMap.getXmlCountriesFile();
            FILE_XML_COUNTRIES = xmlmap.lookupSymbol("countries_XML");
        } catch (Exception e) {     
            System.out.println("Error with loading XML file:");
            System.err.println(e.getMessage());       
        }
        
        // Get countries information from all XML files
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // XML file to read
            Document document = builder.parse(FILE_XML_COUNTRIES);
            Element catalog = document.getDocumentElement();
            NodeList countries = catalog.getElementsByTagName("country");

            country_array = new String[countries.getLength()][3];

            for (int i = 0, ii = 0, n = countries.getLength() ; i < n ; i++) {
                Node child = countries.item(i);

                Element country = (Element)child;
                ii++;

                String c_id = country.getAttribute("c_id");
                String name_eng = getCharacterData(findFirstNamedElement(child, "name_eng"));
                String name = getCharacterData(findFirstNamedElement(child, "name"));
                String population = getCharacterData(findFirstNamedElement(child, "population"));
                String code = getCharacterData(findFirstNamedElement(child, "code"));
                String file = getCharacterData(findFirstNamedElement(child, "file"));

                System.out.printf("""
                                %3d. country code = %s
                                name_eng: %s
                                name: %s
                                population: %s
                                code: %s
                                file: %s
                                """,
                    ii, c_id, name_eng, name, population, code, file);

                
                country_array[ii-1][0] = code;
                country_array[ii-1][1] = file;
                country_array[ii-1][2] = population;

            }   
            
        } catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println("Error with parsing XML file:");
            System.err.println(e.getMessage());       
        }

        //DEBUG_MODE: print data read from XML
        if (debugMode) {
            System.out.println("<DEBUG_MODE=true LogSrc=RTSfile>");
            System.out.println();
            //showArray2Dstr(country_array);
            showArray(country_array);
        }

        return country_array;
        
    };

    /*
     * Load text for each menu screen
     */
    static public String[][] loadCountryPlates (String code, String xmlfile, Boolean debugMode){

        String[][] county_array = null;

        // Get countries information from all XML files
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // XML file to read
            Document document = builder.parse(xmlfile);
            Element catalog = document.getDocumentElement();
            NodeList counties = catalog.getElementsByTagName("county");

            county_array = new String[counties.getLength()][4];
           

            for (int i = 0, ii = 0, n = counties.getLength() ; i < n ; i++) {
                Node child = counties.item(i);

                //Element county = (Element)child;
                ii++;

                String prefix = getCharacterData(findFirstNamedElement(child, "prefix"));
                String population = getCharacterData(findFirstNamedElement(child, "population"));

                county_array[ii-1][0] = code;
                county_array[ii-1][1] = prefix;
                county_array[ii-1][2] = population;
                county_array[ii-1][3] = "0";
            }   
            
        } catch(IOException | ParserConfigurationException | SAXException e) {
            System.out.println("Error with parsing XML file:");
            System.err.println(e.getMessage());       
        } 
        
        //DEBUG_MODE: print data loaded into county_array
        if (debugMode) {
            System.out.println("<DEBUG_MODE=true LogSrc=RTSfile> county_array:");
            //showArray2Dstr(county_array);
            showArray(county_array);
        }
        return county_array;

    };

    /*
     * Load text for each menu screen
     */
    static public void appendToFile (String[][] array) {

    };
    

}