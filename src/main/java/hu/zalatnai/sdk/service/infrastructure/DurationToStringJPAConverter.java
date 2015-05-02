package hu.zalatnai.sdk.service.infrastructure;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;

@Converter(autoApply = true)
public class DurationToStringJPAConverter implements AttributeConverter<Duration, String> {

    @Override
    public String convertToDatabaseColumn(Duration attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toString();
    }

    @Override
    public Duration convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Duration.parse(dbData);
    }

}