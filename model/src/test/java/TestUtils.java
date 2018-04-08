import java.util.HashMap;
import java.util.Map;

public class TestUtils {
    public static void main(String[] args) {
        Map<String, Integer> stringIntegerMap = new HashMap<>();
        stringIntegerMap.put("Aha", 2);
        Integer aha = stringIntegerMap.get("Aha");
        aha++;
        stringIntegerMap.replace("Aha", aha);
        Integer aha1 = stringIntegerMap.get("Aha");
        System.out.println("aha1 = " + aha1);
    }
}
