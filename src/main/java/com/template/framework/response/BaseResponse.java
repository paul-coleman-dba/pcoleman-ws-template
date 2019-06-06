package com.template.framework.response;

import com.template.utilities.Utility;

import java.util.HashMap;

public class BaseResponse
{
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String MISSING_TOKEN = "MISSING TOKEN";

    public static final HashMap<String, String> StatusMap = new HashMap();

    static
    {
        StatusMap.put(MISSING_TOKEN, "Missing Authorization token");
        StatusMap.put(UNAUTHORIZED, "Unauthorized Request");
    }

    public BaseResponse()
    {
    }

    public String toString()
    {
        return Utility.getJSON(this);
    }

}
