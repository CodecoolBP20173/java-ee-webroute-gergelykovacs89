package com.codecool.webroute;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class TestServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new Handler());
        server.setExecutor(null);
        server.start();
    }












}
