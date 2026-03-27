import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate <--> JSON
 * Source: <a href="https://www.youtube.com/watch?v=j2vWhohxBVA">...</a>
 */
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDate.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
