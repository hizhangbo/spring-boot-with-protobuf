package io.github.hizhangbo.controller;

import io.github.hizhangbo.model.UserProto;
import io.github.hizhangbo.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@RunWith(SpringRunner.class)
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
    public void test3() {


    }
}