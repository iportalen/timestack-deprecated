package com.iportalen.timestack.config;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		return Optional.ofNullable(localDate)
				.map(Date::valueOf)
				.orElse(null);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date date) {
		return Optional.ofNullable(date)
				.map(Date::toLocalDate)
				.orElse(null);
	}
}