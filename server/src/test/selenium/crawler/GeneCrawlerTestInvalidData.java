package selenium.crawler;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GeneCrawlerTestInvalidData {
    @Test
    public void basicCrawl() throws ParserConfigurationException, SAXException, IOException, InterruptedException {
        assertThat("ID did not match",
                GeneCrawler.getGeneId("FBN1"),
                is(equalTo("2200"))
        );
        Thread.sleep(1500);
        assertThat("ID did not match",
                GeneCrawler.getGeneId("HUGO"),
                is(equalTo("22862"))
        );
        Thread.sleep(1500);
        assertThat("ID did not match",
                GeneCrawler.getGeneId("MySql"),
                is(equalTo("genDoesntExist"))
        );
    }
}
