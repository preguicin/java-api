import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;
import com.sun.net.httpserver.HttpServer;

import Api.Constants;
import Api.StatusCode;
import Api.UtilApi;
import User.Person;
import User.PersonManager;
import User.Pet;

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
    private static final String allowedMethodPerson = methodGet + "," + methodPost + "," + methodPut+ "," + methodDelete+ "," + methodOptions;

    //Utility classes
    private static final UtilApi util = new UtilApi();
    private static final Constants constants = new Constants();
    private static PersonManager personManager = new PersonManager();

    //Utility Variables
    private static int index = 0;

    public static void main(String[] args ) throws Exception{
        final HttpServer server =  HttpServer.create(new InetSocketAddress(hostName, port), backlog);

        server.createContext("/api/person", ex ->{
            try{

                final Headers headers = ex.getResponseHeaders();
                final String requestMethod = ex.getRequestMethod().toUpperCase();
                Map<String,String> requestParamMap = util.getRequestParameters(ex.getRequestURI(), constants.charset);

                if(requestMethod.equals("POST")){
                    String personAdr = requestParamMap.get("adr");
                    String personName = requestParamMap.get("name");

                    if(personAdr == null || personAdr.equals("")){
                        personAdr = "Anonymous";
                    }

                    if(personName != null){
                        //Adding Person 
                        personManager.addPerson(personName, personAdr, index);
                        index++;
                        //Message
                        final String responseBodyJson = String.format("{\"Inserted\": \"True\", \"Person\": {\"name\": \"%s\", \"adr\": \"%s\"} }", personName, personAdr);
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.CREATED.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                        
                    }else{
                        final String responseBodyJson = String.format("{\"Inserted\": \"False\", \"Person\": {\"name\": \"Name must be inserted\", \"adr\": \"Address is optional\" }");
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }

                }
                else if(requestMethod.equals("DELETE")){
                    boolean IsPersonDeleted = false; 

                    int id = requestParamMap.get("id") != null ? Integer.parseInt(requestParamMap.get("id")) : Integer.MIN_VALUE;
                    IsPersonDeleted = personManager.deletePerson(id);
                    
                    if(IsPersonDeleted == true){
                        final String responseBodyJson = String.format("{\"Deleted\": \"True\"}");
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.OK.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }else{
                        final String responseBodyJson = String.format("{\"Deleted\": \"False\"}");
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }
                 
                    
                }
                else if(requestMethod.equals("GET")){
                    
                    int id = requestParamMap.get("id") != null ? Integer.parseInt(requestParamMap.get("id")) : Integer.MIN_VALUE;
                    Person tempPerson = personManager.getPerson(id);
                  
                    if(tempPerson != null){
                        final String responseBodyJson = String.format("{\"Person\": {\"name\": \"%s\", \"adr\": \"%s\", id: %s, \"Pet\":{ \"name\": \"Pet Name\" }} }", tempPerson.getName() , tempPerson.getAddress(), tempPerson.getId());
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.OK.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }else{
                        final String responseBodyJson = String.format("{\"Person\": {\"name\": \"Empty name\", \"adr\": \"Empty adr\", id: -999, \"Pet\":{ \"name\": \"Empty Pet Name\" } } }");
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }
                    
                }
                else if(requestMethod.equals("OPTIONS")){
                    headers.set(constants.headerAllow, allowedMethodPerson);
                    ex.sendResponseHeaders(StatusCode.OK.getCode(), constants.noResponseLenght);
                }
                else if(requestMethod.equals("PUT")){
                    int id = requestParamMap.get("id") != null ? Integer.parseInt(requestParamMap.get("id")) : Integer.MIN_VALUE;
                    String personName = requestParamMap.get("name");
                    String personAdr = requestParamMap.get("adr");

                    //Check if person exist
                    Person person = personManager.getPerson(id);

                    if(id == Integer.MIN_VALUE || person == null){
                        final String responseBodyJson = String.format("{\"Person\": {\"name\": \"Invalid Id\", \"adr\": \"Invalid Id\", id: -999, \"Pet\":{ \"name\": \"Invalid Id\" } } }");
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }
                    else{
                        if(personName != null){
                            person.setName(personName);
                        }
                        if(personAdr != null){
                            person.setAddress(personAdr);
                        }
                        final String responseBodyJson = String.format("{\"Person\": {\"name\": \"%s\", \"adr\": \"%s\", \"id\": \"%s\", \"Pet\":{ \"name\": \"Pet Name\" } } }", person.getName(), person.getAddress(), person.getId());
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.OK.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }
                    

                }
            }
            finally{
                ex.close();
            }
        });

        server.createContext("/api/person/pet", ex ->{
            final Headers headers = ex.getResponseHeaders();
            final String requestMethod = ex.getRequestMethod().toUpperCase();
            Map<String,String> requestParamMap = util.getRequestParameters(ex.getRequestURI(), constants.charset);
            
            if(requestMethod.equals("GET")){
                int personId = requestParamMap.get("id") != null ? Integer.parseInt(requestParamMap.get("id")) : Integer.MIN_VALUE;

                ArrayList<Pet> tempPets = personManager.getPets(personId);
                
                if(tempPets != null){
                    String tempJsonString = util.formatPetsString(tempPets);
                    final byte[] rawResponseBodyJson = tempJsonString.getBytes(constants.charset);
                    headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                    ex.sendResponseHeaders(StatusCode.OK.getCode(), rawResponseBodyJson.length);
                    ex.getResponseBody().write(rawResponseBodyJson);

                }else{
                    final String responseBodyJson = String.format("{\"Pets\":  {\"Pet\": {id: -999, \"name\": \"Pet Id invalid\"}}");
                    final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                    headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                    ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                    ex.getResponseBody().write(rawResponseBodyJson);
                }
            }
            else if(requestMethod.equals("POST")){
                String petName = requestParamMap.get("name");
                int personId = requestParamMap.get("id") != null ? Integer.parseInt(requestParamMap.get("id")) : Integer.MIN_VALUE;
                
                if(petName != null){

                    boolean DoesPersonExist = personManager.addPet(personId, petName);
                    Person person = personManager.getPerson(personId);

                    if(DoesPersonExist != false && person != null){
                        final String responseBodyJson = String.format("{\"Pet Added\": \"True\", \"Person\": {\"name\": \"%s\", \"adr\": \"%s\",id: %s, \"Pet\":{ id: %s,\"name\": \"%s\" } } }", person.getName(),person.getAddress(),person.getId(), person.getLatestPet().getId(), person.getLatestPet().getName());
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.CREATED.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }else{
                        final String responseBodyJson = String.format("{\"Pet Added\": \"False\" }");
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }
                }else{
                    final String responseBodyJson = String.format("{\"Pet Added\": \"False\" }");
                    final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                    headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                    ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                    ex.getResponseBody().write(rawResponseBodyJson);
                }
            }
            else if(requestMethod.equals("DELETE")){
                
            int personId = requestParamMap.get("id") != null ? Integer.parseInt(requestParamMap.get("id")) : Integer.MIN_VALUE;
            int petId = requestParamMap.get("pid") != null ? Integer.parseInt(requestParamMap.get("pid")) : Integer.MIN_VALUE;
            Person per = personManager.getPerson(personId);
               
            if(per != null){
                boolean result = per.deletePet(petId);
                   
                if(result == true){
                    final String responseBodyJson = String.format("{\"Pet Deleted\": \"True\", \"Person\": {\"name\": \"%s\", \"adr\": \"%s\", \"id\": \"%s\"} }", per.getName(),per.getAddress(),per.getId());
                  
                     final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.OK.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }else{
                        final String responseBodyJson = String.format("{\"Pet Deleted\": \"False\" }");
                        final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                        headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                        ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                        ex.getResponseBody().write(rawResponseBodyJson);
                    }
                }else{
                    final String responseBodyJson = String.format("{\"Pet Deleted\": \"False\" }");
                    final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                    headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                    ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                    ex.getResponseBody().write(rawResponseBodyJson);
                }
                
            }
            else if(requestMethod.equals("PUT")){
                int personId = requestParamMap.get("id") != null ? Integer.parseInt(requestParamMap.get("id")) : Integer.MIN_VALUE;
                int petId = requestParamMap.get("pid") != null ? Integer.parseInt(requestParamMap.get("pid")) : Integer.MIN_VALUE;
                String petName = requestParamMap.get("name");

                if(petName != null){
                    Person person = personManager.getPerson(personId);
                    if(person != null){
                        Pet pet = person.getPet(petId);
                        if(pet != null){
                            pet.setName(petName);
                            final String responseBodyJson = String.format("{\"Pet Changed\": \"True\", \"Person\": {\"name\": \"%s\", \"adr\": \"%s\",id: %s, \"Pet\":{ id: %s,\"name\": \"%s\" } } }", person.getName(),person.getAddress(),person.getId(), pet.getId(), pet.getName());
                            final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                            headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                            ex.sendResponseHeaders(StatusCode.OK.getCode(), rawResponseBodyJson.length);
                            ex.getResponseBody().write(rawResponseBodyJson);

                        }else{
                            final String responseBodyJson = String.format("{\"Pet Changed\": \"False\", \"Person\": {\"name\": \"%s\", \"adr\": \"%s\",id: %s, \"Pet\":{ id: -999,\"name\": \"Invalid Pet Id\" } } }", person.getName(),person.getAddress(),person.getId());
                            final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                            headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                            ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                            ex.getResponseBody().write(rawResponseBodyJson);

                        }
                    }else{
                            final String responseBodyJson = String.format("{\"Pet Changed\": \"False\", \"Person\": {\"name\": \"Invalid Person Id\", \"adr\": \"Invalid Person Id\",id: -999, \"Pet\":{ id: -999,\"name\": \"Invalid Person Id\" } } }");
                            final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                            headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                            ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                            ex.getResponseBody().write(rawResponseBodyJson);

                    }
                }else{
                            final String responseBodyJson = String.format("{\"Pet Changed\": \"False\", \"Person\": {\"name\": \"Invalid Pet Name\", \"adr\": \"Invalid Pet Name\",id: -999, \"Pet\":{ id: -999,\"name\": \"Invalid Pet Name\" } } }");
                            final byte[] rawResponseBodyJson = responseBodyJson.getBytes(constants.charset);
                            headers.set(constants.headerContentType, String.format("application/json: charset=%s", constants.charset));
                            ex.sendResponseHeaders(StatusCode.BAD_REQUEST.getCode(), rawResponseBodyJson.length);
                            ex.getResponseBody().write(rawResponseBodyJson);
                }
            }
        });

        server.start();
    }
}
