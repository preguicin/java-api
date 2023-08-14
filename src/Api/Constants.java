package Api;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {

    //Requisitions Headers
    public final String headerAllow = "Allow";
    public final String headerExpect = "Expect";
    public final String headerContentType = "Content-Type";

    //Charset
    public final Charset charset = StandardCharsets.UTF_8;
    
    //Response
    public final int noResponseLenght = -1;
}
