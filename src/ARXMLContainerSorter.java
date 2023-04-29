import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ARXMLContainerSorter {
    public static void main(String[] args) {
        // check if the input filename is provided
        if (args.length < 1) {
            System.out.println("Usage: java ARXMLContainerSorter input_file");
            return;
        }

        String inputFilename = args[0];
        String outputFilename = inputFilename.replace(".arxml", "_mod.arxml");


        // check if the input file has a .arxml extension
        if (!inputFilename.endsWith(".arxml")) {
            try {
                throw new NotValidAutosarFileException("The input file is not a valid AUTOSAR file.");
            } catch (NotValidAutosarFileException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            // parse the input file
            File inputFile = new File(inputFilename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // get the list of container nodes and sort them by name
            NodeList containerNodes = doc.getElementsByTagName("CONTAINER");

            /* check if the file is empty */
            if(containerNodes.getLength() == 0){
                throw new EmptyAutosarFileException("The input file is empty.");
            }

            List<Element> containers = new ArrayList<Element>();
            for (int i = 0; i < containerNodes.getLength(); i++) {
                containers.add((Element) containerNodes.item(i));
            }
            Collections.sort(containers, (a, b) -> a.getElementsByTagName("SHORT-NAME").item(0).getTextContent()
                    .compareToIgnoreCase(b.getElementsByTagName("SHORT-NAME").item(0).getTextContent()));

            // create a new ARXML document with the sorted containers
            Document newDoc = dBuilder.newDocument();
            Element rootElement = newDoc.createElement("AUTOSAR");
            newDoc.appendChild(rootElement);
            for (Element container : containers) {
                rootElement.appendChild(newDoc.importNode(container, true));
            }

            // write the sorted containers to the output file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDoc);
            StreamResult result = new StreamResult(new File(outputFilename));
            transformer.transform(source, result);
            System.out.println("Sorted containers written to " + outputFilename);
        } catch (ParserConfigurationException | TransformerException | IOException | EmptyAutosarFileException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            System.out.println("Exception was called ,Error: Failed to parse input file " + inputFilename);
            e.printStackTrace();
        }
    }
}

class NotValidAutosarFileException extends Exception {
    public NotValidAutosarFileException(String message) {
        super(message);
    }
}
class EmptyAutosarFileException extends RuntimeException { public EmptyAutosarFileException(String message) {
        super(message);
    }
}