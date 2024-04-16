package apple.voltskiya.webroot.model.json;

import apple.voltskiya.webroot.model.BaseEntity;
import io.ebean.annotation.Index;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "json_field",
    uniqueConstraints = @UniqueConstraint(columnNames = {"document_id", "key_path", "version"})
)
public class DJsonField extends BaseEntity {

    @Id
    private UUID id;
    @ManyToOne(optional = false)
    private DJsonDocument document;

    @Index
    @Column(nullable = false)
    private String keyPath;
    @Index
    @Column(nullable = false)
    private int version;
    @Column(columnDefinition = "MEDIUMTEXT")
    @Nullable
    private String json;

    public DJsonField(DJsonDocument document, String keyPath, @Nullable String json, int version) {
        this.document = document;
        this.keyPath = keyPath;
        this.json = json;
        this.version = version;
    }

    @Nullable
    public String getJson() {
        return json;
    }

    public DJsonField setJson(String json) {
        this.json = json;
        return this;
    }

    public int getVersion() {
        return version;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public DJsonDocument getDocument() {
        return this.document;
    }
}
