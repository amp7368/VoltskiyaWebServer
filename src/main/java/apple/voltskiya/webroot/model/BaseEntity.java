package apple.voltskiya.webroot.model;

import io.ebean.Model;
import io.ebean.annotation.DbName;
import javax.persistence.MappedSuperclass;

@DbName(WebDatabase.NAME)
@MappedSuperclass
public class BaseEntity extends Model {

    public BaseEntity() {
        super(WebDatabase.NAME);
    }
}
