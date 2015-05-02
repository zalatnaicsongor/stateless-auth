package hu.zalatnai.sdk.service.infrastructure;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;

@Converter(autoApply = true)
public class InstantToUnixTimestampConverter implements AttributeConverter<Instant, Long> {

    @Override
    public Long convertToDatabaseColumn(Instant attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getEpochSecond();
    }

    @Override
    public Instant convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        return Instant.ofEpochSecond(dbData);
    }

}