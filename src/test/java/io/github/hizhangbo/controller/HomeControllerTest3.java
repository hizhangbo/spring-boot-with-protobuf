package io.github.hizhangbo.controller;

import com.googlecode.protobuf.format.JsonFormat;
import io.github.hizhangbo.App;
import io.github.hizhangbo.model.UserProto;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
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

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
public class HomeControllerTest3 {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        //构造MockMvc
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
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
    public void user() throws IOException {
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