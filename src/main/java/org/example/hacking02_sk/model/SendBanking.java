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
        return Encrypt.encryptAES(this.myacc);
    }

    public String getMysendacc() {
        return Encrypt.encryptAES(this.mysendacc);
    }

    public String getMyaccDec() {
        return Encrypt.decryptAES(this.myacc);
    }

    public String getMysendaccDec() {
        return Encrypt.decryptAES(this.mysendacc);
    }
}
