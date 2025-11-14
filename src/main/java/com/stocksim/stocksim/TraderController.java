package com.stocksim.stocksim;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TraderController
{

    @RequestMapping("/hallo")
    public Trader hallo()
    {

        Trader trader = new Bot(1, "hans");

        return trader;
    }
}
