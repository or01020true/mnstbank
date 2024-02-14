package org.example.hacking02_sk.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.example.hacking02_sk.service.Encrypt;

@Alias("bank")
@Data
public class Banking {
    public int mymoney;
    public String myacc, myid, mybank, csrfToken, myaccpw;
    public Date myaccregdate;
    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getMyaccregdate() {
        return sdf.format(myaccregdate);
    }

    public String getDecMyacc() {  // decrypt
        System.out.println("(hy debug) Banking class getDecMyacc() -> Encrypt.decryptAES(myacc) = " + Encrypt.decryptAES(this.myacc));
        return Encrypt.decryptAES(this.myacc);
    }
}
