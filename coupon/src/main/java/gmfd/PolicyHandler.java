package gmfd;

import gmfd.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }


    //Bean 간 연결
    @Autowired
    CouponRepository couponRepository;
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCancelled_remove(@Payload Kitchencancelled kitchencancelled){

        if(kitchencancelled.isMe()){
            System.out.println("##### listener  : " + kitchencancelled.toJson());

            //Mileage mileage = mileageRepository.findById(cancelled.getId()).get();
            //mileageRepository.deleteByPayId(cancelled.getId());
            //mileageRepository.saveAll();

            Optional<Coupon> optionalCoupon = couponRepository.findByPayId(kitchencancelled.getId());
            Coupon coupon = optionalCoupon.get();

            //pay.setOrderid(deliveryCanceled.getOrderId());

            couponRepository.delete(coupon);
            //

            /*Cancelled cancelled = new Cancelled();
            cancelled.setAmout(pay.getAmout());
            cancelled.setId(pay.getId());
            cancelled.setOrderid(pay.getOrderid());
            cancelled.setStatus(pay.getStatus());
            cancelled.publish();*/
        }
    }

}
