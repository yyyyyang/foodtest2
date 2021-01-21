
package gmfd.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="coupon", url=//"http://localhost:8090")
"http://coupon:8080")
public interface CouponService {

    @RequestMapping(method= RequestMethod.GET, path="/coupons")
    public void mileage(@RequestBody Coupon mileage);

}