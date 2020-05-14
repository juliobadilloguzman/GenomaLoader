package selenium.controller;

import selenium.bean.Allele;
import selenium.bean.Gene;
import selenium.crawler.GeneCrawler;
import selenium.dao.AlleleDao;
import selenium.dao.GeneDao;
import selenium.util.ReferenceMemory;

import javax.jws.WebParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/gene")
public class Controller {
    @POST
    @Path("/load/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String addGene(@PathParam("id") String geneName) {
        try{

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
            GeneCrawler.getSequenceData(allele);
            System.out.println(allele.toString());
            AlleleDao ad = new AlleleDao();
            ad.storeAllele(allele);

            //Conseguir referencias
            ReferenceMemory refMemory = ReferenceMemory.getInstance();
            ArrayList<String> referencesAllele = GeneCrawler.getBibliographyLinks("tempFiles\\allele\\allele_"+id+".txt");
            for (String ref : referencesAllele) refMemory.queueReference(ref);
            ArrayList<String> referencesSequence = GeneCrawler.getBibliographyLinks("tempFiles\\sequence\\sequence_"+allele.getGeneAccession()+".txt");
            for (String ref : referencesSequence) refMemory.queueReference(ref);
            refMemory.writeReferences();

            //Relacion muchos a muchos
            for (String ref : referencesAllele){
                //Relacionar alelo con referencias
                //allele.getIdAllele();
                //refMemory.getReference(ref).getIdReference();
            }
            for (String ref : referencesSequence){
                //Relacionar alelo con referencias
                //allele.getIdAllele();
                //refMemory.getReference(ref).getIdReference();
            }

            return "Success";

        }catch (Exception ex){
            return ex.getMessage();
        }
    }

    @GET
    @Path("/test/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String test(@PathParam("id") String geneName) {
        return geneName;
    }

}
