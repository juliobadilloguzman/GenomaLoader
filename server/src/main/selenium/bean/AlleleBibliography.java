package selenium.bean;

import java.io.Serializable;

public class AlleleBibliography implements Serializable {
    private String geneIdSymbol;
    private Integer bibliographyReferenceId;

    public AlleleBibliography(String geneIdSymbol, Integer bibliographyReferenceId) {
        this.geneIdSymbol = geneIdSymbol;
        this.bibliographyReferenceId = bibliographyReferenceId;
    }

    public AlleleBibliography(){}

    public String getGeneIdSymbol() {
        return geneIdSymbol;
    }

    public void setGeneIdSymbol(String geneIdSymbol) {
        this.geneIdSymbol = geneIdSymbol;
    }

    public Integer getBibliographyReferenceId() {
        return bibliographyReferenceId;
    }

    public void setBibliographyReferenceId(Integer bibliographyReferenceId) {
        this.bibliographyReferenceId = bibliographyReferenceId;
    }

    @Override
    public String toString() {
        return "AlleleBibliography{" +
                "geneIdSymbol='" + geneIdSymbol + '\'' +
                ", bibliographyReferenceId=" + bibliographyReferenceId +
                '}';
    }
}
