package selenium.crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import selenium.bean.Allele;
import selenium.bean.Gene;
import selenium.bean.Reference;
import selenium.util.AsyncStorage;
import selenium.util.drivers.EnhancedWebDriver;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeneCrawler {

    private static ArrayList<String> splitLine(String line){
        ArrayList<String> result = new ArrayList<>();

        //VALIDAR QUE CONTENGA TEXTO
        line = line.substring(line.indexOf(">")+1);
        if (!line.equals("")){

            //EXTRAER INFORMACION
            String node, tag;
            Integer pos = line.indexOf("<");
            node = line.substring(0,pos);
            tag = line.substring(pos+2);
            tag = tag.substring(0,tag.length()-1);

            result.add(tag);
            result.add(node);
        }

        return result;
    }

    private static ArrayList<String> splitCollapsable(WebElement collapsable){
        ArrayList<String> result = new ArrayList<>();

        WebElement parent = collapsable.findElement(By.xpath("./.."));
        if (parent != null) {
            String line = parent.getText();

            //VALIDAR QUE CONTENGA TEXTO
            line = line.substring(line.indexOf("\n") + 1);
            if (!line.equals("")) {

                //EXTRAER INFORMACION
                String node, tag;
                Integer pos = line.indexOf("\n");
                node = line.substring(0, pos);
                tag = line.substring(pos + 3);
                tag = tag.substring(0, tag.length() - 1);

                result.add(tag);
                result.add(node);
            }
        }
        return result;
    }


    public static String getGeneId(EnhancedWebDriver myDriver, String gene) throws InterruptedException {

        //IR A LA PAGINA
        myDriver.get("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=gene&term="+gene+"[Gene%20Name]+AND+%22Homo%20sapiens%22[Organism]");

        //CONSEGUIR CADA LINEA
        List<WebElement> lines = myDriver.findDynamicElements(By.className("line"));
        if (lines != null) {
            for (WebElement line : lines){

                //VALIDAR QUE CONTENGA TEXTO
                ArrayList<String> tagAndNode = splitLine(line.getText());
                if (tagAndNode.size()==2) {
                    //DEVOLVER ID
                    if (tagAndNode.get(0).toUpperCase().equals("ID")) {
                        return tagAndNode.get(1);
                    }
                }
            }
        }

        return "";

    }

    public static Gene getGeneInfo(EnhancedWebDriver myDriver, String id) throws InterruptedException {
        ArrayList<String> result = new ArrayList<>();
        Gene gene = new Gene();
        gene.setId(id);

        //IR A LA PAGINA
        myDriver.get("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=gene&id="+id+"&retmode=xml");

        //CONSEGUIR CADA LINEA
        List<WebElement> lines = myDriver.findDynamicElements(By.className("line"));
        if (lines != null) {
            for (WebElement line : lines) {

                //VALIDAR QUE CONTENGA TEXTO
                ArrayList<String> tagAndNode = splitLine(line.getText());
                if (tagAndNode.size() == 2) {
                    //MOSTRAR DATOS
                    switch (tagAndNode.get(0).toUpperCase()) {
                        case "NAME":
                            result.add(tagAndNode.get(1));
                            gene.setName(tagAndNode.get(1));
                            break;
                        case "NOMENCLATURENAME":
                            result.add(tagAndNode.get(1));
                            gene.setNomenclatureName(tagAndNode.get(1));
                            break;
                        case "CHROMOSOME":
                            result.add(tagAndNode.get(1));
                            gene.setChromosome(tagAndNode.get(1));
                            break;
                        case "MAPLOCATION":
                            result.add(tagAndNode.get(1));
                            gene.setLocus(tagAndNode.get(1));
                            break;
                    }
                } else {
                    if (line.getText().equals("<Summary>")) {
                        //VALIDAR QUE CONTENGA TEXTO
                        tagAndNode = splitCollapsable(line);
                        if (tagAndNode.size() == 2) {
                            //MOSTRAR DATOS
                            if (tagAndNode.get(0).toUpperCase().equals("SUMMARY")) {
                                result.add(tagAndNode.get(1));
                                gene.setSummary(tagAndNode.get(1));
                            }
                        }
                    }
                }

                if (result.size() == 5) break;
            }
        }
        return gene;
    }


    public static Allele getAlleleInfo(EnhancedWebDriver myDriver, String id) throws InterruptedException, ParserConfigurationException, SAXException, IOException {
        Allele allele = new Allele();

        //IR A LA PAGINA
        myDriver.get("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id="+id+"&report=sgml&retmode=xml");
        //GUARDAR XML
        myDriver.printSourcePage("allele_"+id+".txt");

        //ALMACENAMIENTO DURANTE LECTURA ASYNC
        HashMap<String,String> storage = AsyncStorage.getInstance().getStorage();
        storage.clear();

        //LECTOR DE ARCHIVO XML ASYNC
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler handler = new DefaultHandler(){
            Boolean accession = false;
            String tag = "";

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                tag = qName;
                if (qName.toUpperCase().equals("GENE-COMMENTARY_ACCESSION")) accession = true;
                else if (qName.equals("Seq-id_gi")) accession = false;
                else if (qName.equals("Na-strand") && accession){
                    AsyncStorage.getInstance().getStorage().put("Na-strand",attributes.getValue(0));
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException { }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (accession){
                    String temp = new String (ch, start, length).trim();
                    if (tag.equals("Gene-commentary_accession") && !temp.equals("") && !temp.contains("NG_")){
                        accession = false;
                    }

                    if (!temp.equals("") && accession){
                        if (tag.equals("Gene-commentary_accession") || tag.equals("Seq-interval_from") || tag.equals("Seq-interval_to")) {
                            AsyncStorage.getInstance().getStorage().put(tag, temp);
                        }
                    }
                }
            }
        };
        saxParser.parse("allele_"+id+".txt",handler);

        //COPIAR CONTENIDO A OBJETO
        allele.setGeneAccession(storage.get("Gene-commentary_accession"));
        allele.setSequenceStart(storage.get("Seq-interval_to"));
        allele.setSequenceEnd(storage.get("Seq-interval_from"));
        allele.setStrand(storage.get("Na-strand"));

        //LIMPIAR
        storage.clear();
        return allele;
    }

    public static ArrayList<String> getAlleleBibliography(EnhancedWebDriver myDriver, String id) throws InterruptedException, ParserConfigurationException, SAXException, IOException {

        //ALMACENAMIENTO DURANTE LECTURA ASYNC
        HashMap<String,String> storage = AsyncStorage.getInstance().getStorage();
        storage.clear();

        //LECTOR DE ARCHIVO XML ASYNC
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler handler = new DefaultHandler(){
            Boolean accession = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if (qName.equals("PubMedId")){
                    accession = true;
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException { }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (accession){
                    String temp = new String (ch, start, length).trim();
                    AsyncStorage.getInstance().getStorage().put(temp,temp);
                    accession = false;
                }
            }
        };
        saxParser.parse("allele_"+id+".txt",handler);

        //COPIAR CONTENIDO
        ArrayList<String> results = new ArrayList<>(storage.keySet());

        //LIMPIAR
        storage.clear();
        return results;
    }

    public static Reference getReferenceData(EnhancedWebDriver myDriver, String id) throws InterruptedException, ParserConfigurationException, SAXException, IOException {
        Reference reference = new Reference();

        //IR A LA PAGINA
        myDriver.get("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id="+id);
        //GUARDAR XML
        myDriver.printSourcePage("ref_"+id+".txt");

        //ALMACENAMIENTO DURANTE LECTURA ASYNC
        HashMap<String,String> storage = AsyncStorage.getInstance().getStorage();
        storage.clear();

        //LECTOR DE ARCHIVO XML ASYNC
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler handler = new DefaultHandler(){
            Boolean accession = false;
            String tag, lastName;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                tag = qName;
                if (qName.equals("Title") || qName.equals("ArticleTitle") || qName.equals("AbstractText") || qName.equals("LastName") || qName.equals("Initials")) accession = true;
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException { }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                if (accession){
                    String temp = new String (ch, start, length).trim();

                    if (tag.equals("LastName")) lastName = temp;
                    else if (tag.equals("Initials")){
                        if (AsyncStorage.getInstance().getStorage().containsKey("authors")){
                            temp = AsyncStorage.getInstance().getStorage().get("authors") + ", " + lastName + " " +  temp + ".";
                        } else {
                            temp = lastName + " " +  temp + ".";
                        }
                        AsyncStorage.getInstance().getStorage().put("authors", temp);
                    }
                    else{
                        AsyncStorage.getInstance().getStorage().put(tag,temp);
                    }
                    accession = false;
                }
            }
        };
        saxParser.parse("ref_"+id+".txt",handler);

        //COPIAR CONTENIDO A OBJETO
        reference.setId(id);
        reference.setArticleAbstract(storage.get("AbstractText"));
        reference.setAuthors(storage.get("authors"));
        reference.setPublicationTitle(storage.get("Title"));
        reference.setTitle(storage.get("ArticleTitle"));

        //LIMPIAR
        storage.clear();

        return reference;
    }


}
