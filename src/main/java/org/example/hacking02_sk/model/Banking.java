package org.example.hacking02_sk.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("bank")
@Data
public class Banking {
    int myacc, mymoney, myaccpw;
    String myid, mybank;
    Date myaccregdate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getMyaccregdate() {
        return sdf.format(myaccregdate);
    }
}
