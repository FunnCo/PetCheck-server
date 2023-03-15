package com.funnco.petcheckserver.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Converter
public class AtomicLongConverter implements AttributeConverter<AtomicLong, Long> {

    @Override
    public Long convertToDatabaseColumn(AtomicLong attribute) {
        return attribute.get();
    }

    @Override
    public AtomicLong convertToEntityAttribute(Long dbData) {
        return new AtomicLong(dbData);
    }

}

