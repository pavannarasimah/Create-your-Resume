import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference; // Added for type safety
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserData {
    private static final String FILE_PATH = "users.json";
    private final ObjectMapper objectMapper;

    public UserData() {
        objectMapper = new ObjectMapper();
    }

    // Load user data from file
    public Map<String, String[]> loadUserData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try {
            // Specify type explicitly for deserialization
            return objectMapper.readValue(file, new TypeReference<Map<String, String[]>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // Save user data to file
    public void saveUserData(Map<String, String[]> usersDatabase) {
        try {
            objectMapper.writeValue(new File(FILE_PATH), usersDatabase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
