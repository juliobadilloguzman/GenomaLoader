package selenium.bean;

import java.io.Serializable;

public class Allele implements Serializable {
    //Id del dbms
    private Integer idAllele, idGene;
    //Atributos recolectados
    private String geneAccession, sequenceStart, sequenceEnd, strand, sequence;

    public Allele() {
        this.idAllele = 0;
    }

    public Allele(Integer idAllele, Integer idGene, String geneAccession, String sequenceStart, String sequenceEnd, String strand, String sequence) {
        this.idAllele = idAllele;
        this.idGene = idGene;
        this.geneAccession = geneAccession;
        this.sequenceStart = sequenceStart;
        this.sequenceEnd = sequenceEnd;
        this.strand = strand;
        this.sequence = sequence;
    }

    public Integer getIdAllele() {
        return idAllele;
    }

    public void setIdAllele(Integer idAllele) {
        this.idAllele = idAllele;
    }

    public Integer getIdGene() {
        return idGene;
    }

    public void setIdGene(Integer idGene) {
        this.idGene = idGene;
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

    public String getSequence() {
        return strand;
    }

    public void setSequence(String strand) {
        this.strand = strand;
    }

    @Override
    public String toString() {
        return "Allele{" +
                "idAllele=" + idAllele +
                ", idGene=" + idGene +
                ", geneAccession='" + geneAccession + '\'' +
                ", sequenceStart='" + sequenceStart + '\'' +
                ", sequenceEnd='" + sequenceEnd + '\'' +
                ", strand='" + strand + '\'' +
                ", sequence='" + sequence + '\'' +
                '}';
    }
}
