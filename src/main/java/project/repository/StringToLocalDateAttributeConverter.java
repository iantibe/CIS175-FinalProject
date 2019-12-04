package project.repository;

import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringToLocalDateAttributeConverter implements AttributeConverter<String, LocalDate> {

	@Override
	public LocalDate convertToDatabaseColumn(String attribute) {
		return (attribute == null ? null : LocalDate.parse(attribute));
	}

	@Override
	public String convertToEntityAttribute(LocalDate dbData) {
		// TODO Auto-generated method stub
		return dbData.toString();
	}

}
