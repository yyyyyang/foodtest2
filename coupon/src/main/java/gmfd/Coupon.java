package gmfd;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Entity
@Table(name="Coupon_table")
public class Coupon {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long payId;
    private Long orderId;
    private Integer cnt;

    @PostPersist
    public void onPostPersist(){
        Couponadded couponadded = new Couponadded();
        BeanUtils.copyProperties(this, couponadded);
        couponadded.publish();

        /*
       TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void beforeCommit(boolean readonly ) {
                milegeadded.publishAfterCommit();
            }
        });


        try {
            Thread.currentThread().sleep((long) (400 + Math.random() * 220));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/






    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }




}
