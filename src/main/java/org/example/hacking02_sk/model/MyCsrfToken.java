package org.example.hacking02_sk.model;

import java.util.UUID;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class MyCsrfToken {
    private String csrfToken;

    public MyCsrfToken() {
        UUID uuid = UUID.randomUUID();
        this.csrfToken = uuid.toString();
    }
}
