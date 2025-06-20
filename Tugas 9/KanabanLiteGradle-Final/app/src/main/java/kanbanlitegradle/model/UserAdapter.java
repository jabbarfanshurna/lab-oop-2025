package kanbanlitegradle.model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class UserAdapter implements JsonSerializer<User>, JsonDeserializer<User> {

    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("username", src.getUsername());
        obj.addProperty("password", src.getPassword());
        obj.addProperty("role", src.getRole());
        return obj;
    }

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        if (!obj.has("username") || !obj.has("password") || !obj.has("role")) {
            throw new JsonParseException("Invalid user data: missing required fields");
        }

        String username = obj.get("username").getAsString();
        String password = obj.get("password").getAsString();
        String role = obj.get("role").getAsString();

        if ("manager".equalsIgnoreCase(role)) {
            return new Manager(username, password, "manager");
        } else {
            return new Member(username, password, "member");
        }
    }
}