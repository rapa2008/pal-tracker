package io.pivotal.pal.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class PalTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
        EnvController controller = new EnvController(
                "8675",
                "12G",
                "34",
                "123.sesame.street"
        );

        Map<String, String> env = controller.getEnv();
        System.out.printf("PORT" + env.get("PORT"));
        System.out.println("MEMORY_LIMIT" + env.get("MEMORY_LIMIT"));
        System.out.println("CF_INSTANCE_INDEX" + env.get("CF_INSTANCE_INDEX"));
        System.out.println("CF_INSTANCE_ADDR" + env.get("CF_INSTANCE_ADDR"));

    }
}

