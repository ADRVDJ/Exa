package com.example.CONFIG;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.example.MODEL.ERole;
import com.example.MODEL.RoleEntity;

import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<ERole> {

    @Override
    public ERole deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String role = p.getText();
        return ERole.valueOf(role.replace("ROLE_", ""));
    }
}

