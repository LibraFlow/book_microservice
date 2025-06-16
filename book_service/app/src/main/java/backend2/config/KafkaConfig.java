package backend2.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {
    public static final String BOOK_DELETED_TOPIC = "book.deleted";
    public static final String BORROWING_CREATED_TOPIC = "borrowing.created";

    @Bean
    public NewTopic bookDeletedTopic() {
        return new NewTopic(BOOK_DELETED_TOPIC, 1, (short) 1);
    }
} 