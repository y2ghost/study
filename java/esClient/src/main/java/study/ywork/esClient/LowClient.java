package study.ywork.esClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import java.io.IOException;

// REST client封装
public class LowClient {
    public static void main(String[] args) {
        RestClient client = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();

        try {
            Request request = new Request("GET", "/mybooks/_doc/1");
            Response response = client.performRequest(request);

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + response.getStatusLine());
            } else {
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity);
                System.out.println(responseBody);
            }
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 释放连接
            try {
                client.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
