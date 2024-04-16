package apple.voltskiya.webroot.model.json;

import apple.utilities.util.FileFormatting;
import apple.voltskiya.webroot.WebPlugin;
import apple.voltskiya.webroot.model.BaseEntity;
import apple.voltskiya.webroot.model.site.DSite;
import com.google.gson.JsonObject;
import io.ebean.annotation.SoftDelete;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "json_document",
    uniqueConstraints = @UniqueConstraint(columnNames = {"site_id", "path"})
)
public class DJsonDocument extends BaseEntity {

    @Id
    private UUID id;
    @WhenCreated
    private Timestamp createdAt;
    @WhenModified
    private Timestamp modifiedAt;
    @SoftDelete
    private boolean deleted;

    @ManyToOne
    private DSite site;
    @Column(nullable = false)
    private String path;
    @Column(nullable = false, columnDefinition = "text")
    private String currentJson;
    @OneToMany
    private List<DJsonField> fields;

    public DJsonDocument(DSite site, String path) {
        this.site = site;
        this.path = path;
        this.currentJson = "{}";
    }

    public JsonObject getJson() {
        return JsonUpdateApi.gson().fromJson(currentJson, JsonObject.class);
    }

    public String getJsonString() {
        return currentJson;
    }

    public DJsonDocument setCurrentJson(String currentJson) {
        this.currentJson = currentJson;
        return this;
    }

    public UUID getId() {
        return this.id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public DSite getSite() {
        return site;
    }

    public String getPath() {
        return path;
    }

    public File getFile() {
        File root = WebPlugin.get().getRootFile("JSON");
        String site = this.getSite().getName();
        return FileFormatting.fileWithChildren(root, site, "j", this.getPath());
    }
}
