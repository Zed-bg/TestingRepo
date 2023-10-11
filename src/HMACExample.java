import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMACExample {

    public static void main(String[] args) {
        String message = "0100201000056658|0100101000323977|10000";
        String secretKey = "my-secret-key";

        try {
            String hmacResult = computeHmacSha256(message, secretKey);
            System.out.println("HMAC-SHA256: " + hmacResult);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static String computeHmacSha256(String message, String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        hmacSha256.init(keySpec);
        byte[] hmacResult = hmacSha256.doFinal(message.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : hmacResult) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
