package AddClicks;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
	
	private String htmlFile;
	
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
			
			HttpResponse response = client.execute(request,context);
			
		} catch (ClientProtocolException e) {
			System.out.println("Client Protocol Exception in clicks()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception in clicks()");
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
            HttpGet httpget = new HttpGet(testLink);
            
            HttpResponse response = client.execute(httpget);

        }catch(IOException e ){
        	System.out.println("GetCookies Catch IO error");
        }finally{
			try {
				client.close();
			} catch (IOException e) {
				System.out.println("GetCookies finally IO error");
				e.printStackTrace();
			}
        }
	}
	
	public void thirdPartyLead(CookieStore cookieStore, String pixelType, String hashCode, String aid) throws IOException{
		
		String domain = testLink.replaceAll("\\/mt\\/.*", "").replaceAll("http:\\/\\/", "");
		
		this.writeToFile(pixelType, domain, hashCode, aid);
		
		CloseableHttpClient client = null;
		String testLink = "http://localhost:80/pixel-test.asp";

        try {
        	
            setCookieStore();
        	
        	client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
            HttpGet httpget = new HttpGet(testLink);
            
            HttpResponse response = client.execute(httpget);
           // printResponse(response);
            
            HttpEntity entity = response.getEntity();  
            String responseString = EntityUtils.toString(entity);  
            
            Pattern pattern = Pattern.compile("http.+\"");
            Matcher matcher = pattern.matcher(responseString);
            
            String templink = null;
            if(matcher.find()){
            	templink = matcher.group(0).replace("\"", "");
            }
            
            System.out.println(templink);
            httpget = new HttpGet(testLink);
            response = client.execute(httpget);

        }catch(IOException e ){
        	e.printStackTrace();
        	System.out.println("GetCookies Catch IO error");
        }finally{
			try {
				client.close();
			} catch (IOException e) {
				System.out.println("GetCookies finally IO error");
				e.printStackTrace();
			}
        }
	}
	
	public void s2sLead(String hashCode, String cookieID){
		
		String domain = testLink.replaceAll("\\/mt\\/.*", "").replaceAll("http:\\/\\/", "");
		String leadFireLink;
		
		if(domain.contains("qa-dev")){
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
			System.out.println("Client Protocol Exception in s2sLead()");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception in s2sLead()");
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
    
    private void writeToFile(String pixelType, String domain, String hashCode, String aid){
    	
    	htmlFile = "C:/inetpub/wwwroot/pixel-test.asp";
    	String content = "";
    	
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			
			switch(pixelType.trim().toLowerCase()){
			
			case "image" :
				content = "<!--- Affiliate Pixel --->\n\n\n" + 
									"<!--- Our Pixel --->\n" + 
										"<img alt=\"\" style=\"display:none;\" src=\"http://" + domain + "/track/" + hashCode + "/Image-pixel\" />";
				break;
				
			case "script" :
				content = "<!--- Affiliate Pixel --->\n\n\n" + 
									"<!--- Our Pixel --->\n" + 
										"<script type=\"text/javascript\" src=\"http://" + domain + "/third/" + hashCode + "/Script-pixel\"></script>";
				break;
				
			case "iframe" :
				content = "<!--- Affiliate Pixel --->\n\n\n" + 
									"<!--- Our Pixel --->\n" + 
										"<iframe style=\"display:none;\" src=\"http://" + domain + "/frame/" + hashCode + "/Iframe-pixel\"></iframe>";
				break;

			case "cscr" :
				content = "<html><head><title>Pixel Test Result</title></head><body>\n\n"
									+ "<!--- Our Pixel --->\n"
										+ "<script type=\"text/javascript\" src=\"http://" + domain + "/cscr/" + hashCode + "/CSCR-pixel&aid=" + aid + "\"></script>\n\n\n\n"
											+ "</body></html>";
				break;
				
			case "cfrm" :
				content = "<html><head><title>Pixel Test Result</title></head><body>\n\n"
									+ "<!--- Our Pixel --->\n"
										+ "<iframe style=\"display:none;\" src=\"http://" + domain + "/cfrm/" + hashCode + "/CFRM-pixel&aid=" + aid + "\"></iframe>\n\n\n\n"
											+ "</body></html>";
				break;				
			}

			fw = new FileWriter(htmlFile);
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    }
    
    public static void printResponse(HttpResponse httpResponse)  
            throws ParseException, IOException {  
        //  Get response message entity
        HttpEntity entity = httpResponse.getEntity();  
        // response status  
        System.out.println("status:" + httpResponse.getStatusLine());  
        System.out.println("headers:");  
        HeaderIterator iterator = httpResponse.headerIterator();  
        while (iterator.hasNext()) {  
            System.out.println("\t" + iterator.next());  
        }  
        // NULL or not 
        if (entity != null) {  
            String responseString = EntityUtils.toString(entity);  
            System.out.println("response length:" + responseString.length());  
            System.out.println("response content:"  
                    + responseString.replace("\r\n", ""));
            
            Pattern pattern = Pattern.compile("http.+\"");
            Matcher matcher = pattern.matcher(responseString);
            if(matcher.find()){
            	System.out.println(matcher.group(0).replace("\"", ""));
            }
            
        }  
    }  

}