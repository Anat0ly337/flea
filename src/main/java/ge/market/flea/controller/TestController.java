package ge.market.flea.controller;

import ge.market.flea.data.entity.Goods;
import ge.market.flea.service.MobileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "/")
public class TestController {
    @Autowired
    private MobileService mobileService;

    @GetMapping("search/{search}/{page}")
    public List<Goods> search(@PathVariable String search, @PathVariable Integer page) {

        return mobileService.find(search, page);
    }
}
