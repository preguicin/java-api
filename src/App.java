import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;

import Api.Constants;
import Api.StatusCode;
import Api.UtilApi;
import User.Person;
import User.PersonManager;

import com.sun.net.httpserver.Headers;

public class App {

    //Server Especifications 
    private static final String hostName = "localhost";
    private static final int port = 8000;   
    private static final int backlog = 1;

    //Http Methods
    private static final String methodGet = "GET";
    private static final String methodPost = "POST";
    private static final String methodPut = "PUT";
    private static final String methodDelete = "DELETE";
    private static final String methodOptions = "OPTIONS";
    //Allowed Http Methods by Domain
    private static final String allowedMethodsJson = methodGet + "," + methodOptions;
     private static final String allowedMethodPerson = methodGet + "," + methodPost + "," + methodPut+ "," +methodDelete;

    //Utility classes
    private static final UtilApi util = new UtilApi();
    private static final Constants constants = new Constants();
    private static PersonManager personManager = new PersonManager();

    public static void main(String[] args ) throws Exception{
        final HttpServer server =  HttpServer.create(new InetSocketAddress(hostName, port), backlog);

        server.createContext("/api/person", ex ->{
            try{
                final Headers headers = ex.getResponseHeaders();
                final String requestMethod = ex.getRequestMethod().toUpperCase();
                
                if(requestMethod.equals("POST")){
                    //Getting all query items
                    Map<String,List<String>> queryParam = util.getRequestParameters(ex.getRequestURI(), constants.charset);
                    List<String> tempStringList = queryParam.get("name");
                    for(String s : tempStringList){
                        personManager.addPerson(s);
                    }
                    headers.set(constants.headerAllow, allowedMethodPerson);
                    ex.sendResponseHeaders(StatusCode.CREATED.getCode(), constants.noResponseLenght);
                }
                else if(requestMethod.equals("DELETE")){
                    Map<String,List<String>> queryParam = util.getRequestParameters(ex.getRequestURI(), constants.charset);
                    List<String> tempStringList = queryParam.get("name");
                    boolean isPersonDeleted = personManager.deletePerson(tempStringList.get(0));

                    if(isPersonDeleted == false){
                        headers.set(requestMethod, requestMethod);
                        ex.sendResponseHeaders(port, backlog);
                    }else{

                    }                    
                
                }
                else if(requestMethod.equals("GET")){

                }


            }

            finally{
                ex.close();
            }
        });

        server.createContext("/api/json", ex -> {
            try{
                final Headers headers = ex.getResponseHeaders();
                final String requestMethod = ex.getRequestMethod().toUpperCase();

                //Request Methods
                if(requestMethod.equals("GET")){
                    final String responseBodyJson = "{\"email\": \"example@com\", \"name\": \"Example\"}";

                    headers.set(constants.headerContentType, String.format("application/json; charset=%s", constants.charset));
                    final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                    ex.sendResponseHeaders(StatusCode.OK.getCode(), rawResponseBodyJson.length);
                    ex.getResponseBody().write(rawResponseBodyJson);
                }
                else if(requestMethod.equals("OPTIONS")){
                    headers.set(constants.headerAllow, allowedMethodsJson);
                    ex.sendResponseHeaders(StatusCode.OK.getCode(), constants.noResponseLenght);
                }else{
                    headers.set(constants.headerAllow, allowedMethodsJson);
                    ex.sendResponseHeaders(StatusCode.METHOD_NOT_ALLOWED.getCode(), constants.noResponseLenght);
                }

            }finally{
                ex.close();
            }
        });
        server.start();
    }
}
