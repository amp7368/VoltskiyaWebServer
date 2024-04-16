package apple.voltskiya.webroot.model.site;

import apple.voltskiya.webroot.model.BaseEntity;
import apple.voltskiya.webroot.model.json.DJsonDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "site")
public class DSite extends BaseEntity {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany
    private List<DJsonDocument> documents = new ArrayList<>();

    public DSite(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Site toSite() {
        return new Site(this);
    }

    public UUID getId() {
        return id;
    }

    public List<DJsonDocument> getDocuments() {
        return documents;
    }
}
