package gmfd;

public class Kitchencancelled extends AbstractEvent {

    private Long id;
    private Integer amout;
    private String status;
    private Long orderid;

    public Kitchencancelled(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getAmount() {
        return amout;
    }

    public void setAmount(Integer amout) {
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