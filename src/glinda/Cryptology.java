
package glinda;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class Cryptology {
     public Cryptology() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException, InvalidKeySpecException{
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = g.generateKeyPair();
        PublicKey publicKey = keypair.getPublic();
        PrivateKey privateKey = keypair.getPrivate(); 
        
        //at sender's end
        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        ecdsaSign.initSign(privateKey);
        
        String message = "hello world";
        
        ecdsaSign.update(message.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String pri = Base64.getEncoder().encodeToString(privateKey.getEncoded());
         System.out.println(pub);
         System.out.println(pri);
        String sig = Base64.getEncoder().encodeToString(signature);
        
         String publicKey_x = "MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEgTYrPej1k0GSzTNMWqiBSRUTUQOEOr8sA4/KJ+nkvLa7UrnLIdfXtLZ0nyjkcNrXu592GwEPnpkTUwnLRpGatw==";
         String signature_x = "MEUCIQDbxudDgcY/Gb2vJrLJHtPNq9KC7NusZDE7/Zil5Be5MgIgNFoPnbLlMEKP8zu6zxa9QZzjfBNdZggFX0qjrauha5Y=";
         
        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey_x));

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey pubKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(pubKey);
        ecdsaVerify.update(message.getBytes("UTF-8"));
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(signature_x));
         System.out.println(result);
     }
     public static String sign(PrivateKey privateKey,String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException{
         Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(message.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        return Base64.getEncoder().encodeToString(signature);
     }
     public static boolean verify(PublicKey  publicKey, String message, String signature){
        try{
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(message.getBytes("UTF-8"));
            return ecdsaVerify.verify(Base64.getDecoder().decode(signature));
        }catch(Exception e){
            return false;
        }
     }
     public static KeyPair createKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        KeyPairGenerator g = KeyPairGenerator.getInstance("EC");
        g.initialize(ecSpec, new SecureRandom());
        return g.generateKeyPair();
     }
     public static String pubKeyToString(PublicKey publicKey){
         byte[] bytes = publicKey.getEncoded();
         return Base64.getEncoder().encodeToString(bytes);
     }
     public static PublicKey stringTopubKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
         byte[] bytes = Base64.getDecoder().decode(publicKey);
         KeyFactory kf = KeyFactory.getInstance("EC");
         return kf.generatePublic(new X509EncodedKeySpec(bytes));
     }
     public static String privKeyToString(PrivateKey privKey){
         byte[] bytes = privKey.getEncoded();
         return Base64.getEncoder().encodeToString(bytes);
     }
     public static PrivateKey stringToprivKey(String privKey) throws NoSuchAlgorithmException, InvalidKeySpecException{
         byte[] bytes = Base64.getDecoder().decode(privKey);
         KeyFactory kf = KeyFactory.getInstance("EC");
         return kf.generatePrivate(new PKCS8EncodedKeySpec(bytes));
     }
     public static String bytesToString(byte[] bytes) throws NoSuchAlgorithmException, InvalidKeySpecException{
         KeyFactory kf = KeyFactory.getInstance("EC");
         return pubKeyToString(kf.generatePublic(new X509EncodedKeySpec(bytes)));
     }
     
     // ONE TIME PADDING TO SAVE PRIVATE KEYS IN USER ACCESSABLE FILE
     public static String encrypt_oneTimePadding(String input){
         byte content_inByte[] = input.getBytes();
         byte content_encrypted[] = new byte[content_inByte.length];
         byte content_key[] = new byte[content_inByte.length];
         
         byte definedPassword_inByte[] = GUI.definedPassword.getBytes();
         if(definedPassword_inByte.length > content_key.length)
             content_key = (GUI.definedPassword.substring(0, content_key.length)).getBytes();
         else
             for(int i = 0; i < content_key.length; i++)
                 content_key[i] = definedPassword_inByte[i%(definedPassword_inByte.length)];
         for(int i = 0; i < content_inByte.length; i++)
             content_encrypted[i] = (byte) (content_inByte[i] ^ content_key[i]);
         return new String(content_encrypted);
     }
     public static String decrypt_oneTimePadding(byte[] content_inByte){
         byte content_decrypted[] = new byte[content_inByte.length];
         byte content_key[] = new byte[content_inByte.length];
         
         byte definedPassword_inByte[] = GUI.definedPassword.getBytes();
         if(definedPassword_inByte.length > content_key.length)
             content_key = (GUI.definedPassword.substring(0, content_key.length)).getBytes();
         else
             for(int i = 0; i < content_key.length; i++)
                 content_key[i] = definedPassword_inByte[i%(definedPassword_inByte.length)];
         for(int i = 0; i < content_inByte.length; i++)
             content_decrypted[i] = (byte) (content_inByte[i] ^ content_key[i]);
         return new String(content_decrypted);
     }
}
