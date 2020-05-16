package selenium.util;

import org.xml.sax.SAXException;
import selenium.bean.Reference;
import selenium.crawler.GeneCrawler;
import selenium.dao.ReferenceDao;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ReferenceMemory {
    private static ReferenceMemory instance = null;

    private HashMap<String, Reference> knownReferences;
    private ArrayList<String> newReferences;

    private ReferenceMemory(){
        knownReferences = new HashMap<>();
        newReferences = new ArrayList<>();
    }

    public static synchronized ReferenceMemory getInstance(){
        if (instance == null) instance = new ReferenceMemory();
        return instance;
    }

    public void queueReference (String ref){
        if (!knownReferences.containsKey(ref)){
            newReferences.add(ref);
            knownReferences.put(ref, new Reference());
        }
    }

    public void writeReferences() throws IOException, SAXException, ParserConfigurationException, SQLException {
        for (String link : newReferences) {
            Reference reference = GeneCrawler.getReferenceData(link);

            ReferenceDao refDao = new ReferenceDao();
            refDao.storeReference(reference);

            //System.out.println(reference);
            knownReferences.put(link,reference);
        }
        newReferences.clear();
    }

    public Reference getReference(String ref){
        if (knownReferences.containsKey(ref)) return knownReferences.get(ref);
        else return null;
    }

}
