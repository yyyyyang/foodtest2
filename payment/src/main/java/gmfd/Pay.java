package gmfd;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Pay_table")
public class Pay {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer amout;
    private String status;
    private Long orderid;

    @PostPersist
    public void onPostPersist(){
        //원래소스
        /*Paid paid = new Paid();
        BeanUtils.copyProperties(this, paid);
        paid.publishAfterCommit();*/

        Paid paid = new Paid();
        BeanUtils.copyProperties(this, paid);

        paid.publishAfterCommit();

        gmfd.external.Kitchen kitchen = new gmfd.external.Kitchen();
        // mappings goes here
        kitchen.setOrderid(this.getId());
        kitchen.setStatus("Paid");
        kitchen.setAmount(this.getAmout());


        PaymentApplication.applicationContext.getBean(gmfd.external.KitchenService.class).cook(kitchen);


        /*TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit( ) {
                paid.publishAfterCommit();
            }
        });


        try {
            Thread.currentThread().sleep((long) (500 + Math.random() * 220));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }

    @PreRemove
    public void onPreRemove(){
        Cancelled cancelled = new Cancelled();
        BeanUtils.copyProperties(this, cancelled);
        cancelled.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getAmout() {
        return amout;
    }

    public void setAmout(Integer amout) {
        this.amout = amout;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }




}
