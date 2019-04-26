package io.pivotal.pal.tracker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.sql.DataSource;
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

    @Bean
    TimeEntryRepository create(DataSource datasource){

        //return new InMemoryTimeEntryRepository();
        return new JdbcTimeEntryRepository(datasource);
    }

    @Bean
    public ObjectMapper jsonObjectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .serializationInclusion(JsonInclude.Include.NON_NULL) // Don’t include null values
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //ISODate
                .modules(new JavaTimeModule())
                .build();
    }
}

