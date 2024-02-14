package org.example.hacking02_sk.model;

import java.util.Date;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.example.hacking02_sk.service.Encrypt;

@Alias("sendbank")
@Data
public class SendBanking {
    int myaccin, myaccout, myaccbalance;
    String myacc, myaccioname, mysendacc, myaccmemo, mysendbank, csrfToken, myaccpw;
    Date myaccdate;

    public String getMyacc() {
        System.out.println("(hy debug) SendBanking class getMyacc() -> myacc = " + myacc);
        return myacc;
    }

    public String getDecMyacc() {
        System.out.println("(hy debug) SendBanking class getdecMyacc() -> Encrypt.decryptAES(this.myacc) = " + Encrypt.decryptAES(this.myacc));
        return Encrypt.decryptAES(this.myacc);
    }

    public String getMysendacc() {
        System.out.println("(hy debug) SendBanking class getMysendacc() -> mysendacc = " + mysendacc);
        return mysendacc;
    }

    public String getDecMysendacc() {
        System.out.println("(hy debug) SendBanking class getdecMysendacc() -> Encrypt.decryptAES(this.mysendacc) = " + Encrypt.decryptAES(this.mysendacc));
        return Encrypt.decryptAES(this.mysendacc);
    }
}
