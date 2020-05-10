package selenium.bean;

import java.io.Serializable;

public class Allele implements Serializable {
    //Id del dbms
    private Integer idAllele;
    //Atributos recolectados
    private String geneAccession, sequenceStart, sequenceEnd, strand;

    public Allele() {
        this.idAllele = 0;
    }

    public Allele(Integer idAllele, String geneAccession, String sequenceStart, String sequenceEnd, String strand) {
        this.idAllele = idAllele;
        this.geneAccession = geneAccession;
        this.sequenceStart = sequenceStart;
        this.sequenceEnd = sequenceEnd;
        this.strand = strand;
    }

    public Integer getIdAllele() {
        return idAllele;
    }

    public void setIdAllele(Integer idAllele) {
        this.idAllele = idAllele;
    }

    public String getGeneAccession() {
        return geneAccession;
    }

    public void setGeneAccession(String geneAccession) {
        this.geneAccession = geneAccession;
    }

    public String getSequenceStart() {
        return sequenceStart;
    }

    public void setSequenceStart(String sequenceStart) {
        this.sequenceStart = sequenceStart;
    }

    public String getSequenceEnd() {
        return sequenceEnd;
    }

    public void setSequenceEnd(String sequenceEnd) {
        this.sequenceEnd = sequenceEnd;
    }

    public String getStrand() {
        return strand;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    @Override
    public String toString() {
        return "Allele{" +
                "idAllele=" + idAllele +
                ", geneAccession='" + geneAccession + '\'' +
                ", sequenceStart='" + sequenceStart + '\'' +
                ", sequenceEnd='" + sequenceEnd + '\'' +
                ", strand='" + strand + '\'' +
                '}';
    }
}
