package selenium.bean;

import java.io.Serializable;

public class Gene implements Serializable {
    //Id del dbms
    private Integer idGene;
    //Atributos recolectados
    private String id, name, nomenclatureName, summary, chromosome, locus;

    public Gene() {
        this.idGene = 0;
    }

    public Gene(Integer idGene, String id, String name, String nomenclatureName, String summary, String chromosome, String locus) {
        this.idGene = idGene;
        this.id = id;
        this.name = name;
        this.nomenclatureName = nomenclatureName;
        this.summary = summary;
        this.chromosome = chromosome;
        this.locus = locus;
    }

    public Integer getIdGene() {
        return idGene;
    }

    public void setIdGene(Integer idGene) {
        this.idGene = idGene;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNomenclatureName() {
        return nomenclatureName;
    }

    public void setNomenclatureName(String nomenclatureName) {
        this.nomenclatureName = nomenclatureName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getLocus() {
        return locus;
    }

    public void setLocus(String locus) {
        this.locus = locus;
    }

    @Override
    public String toString() {
        return "Gene{" +
                "idGene='" + idGene.toString() + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", nomenclatureName='" + nomenclatureName + '\'' +
                ", summary='" + summary + '\'' +
                ", chromosome='" + chromosome + '\'' +
                ", locus='" + locus + '\'' +
                '}';
    }
}
