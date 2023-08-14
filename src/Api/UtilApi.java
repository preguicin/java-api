package Api;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;


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

}   
