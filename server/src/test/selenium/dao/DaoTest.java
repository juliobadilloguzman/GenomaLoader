package selenium.dao;

import org.junit.Test;
import selenium.bean.Allele;
import selenium.bean.Gene;

import java.sql.SQLException;

public class DaoTest {
    @Test
    public void Daos_Should_insertAndGetID() throws SQLException {
        Gene gene = new Gene();
        gene.setId("6969");
        gene.setName("FKU2");
        gene.setNomenclatureName("Fukano2");
        gene.setSummary("This is a legit summary");
        gene.setChromosome("180");
        gene.setLocus("128t64.32");

        System.out.println(gene.toString());
        new GeneDao().storeGene(gene);
        System.out.println(gene.toString());

        Allele allele = new Allele();
        allele.setIdGene(gene.getIdGene());

        allele.setGeneAccession("NG_6969");
        allele.setSequenceStart("128128");
        allele.setSequenceEnd("646464");
        allele.setStrand("minus");
        allele.setSequence("tagtag");

        System.out.println(allele.toString());
        new AlleleDao().storeAllele(allele);
        System.out.println(allele.toString());


    }

}
