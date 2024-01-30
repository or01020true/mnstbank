package org.example.hacking02_sk.model;

import lombok.Data;
import java.util.Date;
import org.apache.ibatis.type.Alias;

@Alias("board")
@Data
public class Board {
    private String myid;
    private String myip;
    private String mysubject;
    private String mycontent;
    private String mytext;
    private String myfilepath;
}
