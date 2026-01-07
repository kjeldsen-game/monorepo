package com.kjeldsen.lib;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class BaseClientApiTest {

    protected static MockWebServer mockWebServer;
    protected final InternalClientTokenProvider mockedInternalClientTokenProvider = Mockito.mock(InternalClientTokenProvider.class);

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    protected String readJsonFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        return new String(Files.readAllBytes(Paths.get(resource.getURI())));

    }
}
