package com.export;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@SpringBootApplication
public class Application {

    @GetMapping
    public void home(HttpServletResponse response) throws IOException {
        response.getWriter().print("Hello boss ^-^");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
