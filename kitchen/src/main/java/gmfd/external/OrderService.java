
package gmfd.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="order", url="http://order:8080")
       // "http://localhost:8081")
public interface OrderService {

    @RequestMapping(method= RequestMethod.POST, path="/orders")
    public void cancel(@RequestBody Order order);

}