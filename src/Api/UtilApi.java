package Api;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import User.Pet;


public class UtilApi {

     public Map<String, String> getRequestParameters(final URI requestUri, Charset charset){
       
        final Map<String, String> requestParametersMap = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();

        if (requestQuery != null) {

            final String[] rawRequestParameters = requestQuery.split("&");

            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0],charset);
                final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1],charset) : null;
                requestParametersMap.putIfAbsent(requestParameterName, requestParameterValue);
            }
        }

        return requestParametersMap; 
     }


      private String decodeUrlComponent(final String urlComponent, Charset charset) {
        try {
            return URLDecoder.decode(urlComponent, charset.name());
        } catch (final UnsupportedEncodingException ex) {
            throw new InternalError(ex);
        }
    }

    public String formatPetsString(ArrayList<Pet> pets){
        String tempString = "{\"Pets\":{";
        for(int i = 0; i < pets.size(); i++){
            Pet p = pets.get(i);
            if(i < pets.size() - 1){
                 tempString +=  String.format("\"Pet\": { id: %s, \"name\" \"%s\" },", p.getId(), p.getName());
            }else{
                 tempString +=  String.format("\"Pet\": { id: %s, \"name\" \"%s\" }", p.getId(), p.getName());
            }
           
        }
        return tempString += "}}";
    }

}   
