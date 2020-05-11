package selenium.bean;

import java.io.Serializable;

public class GeneBibliography implements Serializable{
    private Integer alleleOrdNum, bibliographyReferenceId;

    public GeneBibliography(Integer alleleOrdNum, Integer bibliographyReferenceId) {
        this.alleleOrdNum = alleleOrdNum;
        this.bibliographyReferenceId = bibliographyReferenceId;
    }

    public GeneBibliography() {
    }

    public Integer getAlleleOrdNum() {
        return alleleOrdNum;
    }

    public void setAlleleOrdNum(Integer alleleOrdNum) {
        this.alleleOrdNum = alleleOrdNum;
    }

    public Integer getBibliographyReferenceId() {
        return bibliographyReferenceId;
    }

    public void setBibliographyReferenceId(Integer bibliographyReferenceId) {
        this.bibliographyReferenceId = bibliographyReferenceId;
    }

    @Override
    public String toString() {
        return "GeneBibliography{" +
                "alleleOrdNum=" + alleleOrdNum +
                ", bibliographyReferenceId=" + bibliographyReferenceId +
                '}';
    }
}
