package com.codecool.webroute;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;


public class Handler implements HttpHandler{

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

    @Override
    public void handle(HttpExchange httpExchange) {
        String route = httpExchange.getRequestURI().getPath();
        String methodType = httpExchange.getRequestMethod();
        Method[] methods = this.getClass().getMethods();
        for (Method method: methods) {
            if (IsCorrectRoute(route, method.getAnnotation(WebRoute.class).route()) && method.getAnnotation(WebRoute.class).method().toString().equals(methodType)) {
                try {
                    method.invoke(this, httpExchange);
                    break;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean IsCorrectRoute(String route, String webRoute) {
        if (webRoute.contains("<")) {
            List<String> routeArray = Arrays.asList(route.split("/"));
            List<String> webRouteArray = Arrays.asList(webRoute.split("/"));
            if (routeArray.size() == webRouteArray.size()) {
                for (int i = 0; i < routeArray.size() - 1; i++) {
                    return routeArray.get(i).equals(webRouteArray.get(i)) || webRouteArray.contains("<");
                }
            }
            return false;
        } else {
            return route.equals(webRoute);
        }
    }

    private void handleResponse(HttpExchange httpExchange, String s) throws IOException {
        httpExchange.sendResponseHeaders(200, s.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(s.getBytes());
        os.close();
    }
}
