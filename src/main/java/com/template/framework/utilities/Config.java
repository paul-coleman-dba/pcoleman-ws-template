package com.template.framework.utilities;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class Config
{
    private static Map<String, String> env;

    static
    {
        try
        {
            env = Environment.getEnvironmentMap();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static Config single_instance = null;

    // private constructor restricted to this class itself
    private Config()
    {
    }

    // static method to create instance of Singleton class
    public static Config getInstance()
    {
        if (single_instance == null)
        {
            single_instance = new Config();
        }

        return single_instance;
    }

    private String getVar(String envVarName)
    {
        String value = env.get(envVarName);
        if (StringUtils.isEmpty(value))
        {
            throw new IllegalStateException("Environment variable [" + envVarName + "] is not set.");
        }
        return value;
    }

    private static final String fixed_thread_pool_size = env.get("FIXED_THREAD_POOL_SIZE");
    public static final int FIXED_THREAD_POOL_SIZE = StringUtils.isEmpty(fixed_thread_pool_size) ? 100 : Integer.valueOf(fixed_thread_pool_size);

    private static final String default_aad = env.get("DEFAULT_AAD");
    public static byte[] DEFAULT_AAD = new byte[0];

    static
    {
        try
        {
            DEFAULT_AAD = StringUtils.isEmpty(default_aad) ? "random-x-heroku-services".getBytes("UTF-8") : default_aad.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    private static final String iv_size = env.get("IV_SIZE");
    public static int IV_SIZE = StringUtils.isEmpty(iv_size) ? 12 : Integer.valueOf(iv_size);

    private static final String tag_bit_length = env.get("TAG_BIT_LENGTH");
    public static int TAG_BIT_LENGTH = StringUtils.isEmpty(tag_bit_length) ? 128 : Integer.valueOf(tag_bit_length);

    private final String aes_key_16 = env.get("AES_KEY_16");
    public final String AES_KEY_16 = StringUtils.isEmpty(aes_key_16) ? ( env.containsKey("DYNO")? "" : "3ncrypTh1spl3a5E") : aes_key_16;

    private final String debug = env.get("DEBUG");
    public final Boolean DEBUG = StringUtils.isEmpty(debug) ? false : debug.toLowerCase().equals("true");

}
