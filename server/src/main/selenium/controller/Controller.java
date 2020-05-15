package selenium.controller;

import selenium.bean.Allele;
import selenium.bean.Gene;
import selenium.crawler.GeneCrawler;
import selenium.dao.AlleleDao;
import selenium.dao.GeneDao;
import selenium.dao.LinkReferenceDao;
import selenium.dao.Truncate;
import selenium.util.ReferenceMemory;

import javax.jws.WebParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;

@Path("/gene")
public class Controller {
    @POST
    @Path("/load/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String addGene(@PathParam("id") String geneName) {
        geneName = geneName.toUpperCase();
        try{
            //Conseguir info gene
            String id = GeneCrawler.getGeneId(geneName);
            if (id.equals("genDoesntExist")) return id;
            Gene gene = GeneCrawler.getGeneInfo(id);
            GeneDao ed = new GeneDao();
            ed.storeGene(gene);

            //Esperar al sitio
            Thread.sleep(1500);

            //Conseguir info allele
            Allele allele = GeneCrawler.getAlleleInfo(id);
            allele.setIdGene(gene.getIdGene());
            //Esperar al sitio
            Thread.sleep(1500);
            GeneCrawler.getSequenceData(allele);
            AlleleDao ad = new AlleleDao();
            ad.storeAllele(allele);

            //Conseguir referencias
            ReferenceMemory refMemory = ReferenceMemory.getInstance();
            ArrayList<String> referencesAllele = GeneCrawler.getBibliographyLinks("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id="+id+"&report=sgml&retmode=xml");
            for (String ref : referencesAllele) refMemory.queueReference(ref);
            ArrayList<String> referencesSequence = GeneCrawler.getBibliographyLinks("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id="+allele.getGeneAccession()+"&rettype=gb&retmode=xml");
            for (String ref : referencesSequence) refMemory.queueReference(ref);
            refMemory.writeReferences();

            LinkReferenceDao linkReferenceDao = new LinkReferenceDao();

            //Relacion muchos a muchos
            for (String ref : referencesAllele){
                //Relacionar alelo con referencias
                linkReferenceDao.linkReference(refMemory.getReference(ref), allele);

            }
            for (String ref : referencesSequence){
                //Relacionar alelo con referencias
                linkReferenceDao.linkReference(refMemory.getReference(ref), allele);
            }

            return "Success";

        }catch (Exception ex){
            return ex.getMessage();
        }
    }

    @POST
    @Path("/test/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String test(@PathParam("id") String geneName) {
        geneName = geneName.toUpperCase();
        try{
            //Conseguir info gene
            String id = GeneCrawler.getGeneId(geneName);
            if (id.equals("genDoesntExist")) return id;
            Gene gene = GeneCrawler.getGeneInfo(id);
            GeneDao ed = new GeneDao();
            ed.storeGene(gene);
            System.out.println(gene.toString());

            //Esperar al sitio
            Thread.sleep(1500);

            //Conseguir info allele
            Allele allele = GeneCrawler.getAlleleInfo(id);
            allele.setIdGene(gene.getIdGene());
            System.out.println(allele.toString());
            //Esperar al sitio
            Thread.sleep(1500);
            GeneCrawler.getSequenceData(allele);
            AlleleDao ad = new AlleleDao();
            ad.storeAllele(allele);

            System.out.println(allele.toString());

            //Conseguir referencias
            ReferenceMemory refMemory = ReferenceMemory.getInstance();
            ArrayList<String> referencesAllele = GeneCrawler.getBibliographyLinks("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id="+id+"&report=sgml&retmode=xml");
            for (int i = 0; i< 5; i++) refMemory.queueReference(referencesAllele.get(i));
            ArrayList<String> referencesSequence = GeneCrawler.getBibliographyLinks("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id="+allele.getGeneAccession()+"&rettype=gb&retmode=xml");
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

            return "Success";

        }catch (Exception ex){
            return ex.getMessage();
        }
    }

    @GET
    @Path("/check/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String testCon(@PathParam("id") String geneName) {
        return "HELLO THERE " + geneName.toUpperCase();
    }

    @DELETE
    @Path("/truncate")
    @Produces(MediaType.APPLICATION_JSON)
    public String truncate() {
        try {
            return new Truncate().trunc();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Failed to connect";
    }

}
