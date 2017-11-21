package AddClicks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class Click {
	
	private CookieStore cookieStore = null;
	private HttpContext context = null;
	String testURL = "http://qa-dev.dev-mt01/mt/v24443a494x2230394y203a4r2/";
	private int cookieVersion;
	private String cookieName;
	private String cookieValue;
	private String cookieDomain;
	private String cookiePath;
	private String cookieID;

	public void clicks(){
		CloseableHttpClient client = HttpClients.createDefault();
		
		cookieStore = new BasicCookieStore();
		context = new BasicHttpContext();
		context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		
		HttpGet request = new HttpGet(testURL);
		try {
			
			HttpResponse response = client.execute(request,context);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Cookie> allCookies = cookieStore.getCookies();
		for(Cookie temp:allCookies){
			if(temp.getName().equals("mt_lds")){
                cookieVersion = temp.getVersion();
                cookieName = temp.getName();
                cookieValue = temp.getValue();
                cookieDomain = temp.getDomain();
                cookiePath = temp.getPath();

                String t[]=null;
				try {
					t = URLDecoder.decode(cookieValue, "utf8").split("\"");
					cookieID = t[3];
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void clicksWithCookie(CookieStore cookieStore){
		CloseableHttpClient client = null;
        try {
        	
            setCookieStore();
        	
        	client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
            HttpGet httpget = new HttpGet(testURL);
            
            HttpResponse response = client.execute(httpget);

        }catch(IOException e ){
        	System.out.println("GetCookies Catch IO error");
        }finally{
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
		
    private void printResponse(HttpResponse response)  
            throws ParseException, IOException {  
        //  Get response message entity
        HttpEntity entity = response.getEntity();  
        // response status  
        System.out.println("status:" + response.getStatusLine());  
        System.out.println("headers:");  
        HeaderIterator iterator = response.headerIterator();  
        while (iterator.hasNext()) {  
            System.out.println("\t" + iterator.next());  
        }  
        // NULL or not 
        if (entity != null) {  
            String responseString = EntityUtils.toString(entity);  
            System.out.println("response length:" + responseString.length());  
            System.out.println("response content:"  
                    + responseString.replace("\r\n", ""));  
        }  
    }  
    
    private void setCookieStore(){
    	
    	BasicClientCookie cookie;
        cookieStore = new BasicCookieStore();  
        cookie = new BasicClientCookie(cookieName, cookieValue);
        cookie.setVersion(cookieVersion);  
        cookie.setDomain(cookieDomain);  
        cookie.setPath(cookiePath);
        cookieStore.addCookie(cookie);
    }
    
    public CookieStore getCookieStore(){
    	return cookieStore;
    }
    
    public void convertLead(){
    	
    }

}