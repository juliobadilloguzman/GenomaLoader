package selenium.crawler;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;
import selenium.util.ReferenceMemory;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReferenceMemoryTest {
    public static ReferenceMemory referenceMemory;

    @BeforeClass
    public static void setUpBeforeClass()  {
        referenceMemory = ReferenceMemory.getInstance();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        referenceMemory = null;
    }

    @Test
    public void referenceMemory_should_avoidDuplicates() throws ParserConfigurationException, SAXException, IOException, SQLException {
        referenceMemory.queueReference("20886638");
        referenceMemory.queueReference("15054843");
        referenceMemory.queueReference("16540720");
        referenceMemory.queueReference("24078565");
        referenceMemory.queueReference("16103519");
        referenceMemory.queueReference("10633129");
        referenceMemory.queueReference("16540720");
        referenceMemory.queueReference("24078565");

        System.out.println("first try");
        referenceMemory.writeReferences();
        System.out.println("second");
        referenceMemory.queueReference("20886638");
        referenceMemory.writeReferences();

        System.out.println(referenceMemory.getReference("15054843").getIdReference());

        //American journal of medical genetics. Part A
        assertThat("title did not match",
                referenceMemory.getReference("15054843").getPublicationTitle(),
                is(equalTo("American journal of medical genetics. Part A"))
        );
    }

}
