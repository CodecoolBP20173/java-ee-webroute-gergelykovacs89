package com.codecool.webroute;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;



public class Handler extends MyHandler implements HttpHandler{

    @WebRoute(route = "/")
    public void handleIndex(HttpExchange httpExchange) throws IOException{
        handleResponse(httpExchange, "Index page test,test,test");
    }

    @WebRoute(route = "/test")
    public void handleTest(HttpExchange httpExchange) throws Exception {
        handleResponse(httpExchange, "Testing, testing, testing");
    }

    @WebRoute(route = "/test", method = WebRoute.Method.POST)
    public void handleTestwithPOST(HttpExchange httpExchange) throws Exception {
        handleResponse(httpExchange, "Test page with POST request.");
    }

    @WebRoute(route = "/test/<userid>")
    public void handleTestwithVariable(HttpExchange httpExchange) throws IOException{
        String[] routeArray = httpExchange.getRequestURI().getPath().split("/");
        handleResponse(httpExchange, "Variable passed in route: " + routeArray[routeArray.length-1]);
    }


    private void handleResponse(HttpExchange httpExchange, String s) throws IOException {
        httpExchange.sendResponseHeaders(200, s.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(s.getBytes());
        os.close();
    }


}
