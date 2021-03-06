package pl.sobsoft.spring.boot.compression;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class CompressionSampleApp {

    public static void main(String[] args) {
        SpringApplication.run(CompressionSampleApp.class, args);
    }


    @RestController
    public static class SampleController {

        @RequestMapping(method = RequestMethod.GET, value = "/")
        public SomeDTO getSomething() {
            return new SomeDTO("Something");
        }
    }
}
