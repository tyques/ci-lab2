package lab.shift.crmsystemforshift.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                // Если установить значение false, то при встрече с неоднозначными свойствами будет выбрасываться исключение ConfigurationException
                .setAmbiguityIgnored(true);

        return modelMapper;
    }
}

