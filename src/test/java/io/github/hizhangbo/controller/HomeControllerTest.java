package io.github.hizhangbo.controller;

import com.googlecode.protobuf.format.JsonFormat;
import io.github.hizhangbo.App;
import io.github.hizhangbo.model.UserProto;
import io.github.hizhangbo.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
class HomeControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        //构造MockMvc
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void user() throws IOException, URISyntaxException {
        URL url = new URL("http://localhost:8081/user");
        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
        urlc.setDoInput(true);
        urlc.setDoOutput(true);
        urlc.setRequestMethod("POST");
        urlc.setRequestProperty("Accept", "application/x-protobuf");
        urlc.setRequestProperty("Content-Type", "application/x-protobuf");

        UserProto.User.Builder userBuilder = UserProto.User.newBuilder();
        UserProto.User user = userBuilder.build();

        OutputStream outputStream = urlc.getOutputStream();
        user.writeTo(outputStream);
        outputStream.flush();

        InputStream inputStream = urlc.getInputStream();
        UserProto.User result = user.newBuilder().mergeFrom(inputStream).build();
        System.out.println(result.toBuilder());
    }

    @Test
    public void test1() throws IOException, URISyntaxException {
        URI uri = new URI("http", null, "localhost", 8081, "/user", "", null);
        HttpPost request = new HttpPost(uri);
//        UserProto.User.Builder builder = UserProto.User.newBuilder();
//        builder.setUsername("tom");
//        builder.setPassword("123456");
        HttpResponse response = HttpUtils.doPost(request, null);
        UserProto.User user = UserProto.User.parseFrom(response.getEntity().getContent());
        System.out.println(user.toString());
    }

    @Test
    public void test2() throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = new URI("http", null, "localhost", 8081, "/user", "", null);
        HttpPost post = new HttpPost(uri);
        post.setHeader("Content-Type", "application/x-protobuf");
        post.setHeader("Accept", "application/x-protobuf");

        HttpResponse response = httpClient.execute(post);
        System.out.println(response.getStatusLine().getStatusCode());
    }

    @Test
    public void test3() throws URISyntaxException, IOException {

        URI uri = new URI("http", null, "localhost", 8081, "/user", "", null);
        HttpPost post = new HttpPost(uri);
        post.setHeader("Content-Type", "application/octet-stream;charset=utf-8");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(post);
        System.out.println(response.getStatusLine().getStatusCode());
    }

    @Test
    public void test4() throws IOException {
        URL url = new URL("http://localhost:8081/user");
//        byte[] bytes =
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/x-protobuf");
        conn.setRequestProperty("Accept", "application/x-protobuf");
        conn.connect();
        OutputStream out = conn.getOutputStream();
//        out.write(bytes);
        out.flush();
        out.close();
    }

    @Test
    public void index() throws Exception {
        mvc.perform(get("/").contentType(MediaType.APPLICATION_JSON)).andDo(print());
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet request = new HttpGet("/");
//        HttpResponse httpResponse = httpClient.execute(request);
//        InputStream inputStream = httpResponse.getEntity().getContent();
//        String result = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
//        System.out.println(result);
    }

    @Test
    public void executeHttpRequest() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:8081/user");
        HttpResponse httpResponse = httpClient.execute(request);
        InputStream inputStream = httpResponse.getEntity().getContent();
        String s = convertProtobufMessageStreamToJsonString(inputStream);
        System.out.println(s);
    }

    private String convertProtobufMessageStreamToJsonString(InputStream protobufStream) throws IOException {
        JsonFormat jsonFormat = new JsonFormat();
        UserProto.User user = UserProto.User.parseFrom(protobufStream);
        return jsonFormat.printToString(user);
    }
}