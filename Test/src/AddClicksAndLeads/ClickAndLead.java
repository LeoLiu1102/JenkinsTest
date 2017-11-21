package AddClicksAndLeads;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.apache.http.HttpResponse;
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

public class ClickAndLead {
	
	private CookieStore cookieStore = null;
	private HttpContext context = null;
	String testLink;
	private int cookieVersion;
	private String cookieName;
	private String cookieValue;
	private String cookieDomain;
	private String cookiePath;
	private String cookieID;
	
	public ClickAndLead(String testLink){
		this.testLink = testLink;
	}

	public void clicks(){
		CloseableHttpClient client = HttpClients.createDefault();
		
		cookieStore = new BasicCookieStore();
		context = new BasicHttpContext();
		context.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		
		HttpGet request = new HttpGet(testLink);
		try {
			
            HttpResponse response = null;
			response = client.execute(request,context);
			while(response==null){
				try {
					Thread.sleep(200);
					System.out.print("..");
				} catch (InterruptedException e) {
					System.out.println("Problem in Thread Sleep");
				}
			}
			
		} catch (ClientProtocolException e) {
			System.out.println("Client Protocol Exception in clicks()");
		} catch (IOException e) {
			System.out.println("IO Exception in clicks()");
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
            HttpGet httpget = new HttpGet(testLink);
            
            HttpResponse response = null;
            response = client.execute(httpget);
			while(response==null){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					System.out.println("Problem in Thread Sleep");
				}
			}
        }catch(IOException e ){
        	System.out.println("GetCookies Catch IO error");
        }finally{
			try {
				client.close();
			} catch (IOException e) {
				System.out.println("GetCookies finally IO error");
			}
        }
	}
	
	public void convertLead(String hashCode, String cookieID){
		
		String domain = testLink.replaceAll("\\/mt\\/.*", "").replaceAll("http:\\/\\/", "");
		String leadFireLink;
		
		if(domain.contains("qa-dev") || domain.contains("intranet")){
			leadFireLink = "http://" + domain + "/lead/" + hashCode + "/&cookieid=" + cookieID; 
		}
		else{
			leadFireLink = "http://leads." + domain + "/lead/" + hashCode + "/&cookieid=" + cookieID; 
		}
		
		CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(leadFireLink);
        try {
			HttpResponse response = client.execute(httpget);
		} catch (ClientProtocolException e) {
			System.out.println("Client Protocol Exception in convertLead()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception in convertLead()");
			e.printStackTrace();
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
    
    public String getCookieID(){
    	return cookieID;
    }
    

}