package selenium.crawler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import selenium.bean.Allele;
import selenium.bean.Gene;
import selenium.bean.Reference;
import selenium.util.AsyncStorage;
import selenium.util.XmlReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GeneCrawler {

    public static String getGeneId(String gene) throws IOException, ParserConfigurationException, SAXException {
        String result;

        //GUARDAR XML
        //XmlReader.downloadFile(
          //      "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=gene&term="+gene+"[Gene%20Name]+AND+%22Homo%20sapiens%22[Organism]",
          //      "tempFiles\\gene\\gene_"+gene+".txt"
        //);

        //ALMACENAMIENTO DURANTE LECTURA ASYNC
        HashMap<String,String> storage = AsyncStorage.getInstance().getStorage();
        storage.clear();

        //LECTOR DE ARCHIVO XML ASYNC
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler handler = new DefaultHandler(){
            Boolean accession = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.toUpperCase().equals("ID")){
                    accession = true;
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) {
                if (accession){
                    String temp = new String (ch, start, length).trim();
                    AsyncStorage.getInstance().getStorage().put("id",temp);
                    accession = false;
                }
            }
        };
        saxParser.parse("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=gene&term="+gene+"[Gene%20Name]+AND+%22Homo%20sapiens%22[Organism]",handler);

        //COPIAR CONTENIDO
        result = storage.get("id");

        //LIMPIAR
        storage.clear();

        return result;
    }

    public static Gene getGeneInfo(String id) throws IOException, ParserConfigurationException, SAXException {
        Gene gene = new Gene();
        gene.setId(id);

        //GUARDAR XML
        //XmlReader.downloadFile(
        //        "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=gene&id="+id+"&retmode=xml",
        //        "tempFiles\\gene_info\\gene_"+id+".txt"
        //);


        //ALMACENAMIENTO DURANTE LECTURA ASYNC
        HashMap<String,String> storage = AsyncStorage.getInstance().getStorage();
        storage.clear();

        //LECTOR DE ARCHIVO XML ASYNC
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler handler = new DefaultHandler(){
            Boolean accession = false;
            String tag;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes){
                tag = qName;
                if (qName.equals("Name") || qName.equals("NomenclatureName") || qName.equals("Chromosome") || qName.equals("MapLocation") || qName.equals("Summary")) accession = true;
            }

            @Override
            public void characters(char[] ch, int start, int length){
                if (accession){
                    String temp = new String (ch, start, length).trim();
                    AsyncStorage.getInstance().getStorage().put(tag,temp);
                    accession = false;
                }
            }
        };
        saxParser.parse("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=gene&id="+id+"&retmode=xml",handler);

        //COPIAR CONTENIDO A OBJETO
        gene.setName(storage.get("Name"));
        gene.setNomenclatureName(storage.get("NomenclatureName"));
        gene.setChromosome(storage.get("Chromosome"));
        gene.setLocus(storage.get("MapLocation"));
        gene.setSummary(storage.get("Summary"));

        //LIMPIAR
        storage.clear();

        return gene;
    }


    public static Allele getAlleleInfo(String id) throws ParserConfigurationException, SAXException, IOException {
        Allele allele = new Allele();

        //GUARDAR XML
        //XmlReader.downloadFile(
        //        "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id="+id+"&report=sgml&retmode=xml",
        //        "tempFiles\\allele\\allele_"+id+".txt"
        //);

        //ALMACENAMIENTO DURANTE LECTURA ASYNC
        HashMap<String,String> storage = AsyncStorage.getInstance().getStorage();
        storage.clear();

        //LECTOR DE ARCHIVO XML ASYNC
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler handler = new DefaultHandler(){
            Boolean accession = false;
            String tag = "";

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                tag = qName;
                if (qName.toUpperCase().equals("GENE-COMMENTARY_ACCESSION")) accession = true;
                else if (qName.equals("Seq-id_gi")) accession = false;
                else if (qName.equals("Na-strand") && accession){
                    AsyncStorage.getInstance().getStorage().put("Na-strand",attributes.getValue(0));
                }
            }

            @Override
            public void characters(char[] ch, int start, int length){
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
        saxParser.parse("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id="+id+"&report=sgml&retmode=xml",handler);

        //COPIAR CONTENIDO A OBJETO
        allele.setGeneAccession(storage.get("Gene-commentary_accession"));
        allele.setSequenceStart(storage.get("Seq-interval_to"));
        allele.setSequenceEnd(storage.get("Seq-interval_from"));
        allele.setStrand(storage.get("Na-strand"));

        //LIMPIAR
        storage.clear();
        return allele;
    }

    public static ArrayList<String> getBibliographyLinks(String fileName) throws ParserConfigurationException, SAXException, IOException {

        //ALMACENAMIENTO DURANTE LECTURA ASYNC
        HashMap<String,String> storage = AsyncStorage.getInstance().getStorage();
        storage.clear();

        //LECTOR DE ARCHIVO XML ASYNC
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler handler = new DefaultHandler(){
            Boolean accession = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equals("PubMedId")){
                    accession = true;
                }
            }

            @Override
            public void characters(char[] ch, int start, int length) {
                if (accession){
                    String temp = new String (ch, start, length).trim();
                    AsyncStorage.getInstance().getStorage().put(temp,temp);
                    accession = false;
                }
            }
        };
        saxParser.parse(fileName,handler);

        //COPIAR CONTENIDO
        ArrayList<String> results = new ArrayList<>(storage.keySet());

        //LIMPIAR
        storage.clear();
        return results;
    }

    public static Reference getReferenceData(String id) throws ParserConfigurationException, SAXException, IOException {
        Reference reference = new Reference();

        //GUARDAR XML
        //XmlReader.downloadFile(
        //        "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id="+id,
        //        "tempFiles\\ref\\ref_"+id+".txt"
        //);

        //ALMACENAMIENTO DURANTE LECTURA ASYNC
        HashMap<String,String> storage = AsyncStorage.getInstance().getStorage();
        storage.clear();

        //LECTOR DE ARCHIVO XML ASYNC
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler handler = new DefaultHandler(){
            Boolean accession = false;
            String tag, lastName;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                tag = qName;
                if (qName.equals("Title") || qName.equals("ArticleTitle") || qName.equals("AbstractText") || qName.equals("LastName") || qName.equals("Initials")) accession = true;
            }

            @Override
            public void characters(char[] ch, int start, int length) {
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
        saxParser.parse("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id="+id,handler);

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

    public static void getSequenceData(Allele allele) throws ParserConfigurationException, SAXException, IOException {
        //GUARDAR XML
        //XmlReader.downloadFile(
        //        "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id="+allele.getGeneAccession()+"&rettype=gb&retmode=xml",
        //        "tempFiles\\sequence\\sequence_"+allele.getGeneAccession()+".txt"
        //);

        //ALMACENAMIENTO DURANTE LECTURA ASYNC
        HashMap<String,String> storage = AsyncStorage.getInstance().getStorage();
        storage.clear();

        //LECTOR DE ARCHIVO XML ASYNC
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        DefaultHandler handler = new DefaultHandler(){
            Boolean accession = false;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equals("GBSeq_sequence")) accession = true;
            }

            @Override
            public void characters(char[] ch, int start, int length){
                if (accession){
                    String temp = new String (ch, start, length).trim();
                    AsyncStorage.getInstance().getStorage().put("sequence",temp);
                    accession = false;
                }
            }
        };
        saxParser.parse("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id="+allele.getGeneAccession()+"&rettype=gb&retmode=xml",handler);

        //COPIAR DATOS
        allele.setSequence(storage.get("sequence"));

        //LIMPIAR
        storage.clear();
    }


}
