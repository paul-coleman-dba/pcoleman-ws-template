package com.template.framework.handler;

import spark.Request;
import spark.Response;

public class BaseHandler
{
    public Request request;
    public Response response;

    public BaseHandler(Request request, Response response )
    {
        this.request =request;
        this.response = response;
    }

}
