import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;

import Api.UtilApi;

import com.sun.net.httpserver.Headers;

public class App {

    //Server Especifications 
    private static final String hostName = "localhost";
    private static final int port = 8000;
    private static final int backlog = 1;

    //Requisitions Headers
    private static final String headerAllow = "Allow";
    private static final String headerExpect = "Expect";
    private static final String headerContentType = "Content-Type";

    private static final Charset charset = StandardCharsets.UTF_8;

    //Status Codes
    private static final int statusOk = 200;
    private static final int statusMethodNotAllowed = 405;

    private static final int noResponseLenght = -1;

    //Http Methods
    private static final String methodGet = "GET";
    private static final String methodOptions = "OPTIONS";
    private static final String allowedMethods = methodGet + "," + methodOptions;

    //Utility classes
    private static final UtilApi util = new UtilApi();

    public static void main(String[] args ) throws Exception{
        final HttpServer server =  HttpServer.create(new InetSocketAddress(hostName, port), backlog);

        server.createContext("/api/param", ex ->{
            try{
                final Headers headers = ex.getResponseHeaders();
                final String requestMethod = ex.getRequestMethod().toUpperCase();
                
                if(requestMethod.equals("GET")){
                    //Getting all query items
                    Map<String,List<String>> queryParam = util.getRequestParameters(ex.getRequestURI(), charset); 
                    
                    List<String> tempStringList = queryParam.get("name");

                    if(tempStringList != null){
                        for(String s : tempStringList){
                            if(s == null || s.equals("")){
                                headers.set(headerExpect,"Invalid Parameter");
                                ex.sendResponseHeaders(404, noResponseLenght);
                                break;
                            }  
                        }
                    }else{
                        headers.set(headerExpect,"Invalid Parameter");
                        ex.sendResponseHeaders(404, noResponseLenght);
                    }

                }

            }finally{
                ex.close();
            }
        });

        server.createContext("/api/json", ex -> {
            try{
                final Headers headers = ex.getResponseHeaders();
                final String requestMethod = ex.getRequestMethod().toUpperCase();

                //Request Methods
                if(requestMethod.equals("GET")){
                    final String responseBody = "{\"email\": \"example@com\", \"name\": \"Example\"}";

                    headers.set(headerContentType, String.format("application/json; charset=%s", charset));
                    final byte[] rawResponseBody = responseBody.getBytes(charset);
                    ex.sendResponseHeaders(statusOk, rawResponseBody.length);
                    ex.getResponseBody().write(rawResponseBody);
                }
                else if(requestMethod.equals("OPTIONS")){
                    headers.set(headerAllow, allowedMethods);
                    ex.sendResponseHeaders(statusOk,noResponseLenght);
                }else{
                    headers.set(headerAllow, allowedMethods);
                    ex.sendResponseHeaders(statusMethodNotAllowed,noResponseLenght);
                }

            }finally{
                ex.close();
            }
        });
        server.start();
    }
}
