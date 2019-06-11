package com.template.framework.utilities;


import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * This class shows how to securely perform AES encryption in GCM mode, with 256 bits key size.
 * Based on https://github.com/1MansiS/java_crypto/blob/master/cipher/SecuredGCMUsage.java
 */
public class DefaultCipher
{
    private static final Config c = Config.getInstance();
    private static final byte[] DEFAULT_AAD = c.DEFAULT_AAD;
    private static final int IV_SIZE = c.IV_SIZE;
    private static final int TAG_BIT_LENGTH = c.TAG_BIT_LENGTH;
    private static final String ALGO_TRANSFORMATION_STRING = "AES/GCM/PKCS5Padding";
    private static final String AES_KEY = c.AES_KEY_16;

    private class AESKey
    {
        public SecretKey aesKey;
        public GCMParameterSpec gcmParamSpec;
        public byte[] iv;
        public byte[] aadData;
        public byte[] data;

        public AESKey(SecretKey aesKey, GCMParameterSpec gcmParamSpec, byte[] iv, byte[] aadData)
        {
            this.aesKey = aesKey;
            this.gcmParamSpec = gcmParamSpec;
            this.iv = iv;
            this.aadData = aadData;
        }

        public void setData(byte[] data)
        {
            this.data = data;
        }

        public byte[] getData()
        {
            return data;
        }
    }

    public static String encrypt(String plainText)
    {
        return encryptAAD(plainText, new String(DEFAULT_AAD));
    }

    public static String decrypt(String cipherText)
    {
        return decryptAAD(cipherText, new String(DEFAULT_AAD));
    }

    public static String encryptAAD(String plainText, String aad)
    {
        String cipherText = "";
        byte[] aadData = getAAD(aad);
        byte[] cipherBytes = new byte[0];
        try
        {
            cipherBytes = plainText.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        AESKey dataKey = doCipher(cipherBytes, aadData, true, null);
        byte[] encryptedBytes = dataKey.getData();
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + dataKey.iv.length + encryptedBytes.length);
        byteBuffer.putInt(dataKey.iv.length);
        byteBuffer.put(dataKey.iv);
        byteBuffer.put(encryptedBytes);
        byte[] cipherMessage = byteBuffer.array();
        cipherText = Base64.getEncoder().encodeToString(cipherMessage);

        return cipherText;
    }


    public static String decryptAAD(String cipherText, String aad)
    {
        String plainText = "";
        byte[] aadData = getAAD(aad);
        byte[] decodedMessage = Base64.getDecoder().decode(cipherText);
        ByteBuffer decryptByteBuffer = ByteBuffer.wrap(decodedMessage);
        int ivLength = decryptByteBuffer.getInt();
        byte[] decryptIv = new byte[ivLength];
        decryptByteBuffer.get(decryptIv);
        byte[] cipherBytes = new byte[decryptByteBuffer.remaining()];
        decryptByteBuffer.get(cipherBytes);

        AESKey dataKey = doCipher(cipherBytes, aadData, false, decryptIv);
        plainText = new String(dataKey.getData());

        return plainText;
    }

    private static AESKey doCipher(byte[] data, byte[] aadData, boolean isEncrypt, byte[] seed)
    {
        DefaultCipher defaultCipher = new DefaultCipher();
        AESKey key = defaultCipher.getAESKey(aadData, seed);
        javax.crypto.Cipher cipher = defaultCipher.getCipher(isEncrypt, key);
        key.setData(isEncrypt ? defaultCipher.aesEncrypt(new String(data), cipher) : defaultCipher.aesDecrypt(data, cipher));
        return key;
    }

    private AESKey getAESKey(byte[] aadData, byte[] seed)
    {

        // Generating Key
        SecretKey aesKey = new SecretKeySpec(AES_KEY.getBytes(), "AES");

        // Generating IV
        byte iv[] = (null == seed) ? new byte[IV_SIZE] : seed;
        SecureRandom secRandom = new SecureRandom();
        if (null == seed)
        {
            secRandom.nextBytes(iv);
        }

        String ivString = Base64.getEncoder().encodeToString(iv);

        // Initialize GCM Parameters
        GCMParameterSpec gcmParamSpec = new GCMParameterSpec(TAG_BIT_LENGTH, iv);
        return new AESKey(aesKey, gcmParamSpec, iv, aadData);
    }


    private static byte[] getAAD(String aad)
    {
        byte[] aadBytes = new byte[0];
        try
        {
            aadBytes = StringUtils.isEmpty(aad) ? DEFAULT_AAD : aad.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return aadBytes;
    }

    public javax.crypto.Cipher getCipher(boolean isEncrypt, AESKey key)
    {
        javax.crypto.Cipher c = null;

        try
        {
            c = javax.crypto.Cipher.getInstance(ALGO_TRANSFORMATION_STRING); // Transformation specifies algortihm, mode of operation and padding
        }
        catch (NoSuchAlgorithmException noSuchAlgoExc)
        {
            System.out.println("Exception while decrypting. Algorithm being requested is not available in environment " + noSuchAlgoExc);
            System.exit(1);
        }
        catch (NoSuchPaddingException noSuchAlgoExc)
        {
            System.out.println("Exception while decrypting. Padding scheme being requested is not available in environment " + noSuchAlgoExc);
            System.exit(1);
        }

        try
        {
            c.init((isEncrypt ? javax.crypto.Cipher.ENCRYPT_MODE : javax.crypto.Cipher.DECRYPT_MODE), key.aesKey, key.gcmParamSpec, new SecureRandom());
        }
        catch (InvalidKeyException invalidKeyExc)
        {
            System.out.println("Exception while encrypting. Key being used is not valid. It could be due to invalid encoding, wrong length or uninitialized " + invalidKeyExc);
            System.exit(1);
        }
        catch (InvalidAlgorithmParameterException invalidParamSpecExc)
        {
            System.out.println("Exception while encrypting. Algorithm Param being used is not valid. " + invalidParamSpecExc);
            System.exit(1);
        }

        try
        {
            c.updateAAD(key.aadData); // Add AAD details before decrypting
        }
        catch (IllegalArgumentException illegalArgumentExc)
        {
            System.out.println("Exception thrown while encrypting. Byte array might be null " + illegalArgumentExc);
            System.exit(1);
        }
        catch (IllegalStateException illegalStateExc)
        {
            System.out.println("Exception thrown while encrypting. CIpher is in an illegal state " + illegalStateExc);
            System.exit(1);
        }

        return c;
    }

    public byte[] aesEncrypt(String message, javax.crypto.Cipher c)
    {

        byte[] cipherTextInByteArr = null;
        try
        {
            cipherTextInByteArr = c.doFinal(message.getBytes());
        }
        catch (IllegalBlockSizeException illegalBlockSizeExc)
        {
            System.out.println("Exception while encrypting, due to block size " + illegalBlockSizeExc);
            System.exit(1);
        }
        catch (BadPaddingException badPaddingExc)
        {
            System.out.println("Exception while encrypting, due to padding scheme " + badPaddingExc);
            System.exit(1);
        }

        return cipherTextInByteArr;
    }


    public byte[] aesDecrypt(byte[] encryptedMessage, javax.crypto.Cipher c)
    {
        byte[] plainTextInByteArr = null;
        try
        {
            plainTextInByteArr = c.doFinal(encryptedMessage);
        }
        catch (IllegalBlockSizeException illegalBlockSizeExc)
        {
            System.out.println("Exception while decryption, due to block size " + illegalBlockSizeExc);
            System.exit(1);
        }
        catch (BadPaddingException badPaddingExc)
        {
            System.out.println("Exception while decryption, due to padding scheme " + badPaddingExc);
            System.exit(1);
        }

        return plainTextInByteArr;
    }

}