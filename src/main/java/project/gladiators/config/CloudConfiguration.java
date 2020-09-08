package project.gladiators.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration

public class CloudConfiguration {

    @Value("${cloudinary.cloud-name}")
    private String cloudinaryApiName;

    @Value("${cloudinary.api-key}")
    private String cloudinaryApiKey;

    @Value("${cloudinary.api-secret}")
    private String cloudinaryApiSecret;

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(new HashMap<String, Object>(){{
            put("cloud_name",cloudinaryApiName);
            put("api_key",cloudinaryApiKey);
            put("api_secret",cloudinaryApiSecret);
        }});
    }
}
