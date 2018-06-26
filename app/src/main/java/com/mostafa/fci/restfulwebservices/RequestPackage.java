package com.mostafa.fci.restfulwebservices;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by FCI on 2018-06-25.
 */

public class RequestPackage {
    private String uri ;
    private String method = "GET";
    private HashMap<String,String> params = new HashMap<>() ;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public void setParam(String key, String value) {
        this.params.put(key,value);
    }

    public String getEncodeParams(){

        StringBuilder stringBuilder = new StringBuilder();
        for (String key : params.keySet()){
            String value=null;
            try {
                value = URLEncoder.encode(params.get(key),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (stringBuilder.length() > 0){
                stringBuilder.append("&");
            }
            stringBuilder.append(key +"="+ value );
        }
        return stringBuilder.toString();
    }

}
