package org.example.hacking02_sk.model;

import java.util.Date;
import lombok.Data;
import org.apache.ibatis.type.Alias;

@Alias("sendbank")
@Data
public class SendBanking {
    int myacc, myaccin, myaccout, myaccbalance, mysendacc, myaccpw;
    String myaccioname, myaccmemo, mysendbank;
    Date myaccdate;
}
