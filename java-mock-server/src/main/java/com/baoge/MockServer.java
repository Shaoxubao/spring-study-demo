package com.baoge;

import org.mockserver.integration.ClientAndServer;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockServer {
    public static void main(String[] args) throws InterruptedException {
        ClientAndServer mockServer = ClientAndServer.startClientAndServer(10080);

        mockServer.when(request().withMethod("GET").withPath("/hello"))
                .respond(response().withStatusCode(200).withBody("hello, world."));

        mockServer.when(request().withMethod("POST").withPath("/ami/ms01-00-605/sys-auth/sysLogin/v1")
//                                .withBody("{username: 'ESP_SYS205099', password: '0438c1c25b3c46d904698c58fc5f38e1aa6c3c5fb8d140f5b3f4f8792e380110a479f88a7ff859245409869d7f1f121fbb8eee416afbe1afd34d1d4563e62021843f46a38a33bdf29f64510364015d6ba6c323d97aaaf3a93b91887ac2097870f6a9025de80457eb23'}")
                ).respond(response().withStatusCode(200)
                                .withBody("{ \"result\": \"200\", \"token\": \"12345667890\" }"));

        System.out.println("MockServer started!");
        Thread.sleep(1000000000000000L);
    }
}
