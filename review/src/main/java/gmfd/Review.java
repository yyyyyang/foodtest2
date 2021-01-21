package gmfd;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Review_table")
public class Review {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String username;
    private String content;
    private String productname;

    @PostPersist
    public void onPostPersist(){
        ReviewRegistered reviewRegistered = new ReviewRegistered();
        BeanUtils.copyProperties(this, reviewRegistered);
        reviewRegistered.publishAfterCommit();


    }

    @PreUpdate
    public void onPreUpdate(){
        ReviewUpdated reviewUpdated = new ReviewUpdated();
        BeanUtils.copyProperties(this, reviewUpdated);
        reviewUpdated.publishAfterCommit();


    }

    @PreRemove
    public void onPreRemove(){
        ReviewDeleted reviewDeleted = new ReviewDeleted();
        BeanUtils.copyProperties(this, reviewDeleted);
        reviewDeleted.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }




}
