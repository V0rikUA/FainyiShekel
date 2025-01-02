package com.liorlakhmann.timetracking.configurations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OpenApiDocsFileGeneratorTest {

    private OpenApiDocsFileGenerator openApiDocsFileGenerator;

    @Mock
    private ContextRefreshedEvent event;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        openApiDocsFileGenerator = new OpenApiDocsFileGenerator();
    }

    @Test
    public void testGenerateOpenApiDocsFileCreated() throws IOException {
        String mockUrl = "http://localhost:8070/api-docs";
        String mockResponse = "{\"openapi\":\"3.0.1\",\"info\":{\"title\":\"Mock API\",\"version\":\"1.0.0\"}}";

        URL url = mock(URL.class);
        when(url.openStream()).thenReturn(new ByteArrayInputStream(mockResponse.getBytes()));
        File tempFile = Files.createTempFile("openApi", ".json").toFile();
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

        var mockChannel = Channels.newChannel(new ByteArrayInputStream(mockResponse.getBytes()));
        try (var readableByteChannel = mockChannel) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }

        assertTrue(tempFile.exists());
        String fileContent = Files.readString(tempFile.toPath());
        assertEquals(mockResponse, fileContent);

        tempFile.deleteOnExit();
    }

    @Test
    public void testGenerateOpenApiDocsExceptionHandled() {
        String invalidUrl = "http://localhost:8070/invalid-api-docs";

        assertDoesNotThrow(() -> openApiDocsFileGenerator.generateOpenApiDocs(event));
    }
}
