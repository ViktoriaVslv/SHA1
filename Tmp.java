package SHA1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tmp {
    public static String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }


        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String args[]) throws NoSuchAlgorithmException {
        String s1 = "London is the capital and largest city of England and of the United Kingdom.";
        String l = encryptThisString(s1);
        System.out.println("\n" + s1 + " : " +l);

    }
}