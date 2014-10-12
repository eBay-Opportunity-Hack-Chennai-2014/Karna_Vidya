package com.paypal.ophack.vidya.karna;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class OphackTranslator {
       
       private String srcString;         //Eg. school
       private String srcLangCode;       //Eg. en
       private String resLangCode;       //Eg. ta
       
       public OphackTranslator(){}

       public OphackTranslator(String srcString, String srcLangCode, String resLangCode) {
              super();
              this.srcString = srcString;
              this.srcLangCode = srcLangCode;
              this.resLangCode = resLangCode;
       }
       
       public String getSrcString() {
              return srcString;
       }

       public void setSrcString(String srcString) {
              this.srcString = srcString;
       }

       public String getSrcLangCode() {
              return srcLangCode;
       }

       public void setSrcLangCode(String srcLangCode) {
              this.srcLangCode = srcLangCode;
       }

       public String getResLangCode() {
              return resLangCode;
       }

       public void setResLangCode(String resLangCode) {
              this.resLangCode = resLangCode;
       }

       @Override
       public String toString() {
              return "OphackTranslator [srcString=" + srcString + ", srcLangCode=" + srcLangCode + ", resLangCode=" + resLangCode + "]";
       }
       
       public String getTranslatedText() {
              
              final String REST_URL = frameURL();
              String translatedString = EMPTY_STRING;  
              
              DefaultHttpClient httpClient = new DefaultHttpClient();
              try {
                     HttpGet getrequest = new HttpGet(REST_URL);
                     
                     // Add additional header to getRequest which accepts application/xml data
                     //getrequest.addHeader("accept", "application/json");
       
                     // Execute your request and catch response
                     HttpResponse response = httpClient.execute(getrequest);
       
                     // Check for HTTP response code: 200 = success
                     if (response.getStatusLine().getStatusCode() != 200) {
                           throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
                     }
                     String json_string = EntityUtils.toString(response.getEntity());
                     json_string = json_string.replace("[","").replace("]","").split(",")[0].replace("\"", "");
                     /*Map<String, Object> javaRootMapObject = new Gson().fromJson(json_string, Map.class);*/
                     //LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) javaRootMapObject.get("responseData");
                     //System.out.println(javaRootMapObject.toString());
                     translatedString = json_string;
                     
              } catch(Exception e) {
                     return "ERROR OCCURED WHILE FETCHING RESPONSE FROM API, cause :: " + e.getMessage();
              }
              
              return translatedString;
       }
       private String frameURL() {
              StringBuilder URL = new StringBuilder(EMPTY_STRING);
              try {
                     /*URL = URL.append(TRANSLATOR_API_URL).append(QUESTION_MARK_SEPARATOR).append(SRC_RESP_QUERY_PARAM).append(EQUALS).append(URLEncoder.encode(srcString, UTF8))
                             .append(AMPERSAND_SEPARATOR).append(SRC_RESP_LANGPAIR_PARAM).append(EQUALS).append(URLEncoder.encode(srcLangCode.toLowerCase() + PIPE + resLangCode.toLowerCase(), UTF8))
                                    .append("&key=efa50e4dfef623ca6234");*/
                     URL = URL.append("https://translate.google.co.in/translate_a/single");
                     URL = URL.append("?client=t&sl=en&tl=ta");
                     URL = URL.append("&dt=t&ie=").append(URLEncoder.encode("UTF-8", UTF8)).append("&oe=").append(URLEncoder.encode("UTF-8", UTF8));
            URL = URL.append("&q=").append(URLEncoder.encode(srcString, UTF8));

                     //System.out.println("debug :: " + URL.toString());
              } catch (UnsupportedEncodingException e) {
                     return "INVALID PARAMETERS FOUND, cause :: " + e.getMessage() ;
              }
              return URL.toString();
       }
       
       private final String TRANSLATOR_API_URL  = "http://api.mymemory.translated.net/get";
       private final String QUESTION_MARK_SEPARATOR = "?";
       private final String AMPERSAND_SEPARATOR = "&";
       private final String EMPTY_STRING               = "";
       private final String EQUALS                            = "=";
       private final String PIPE                              = "|";
       private final String SRC_RESP_QUERY_PARAM        = "q";
       private final String SRC_RESP_LANGPAIR_PARAM = "langpair";
       private final String UTF8                              = "UTF-8";
}

