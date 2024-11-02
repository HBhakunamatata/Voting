package cloud.popples.voting.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 24957
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping(value = {"/", "/index", "/home"})
    public String index() {
        return "index";
    }

}
