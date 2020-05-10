package selenium.crawler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.xml.sax.SAXException;
import selenium.bean.Allele;
import selenium.bean.Gene;
import selenium.crawler.GeneCrawler;
import selenium.util.drivers.BrowserDriver;
import selenium.util.drivers.BrowserOption;
import selenium.util.drivers.EnhancedWebDriver;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class GeneCrawlerTest {
    private static EnhancedWebDriver driver;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        driver = new EnhancedWebDriver(BrowserDriver.getDriver(BrowserOption.Chrome));
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        driver.quit();
    }

    @Test
    public void basicCrawl() throws InterruptedException, IOException, SAXException, ParserConfigurationException {
        String id = GeneCrawler.getGeneId(driver, "FBN1");

        assertThat("ID did not match",
                id,
                is(equalTo("2200"))
        );

        Gene gene = GeneCrawler.getGeneInfo(driver, id);
        Allele allele = GeneCrawler.getAlleleInfo(driver,id);

        System.out.println(gene.toString());
        System.out.println(allele.toString());

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

    }
}
