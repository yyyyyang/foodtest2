
package gmfd.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="kitchen", url="http://kitchen:8080")
                                //"http://localhost:8089")
public interface KitchenService {

    @RequestMapping(method= RequestMethod.GET, path="/kitchens")
    public void cook(@RequestBody Kitchen kitchen);

}