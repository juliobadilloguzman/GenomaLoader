package selenium.crawler;

import org.junit.Test;

import org.xml.sax.SAXException;
import selenium.bean.Allele;
import selenium.bean.Gene;
import selenium.bean.Reference;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class GeneCrawlerTest {

    @Test
    public void basicCrawl() throws InterruptedException, IOException, SAXException, ParserConfigurationException {
        String id = GeneCrawler.getGeneId("FBN1");

        assertThat("ID did not match",
                id,
                is(equalTo("2200"))
        );

        Gene gene = GeneCrawler.getGeneInfo(id);
        System.out.println(gene.toString());

        Thread.sleep(1500);

        Allele allele = GeneCrawler.getAlleleInfo(id);
        GeneCrawler.getSequenceData(allele);

        System.out.println(allele.toString());

        ArrayList<String> references = GeneCrawler.getBibliographyLinks("tempFiles\\allele\\allele_"+id+".txt");
        Reference ref = GeneCrawler.getReferenceData(references.get(0));

        System.out.println(references);
        System.out.println(ref);

        //references = GeneCrawler.getBibliographyLinks("tempFiles\sequence\sequence_"+allele.getGeneAccession()+".txt");
        //ref = GeneCrawler.getReferenceData(references.get(0));


        assertThat("id did not match",
                gene.getId(),
                is(equalTo(id))
        );

        assertThat("name did not match",
                gene.getName(),
                is(equalTo("FBN1"))
        );

        assertThat("nomenclature name did not match",
                gene.getNomenclatureName(),
                is(equalTo("fibrillin 1"))
        );

        assertThat("chromosome did not match",
                gene.getChromosome(),
                is(equalTo("15"))
        );

        assertThat("locus did not match",
                gene.getLocus(),
                is(equalTo("15q21.1"))
        );

        assertThat("Summary was empty",
                gene.getSummary(),
                is(not(equalTo("")))
        );

        assertThat("Reference count did not match expected",
                references.size(),
                is(equalTo(405))
        );

    }
}
