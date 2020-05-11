package selenium.bean;

import java.io.Serializable;
import java.util.Objects;

public class Reference implements Serializable {
    //id del dbms
    private Integer idReference;
    // atriibutos recolectados
    private String id, title, authors, articleAbstract, publicationTitle;

    public Reference() { this.idReference = 0; }

    public Reference(Integer idReference, String id, String title, String authors, String articleAbstract, String publicationTitle) {
        this.idReference = idReference;
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.articleAbstract = articleAbstract;
        this.publicationTitle = publicationTitle;
    }

    public Integer getIdReference() {
        return idReference;
    }

    public void setIdReference(Integer idReference) {
        this.idReference = idReference;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public String getPublicationTitle() {
        return publicationTitle;
    }

    public void setPublicationTitle(String publicationTitle) {
        this.publicationTitle = publicationTitle;
    }

    @Override
    public String toString() {
        return "Reference{" +
                "idReference=" + idReference +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", articleAbstract='" + articleAbstract + '\'' +
                ", publicationTitle='" + publicationTitle + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reference)) return false;
        Reference reference = (Reference) o;
        return Objects.equals(id, reference.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
