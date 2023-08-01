package Api;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;

public class UtilApi {

     public Map<String, List<String>> getRequestParameters(final URI requestUri, Charset charset){
       
        final Map<String, List<String>> requestParametersMap = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();

        if (requestQuery != null) {

            final String[] rawRequestParameters = requestQuery.split("&");

            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0],charset);
                requestParametersMap.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1],charset) : null;
                requestParametersMap.get(requestParameterName).add(requestParameterValue);
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

}   
