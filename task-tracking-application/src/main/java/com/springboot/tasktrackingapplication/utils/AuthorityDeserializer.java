package com.springboot.tasktrackingapplication.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.springboot.tasktrackingapplication.entity.Authority;

public class AuthorityDeserializer extends JsonDeserializer<Authority> {
	
    @Override
    public Authority deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String authorityName = jsonParser.getText();
        return Authority.builder().name(authorityName).build();
    }
}
