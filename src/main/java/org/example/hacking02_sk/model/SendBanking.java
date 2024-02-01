package org.example.hacking02_sk.model;

import java.util.Date;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("sendbank")
@Data
public class SendBanking {
    int myacc, myaccin, myaccout, myaccbalance, mysendacc;
    String myaccioname, myaccmemo, mysendbank, csrfToken, myaccpw;
    Date myaccdate;
}
