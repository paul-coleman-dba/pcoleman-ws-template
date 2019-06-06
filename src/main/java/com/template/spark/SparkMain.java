package com.template.spark;

import com.template.framework.service.BaseService;
import com.template.framework.service.DocumentationService;
import com.template.framework.utilities.Environment;
import com.template.transformer.JsonTransformer;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;


public class SparkMain {

    public static final HashMap<String, String> ENV = new HashMap<>();

    public static void main(String args[]) throws Exception {

        ENV.putAll(Environment.getEnvironmentMap());

        String port = ENV.get("PORT") != null ? ENV.get("PORT") : "8000";
        port( Integer.parseInt( port ) );

        get("/", (request, response) -> {
            response.type("text/plain");
            return new DocumentationService().readStaticFile(request, response, "spark/template/freemarker/documentation.ftl", "UTF-8");
        });

        get("/feed/:version/swagger.json", (request, response) ->
        {
            response.type("text/plain");
            return new DocumentationService().readStaticFile(request, response, String.format("spark/template/freemarker/%s", "template-" + request.params("version") + ".swagger-json.ftl"), "UTF-8");
        });

        get("/feed/:version/swagger.yaml", (request, response) ->
        {
            response.type("text/plain");
            return new DocumentationService().readStaticFile(request, response, String.format("spark/template/freemarker/%s", "template-" + request.params("version") + ".swagger.ftl"), "UTF-8");
        });

        get("/feed/:version/openapi.yaml", (request, response) ->
        {
            response.type("text/plain");
            return new DocumentationService().readStaticFile(request, response, String.format("spark/template/freemarker/%s", "template-" + request.params("version") + ".swagger.ftl"), "UTF-8");
        });

        get("feed/:version/application.wadl", (request, response) -> {
            response.type("application/xml");
            return new DocumentationService().readStaticFile(request, response, String.format("spark/template/freemarker/%s", "template-" + request.params("version") + ".wadl.ftl"), "UTF-8");
        });

        Spark.get("/feed/:version", (request, response) -> {
            BaseService.ensureSecureTransport(request,response);
            response.type("application/json");
            Map<String,String> result = new HashMap();
            result.put("msg", "result");
            return result;
        }, new JsonTransformer());

    }
}
