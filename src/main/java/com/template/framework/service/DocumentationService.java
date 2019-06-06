package com.template.framework.service;

import spark.Request;
import spark.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class DocumentationService {

    /**
     * generates map to replace param in file.
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public static Map<String, Object> processDocumentation(Request request, Response response) throws Exception {
        response.type("application/xml");
        Map<String, Object> attributes = new HashMap<String, Object>();
        return attributes;
    }

    /**
     *  Reads file as it is to bypass free marker
     * @param request
     * @param response
     * @param path
     * @param encoding
     * @return
     */
    public String readStaticFile(Request request, Response response, String path, String encoding) {
        try {
            String url=request.url();
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream inputStream = cl.getResourceAsStream(path);
            BufferedReader br = new BufferedReader( new InputStreamReader(inputStream, encoding ) );
            StringBuilder sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                if(line.contains("@path/application.wadl")){
                    line=line.replace("@path/application.wadl",url+"application.wadl");
                }
                if(line.contains("@path/assessment")){
                    line=line.replace("@path/assessment",url+"assessment");
                }

                sb.append( line );
                sb.append( '\n' );
            }
            return sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
            response.status(401);
            return e.getMessage();
        }
    }
}
