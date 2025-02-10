package io.github.sinuscosinustan.hetznercloud.deserialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer extends StdDeserializer<Date> {

    private static final long serialVersionUID = 1L;

    private final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() ->
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));

    public DateDeserializer() {
        this(null);
    }

    public DateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext context) {
        try {
            String date = jsonParser.getText();
            return dateFormatThreadLocal.get().parse(date);
        } catch (IOException | ParseException ignored) {}
        return null;
    }
}
