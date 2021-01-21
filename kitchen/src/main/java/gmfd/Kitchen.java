package gmfd;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import gmfd.KitchenApplication;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Entity
@Table(name="Kitchen_table")
public class Kitchen {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer amount;
    private String status;
    private Long orderid;

    @PostPersist
    public void onPostPersist(){
        Cooked cooked = new Cooked();
        BeanUtils.copyProperties(this, cooked);
        cooked.publishAfterCommit();

        /*TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit( ) {
                cooked.publishAfterCommit();
            }
        });


        try {
            Thread.currentThread().sleep((long) (500 + Math.random() * 220));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        gmfd.external.Coupon coupon = new gmfd.external.Coupon();
        // mappings goes here
        coupon.setPayId(this.getId());
        coupon.setOrderId(this.getOrderid());
        coupon.setCnt(this.getAmount());

        KitchenApplication.applicationContext.getBean(gmfd.external.CouponService.class).mileage(coupon);

    }

    @PreRemove
    public void onPreRemove(){
        gmfd.Kitchencancelled kitchencancelled = new gmfd.Kitchencancelled();
        BeanUtils.copyProperties(this, kitchencancelled);
        kitchencancelled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        gmfd.external.Order cancellation = new gmfd.external.Order();

        cancellation.setId(this.getId());
        cancellation.setStatus("Kitchen Cancelled");

        // mappings goes here
        KitchenApplication.applicationContext.getBean(gmfd.external.OrderService.class)
            .cancel(cancellation);


    }

   /* @PreRemove
    public void onPreRemove(){
        OrderCancelled orderCancelled = new OrderCancelled();
        BeanUtils.copyProperties(this, orderCancelled);
        orderCancelled.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        gmfd.external.Cancellation cancellation = new gmfd.external.Cancellation();
        // mappings goes here
        cancellation.setOrderId(this.getId());
        cancellation.setStatus("Cancelled");



        OrderApplication.applicationContext.getBean(gmfd.external.CancellationService.class)
                .cancelShip(cancellation);


    }*/


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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
