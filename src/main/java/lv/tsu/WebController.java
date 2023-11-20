package lv.tsu;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @PostMapping("/blockData")
    String postBlockData(@RequestBody String reqBody) {
        System.out.println(reqBody);
        return reqBody;
    }

}
