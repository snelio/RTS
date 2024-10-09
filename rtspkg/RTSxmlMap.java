package rtspkg;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/*
 *
 *
 */

public class RTSxmlMap {

    private Properties xmlmap;

    public RTSxmlMap( File file ) {
        xmlmap = new Properties();

        try {
            // Populate the symbol map from the XML file
            xmlmap.loadFromXML( file.toURI().toURL().openStream() );
        }
        catch ( IOException e ) {
            System.err.println(e.getMessage());
            System.out.println("Error with loading XML file " + file.toString());
        }
    }

    // Get the value of given conf_id
    public String lookupSymbol(String symbol) {
        String content = xmlmap.getProperty(symbol);
        if( content == null ) return "";
        return String.format(content);
    }

}