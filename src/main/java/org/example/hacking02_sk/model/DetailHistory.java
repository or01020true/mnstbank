package org.example.hacking02_sk.model;

import java.util.Date;
import lombok.Data;

@Data
public class DetailHistory {
    String keyword, predate, postdate, breakdown, deal;
    int acc;
}
