package selenium.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import javax.ws.rs.core.Response;
import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/gene")
public class Controller {
    @POST
    @Path("/load/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response addGene(@PathParam("id") String geneName) {
        geneName = geneName.toUpperCase();
        try{
            ArrayList<String> existingGenes = new GeneDao().geneNames();
            if (existingGenes.contains(geneName)) {
                JSONObject json = new JSONObject();
                json.put("message","genAlreadyExists");
                return Response.ok().entity(json, new Annotation[0])
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .allow("OPTIONS").build();
            }

            //Conseguir info gene
            String id = GeneCrawler.getGeneId(geneName);
            if (id.equals("genDoesntExist")) {
                JSONObject json = new JSONObject();
                json.put("message",id);
                return Response.ok().entity(json, new Annotation[0])
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .allow("OPTIONS").build();
            }
            Gene gene = GeneCrawler.getGeneInfo(id);
            GeneDao ed = new GeneDao();
            ed.storeGene(gene);

            //Esperar al sitio
            Thread.sleep(1500);

            //Conseguir info allele
            Allele allele = GeneCrawler.getAlleleInfo(id);
            allele.setIdGene(gene.getIdGene());
            //Esperar al sitio
            if (allele.getGeneAccession()!=null){
                Thread.sleep(1500);
                GeneCrawler.getSequenceData(allele);
            }
            AlleleDao ad = new AlleleDao();
            ad.storeAllele(allele);

            //Conseguir referencias
            ReferenceMemory refMemory = ReferenceMemory.getInstance();
            ArrayList<String> referencesAllele = GeneCrawler.getBibliographyLinks("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id="+id+"&report=sgml&retmode=xml");
            for (String ref : referencesAllele) refMemory.queueReference(ref);

            ArrayList<String> referencesSequence = new ArrayList<>();
            if (allele.getGeneAccession()!=null) {
                referencesSequence = GeneCrawler.getBibliographyLinks("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id=" + allele.getGeneAccession() + "&rettype=gb&retmode=xml");
                for (String ref : referencesSequence) refMemory.queueReference(ref);
            }
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

            JSONObject json = new JSONObject();
            json.put("message","Success");
            return Response.ok().entity(json, new Annotation[0])
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS").build();



        }catch (Exception ex){
            JSONObject json = new JSONObject();
            json.put("message",ex.getMessage());
            return Response.ok().entity(json, new Annotation[0])
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS").build();
        }
    }

    @POST
    @Path("/test/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response test(@PathParam("id") String geneName) {
        geneName = geneName.toUpperCase();
        try{
            ArrayList<String> existingGenes = new GeneDao().geneNames();
            if (existingGenes.contains(geneName)){
                JSONObject json = new JSONObject();
                json.put("message","genAlreadyExists");
                return Response.ok().entity(json, new Annotation[0])
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .allow("OPTIONS").build();
            }

            //Conseguir info gene
            String id = GeneCrawler.getGeneId(geneName);
            if (id.equals("genDoesntExist")){
                JSONObject json = new JSONObject();
                json.put("message",id);
                return Response.ok().entity(json, new Annotation[0])
                        .header("Access-Control-Allow-Origin", "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                        .allow("OPTIONS").build();
            }
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
            if (allele.getGeneAccession()!=null){
                Thread.sleep(1500);
                GeneCrawler.getSequenceData(allele);
            }
            AlleleDao ad = new AlleleDao();
            ad.storeAllele(allele);

            System.out.println(allele.toString());

            //Conseguir referencias
            ReferenceMemory refMemory = ReferenceMemory.getInstance();
            ArrayList<String> referencesAllele = GeneCrawler.getBibliographyLinks("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&id="+id+"&report=sgml&retmode=xml");
            for (int i = 0; i< 5; i++) refMemory.queueReference(referencesAllele.get(i));

            ArrayList<String> referencesSequence = new ArrayList<>();
            if (allele.getGeneAccession()!=null) {
                referencesSequence = GeneCrawler.getBibliographyLinks("https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&id=" + allele.getGeneAccession() + "&rettype=gb&retmode=xml");
                for (String ref : referencesSequence) refMemory.queueReference(ref);
            }
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

            JSONObject json = new JSONObject();
            json.put("message","Success");
            return Response.ok().entity(json, new Annotation[0])
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS").build();



        }catch (Exception ex){
            JSONObject json = new JSONObject();
            json.put("message",ex.getMessage());
            return Response.ok().entity(json, new Annotation[0])
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                    .allow("OPTIONS").build();
        }
    }

    @GET
    @Path("/check/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String testCon(@PathParam("id") String geneName) {
        return "HELLO THERE " + geneName.toUpperCase();
    }

    @GET
    @Path("/check2/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response testCon2(@PathParam("id") String geneName) {
        JSONObject json = new JSONObject();
        json.put("message", "HELLO THERE " + geneName.toUpperCase());
        return Response.ok().entity(json, new Annotation[0])
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();

        //return "HELLO THERE " + geneName.toUpperCase();
    }

    @POST
    @Path("/truncate")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response truncate() {
        String res;
        try {
            res = new Truncate().trunc();
        } catch (SQLException e) {
            e.printStackTrace();
            res = "Failed to connect";
        }
        JSONObject json = new JSONObject();
        json.put("message",res);
        return Response.ok().entity(json, new Annotation[0])
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();

    }

    //new GeneDao().geneNames()

    @GET
    @Path("/genes")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getAllNames() {
        List<String> res = new ArrayList<>();

        try {
            res =  new GeneDao().geneNames();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONArray jArray = new JSONArray();
        for (String temp : res){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gen", temp);
            jArray.add(jsonObject);
        }
        return Response.ok().entity(jArray, new Annotation[0])
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .allow("OPTIONS").build();
    }
}
