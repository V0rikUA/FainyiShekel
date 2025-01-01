package com.liorlakhmann.timetracking.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Smart Librarian API",
                version = "v0.1"
        ),
        servers = @Server(url = "http://localhost:8070")
)
public class OpenApiDocsFileGenerator {

    @EventListener
    public void generateOpenApiDocs(ContextRefreshedEvent event) {
        try (var readableByteChannel = Channels.newChannel(
                new URL("http://localhost:8070/api-docs").openStream());
             var fileOutputStream = new FileOutputStream("openApi.json")) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
