package com.codecool.webroute;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public abstract class MyHandler implements HttpHandler {


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
}