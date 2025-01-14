package com.prathamesh.app.config;

import java.util.UUID;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UUIDToStringConverter implements AttributeConverter<UUID, String> {

	@Override
	public String convertToDatabaseColumn(UUID uuid) {
		return uuid == null ? null : uuid.toString();
	}

	@Override
	public UUID convertToEntityAttribute(String uuid) {
		try {
			return uuid == null ? null : UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
