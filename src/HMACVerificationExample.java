import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class HMACVerificationExample {

    public static void main(String[] args) {
        String message = "0100201000056658|0100101000323977|10000";
        String secretKey = "my-secret-key";
        String receivedHmac = "52ab7e931d252677f8b6b82db89b4f44e7ad1d06d3e09d903491596d99709205";

        try {
            boolean isHmacValid = verifyHmacSha256(message, secretKey, receivedHmac);
            if (isHmacValid) {
                System.out.println("HMAC verification succeeded. The message is authentic.");
            } else {
                System.out.println("HMAC verification failed. The message may have been tampered with.");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public static boolean verifyHmacSha256(String message, String secretKey, String receivedHmac)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        hmacSha256.init(keySpec);
        byte[] hmacResult = hmacSha256.doFinal(message.getBytes());

        String computedHmac = bytesToHex(hmacResult);
        return computedHmac.equals(receivedHmac);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
