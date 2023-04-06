package com.jova;
import com.github.mervick.aes_everywhere.Aes256;

/*Extracted from github -> https://github.com/mervick/aes-everywhere */
public class AES256 {
    public static String encrypt(String message, String key) throws Exception {
        return Aes256.encrypt(message, key);
    }

    public static String decrypt(String encryptedMessage, String key) throws Exception {
        return Aes256.decrypt(encryptedMessage, key);
    }
}