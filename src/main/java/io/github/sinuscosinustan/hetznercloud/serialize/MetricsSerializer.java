package io.github.sinuscosinustan.hetznercloud.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MetricsSerializer extends JsonSerializer<Object> {

    @Override
    public void serialize(Object object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField(jsonGenerator.getOutputContext().getCurrentName(), object);
        jsonGenerator.writeEndObject();
    }
}