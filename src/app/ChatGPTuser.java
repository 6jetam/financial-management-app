package app;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPTuser {

	private static final String API_KEY =ConfigLoader.getProperty("API_KEY");
	private static final String MODEL_NAME = ConfigLoader.getProperty("MODEL_NAME");
	private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/"
	        + MODEL_NAME
	        + ":generateContent?key="
	        + API_KEY;
	


	private static final OkHttpClient client = new OkHttpClient.Builder()
	        .connectTimeout(30, TimeUnit.SECONDS)  // Čas na spojenie so serverom
	        .readTimeout(60, TimeUnit.SECONDS)    // Čas na čakanie na odpoveď
	        .writeTimeout(60, TimeUnit.SECONDS)   // Čas na odoslanie požiadavky
	        .build();
	
	
	public static String sendMessage(String message) throws IOException {
		OkHttpClient client = new OkHttpClient();
		MediaType JSON = MediaType.get("application/json; charset=utf-8");
		
		//json request pre gemini
		String jsonReq = "{"
                + "\"contents\": [{\"role\": \"user\", \"parts\": [{\"text\": \"" + escapeJson(message) + "\"}]}]"
                + "}";
		
		  System.out.println("Sending JSON to API: " + jsonReq); 
		
		RequestBody body = RequestBody.create(jsonReq	, MediaType.get("application/json; charset=utf-8"));
		Request request = new Request.Builder()
				.url(API_URL)
				.post(body)
				.build();
				
		
		 Response response = client.newCall(request).execute();
		 String responseBody = response.body().string();
		 
		 System.out.println("API response: " + responseBody);  // Debug výstup
		 if(!response.isSuccessful()) {
			 return "Error: API  returned" + response.code() + " - " + response.message();
		 }
		 
		 System.out.println("Použitý API kľúč: " + API_KEY);
	     return parseResponse(responseBody);
	}
	
	private static String parseResponse(String responseBody) {
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(responseBody);
		
		if (jsonNode.has("candidates") && jsonNode.get("candidates").size() > 0) {
            JsonNode textNode = jsonNode.get("candidates").get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");
            
            if (!textNode.isMissingNode()) {
                String rawResponse = textNode.asText();
		
                String formattedResponse = rawResponse
                        .replaceAll("\\n\\n", "\n") // Odstráni viacnásobné prázdne riadky
                        .replaceAll("\\n", "\n\n") // Pridá medzery medzi odseky
                        .replaceAll("\\*", "") // Odstráni Markdown hviezdičky (*)
                        .trim(); 

                return formattedResponse;
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error: No valid response from Gemini AI.";
    }
	
	
	private static String escapeJson(String text) {
        return text.replace("\"", "\\\"").replace("\n", "\\n");
    } 
	
}
