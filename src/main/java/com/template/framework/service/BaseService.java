package com.template.framework.service;

import com.template.framework.response.BaseResponse;
import com.template.framework.utilities.Environment;
import org.apache.commons.lang3.StringUtils;
import spark.Request;
import spark.Response;

public class BaseService
{
    public static void ensureSecureTransport(Request request, Response response) throws RuntimeException
    {
        String protocolHeader = request.headers("X-Forwarded-Proto");
        String forwardHeader = Environment.getEnvironmentMap().get("X-Forwarded-Proto");
        boolean isHTTPs = "https".equals(protocolHeader) || "https".equals(forwardHeader);
        if (!isHTTPs)
        {
            throw new RuntimeException("Insecure transport protocol");
        }
    }

    public static String getBearer(Request request, Response response)
    {
        String bearer;
        String token = request.headers("Authorization");
        if (StringUtils.isEmpty(token))
        {
            response.status(403);
        }
        String[] tokens = token.split(" ");
        if (tokens.length == 2 && "bearer".equals(tokens[0].toLowerCase()))
        {
            bearer = tokens[1];
        }
        else
        {
            throw new RuntimeException(BaseResponse.StatusMap.get(BaseResponse.MISSING_TOKEN));
        }

        return bearer;
    }
}
