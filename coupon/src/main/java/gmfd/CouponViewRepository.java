package gmfd;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CouponViewRepository extends CrudRepository<CouponView, Long> {


        //void deleteByOrderId(Long orderId);
        //void deleteByPayId(Long payId);
        Optional<CouponView> findByPayId(Long payId);


}