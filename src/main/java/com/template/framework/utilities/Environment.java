package com.template.framework.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Environment
{

    private static final Map<String, String> ENVIRONMENT_MAP = new HashMap<String, String>();

    public static Map<String, String> getEnvironmentMap()
    {
        try
        {
            if (ENVIRONMENT_MAP.isEmpty())
            {
                Map<String, String> envs = System.getenv();
                if (envs.containsKey("DYNO"))
                {
                    System.out.println(" getEnvironmentMap: we are in heroku environment ");
                    ENVIRONMENT_MAP.putAll(envs);
                }
                else if (envs.containsKey("ENV_FILE"))
                {
                    System.out.println(" getEnvironmentMap: we are in heroku local ");
                    ENVIRONMENT_MAP.putAll(envs);
                    ENVIRONMENT_MAP.put("X-Forwarded-Proto", "https");
                }
                else
                {
                    System.out.println(" getEnvironmentMap : we are in java run ");
                    File f = new File(".env");
                    if (f.exists() && !f.isDirectory())
                    {
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(f));
                        for (Object key : properties.keySet())
                        {
                            ENVIRONMENT_MAP.put((String) key, (String) properties.get(key));
                        }
                    }
                    ENVIRONMENT_MAP.put("X-Forwarded-Proto", "https");
                }
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex.getMessage());
        }
        return ENVIRONMENT_MAP;
    }

}
