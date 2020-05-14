package selenium.controller;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;
import selenium.bean.Allele;
import selenium.bean.Gene;
import selenium.crawler.GeneCrawler;
import selenium.dao.AlleleDao;
import selenium.dao.GeneDao;
import selenium.dao.LinkReferenceDao;
import selenium.util.ReferenceMemory;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControllerSimulationTest {
    private static String geneName;

    @BeforeClass
    public static void start(){
        geneName = "FBN1";
    }

    @Test
    public void ControllerSimulation() throws SQLException, ParserConfigurationException, SAXException, IOException, InterruptedException {
        //Conseguir info gene
        String id = GeneCrawler.getGeneId(geneName);
        Gene gene = GeneCrawler.getGeneInfo(id);
        System.out.println(gene.toString());
        GeneDao ed = new GeneDao();
        ed.storeGene(gene);

        //Esperar al sitio
        Thread.sleep(1500);

        //Conseguir info allele
        Allele allele = GeneCrawler.getAlleleInfo(id);
        allele.setIdGene(gene.getIdGene());
        GeneCrawler.getSequenceData(allele);
        System.out.println(allele.toString());
        AlleleDao ad = new AlleleDao();
        ad.storeAllele(allele);

        //Conseguir referencias
        ReferenceMemory refMemory = ReferenceMemory.getInstance();
        ArrayList<String> referencesAllele = GeneCrawler.getBibliographyLinks("tempFiles\\allele\\allele_"+id+".txt");
        for (int i = 0; i< 5; i++) refMemory.queueReference(referencesAllele.get(i));
        ArrayList<String> referencesSequence = GeneCrawler.getBibliographyLinks("tempFiles\\sequence\\sequence_"+allele.getGeneAccession()+".txt");
        for (String ref : referencesSequence) refMemory.queueReference(ref);
        refMemory.writeReferences();

        LinkReferenceDao linkReferenceDao = new LinkReferenceDao();

        //Relacion muchos a muchos
        for (int i = 0; i< 5; i++){
            linkReferenceDao.linkReference(refMemory.getReference(referencesAllele.get(i)),allele);
        }
        for (String ref : referencesSequence){
            //Relacionar alelo con referencias
            linkReferenceDao.linkReference(refMemory.getReference(ref), allele);
        }

    }

}
