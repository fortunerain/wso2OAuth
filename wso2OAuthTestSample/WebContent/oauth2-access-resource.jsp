<%@page import="org.wso2.sample.identity.oauth2.ResponseProcessor"%>
<%@page import="org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLConnection"%>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.io.DataOutputStream" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="org.wso2.carbon.identity.oauth2.stub.dto.OAuth2TokenValidationRequestDTO_OAuth2AccessToken" %>

<%
    String accessTokenIdentifier = request.getParameter("accessToken");

    if (accessTokenIdentifier ==null || accessTokenIdentifier.trim().length()==0) {
%>
<script type="text/javascript">
    window.location = "oauth2.jsp?reset=true";
</script>
<%
    }
        try {

            String resp = new ResponseProcessor().getAndProcessResponse(accessTokenIdentifier);
            if(resp!=null && !resp.equals("")) {
            	session.setAttribute("result", resp);
                response.sendRedirect("search-results.jsp");
            } else {
                RequestDispatcher view = request.getRequestDispatcher("my-photos.jsp");
                view.forward(request, response);
            }
        } catch(Exception e) {
%>
<script type="text/javascript">
    window.location = "oauth2.jsp?reset=true&error=<%=e.getMessage()%>";
</script>
<%
        }
%>

<%!
    public static String executePost(String targetURL, String urlParameters,String accessTokenIdentifier)
    {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization","Bearer " + accessTokenIdentifier);

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

public static String executeGet(String targetURL, String urlParameters,String accessTokenIdentifier){
	try {
	    URL myURL = new URL(targetURL);
	    URLConnection myURLConnection = myURL.openConnection();
	    myURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        myURLConnection.setRequestProperty("Authorization","Bearer " + accessTokenIdentifier);
        myURLConnection.setRequestProperty("Content-Language", "en-US");
        myURLConnection.setUseCaches (false);
        myURLConnection.setDoInput(true);
        myURLConnection.setDoOutput(true);
        
	    BufferedReader br = new BufferedReader(
	                                           new InputStreamReader(myURLConnection.getInputStream()));
	    String line;
        StringBuffer response = new StringBuffer();
        while((line = br.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        br.close();
        return response.toString();
	} 
	catch (Exception e) { 
	    // new URL() failed
	    // ...
	} 
	return "";
}
%>