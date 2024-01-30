package org.example.hacking02_sk.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.example.hacking02_sk.model.Banking;
import org.example.hacking02_sk.model.SendBanking;

@Mapper
public interface BankingMapper {
    List<Banking> myid(String myid);
    Banking myacc(int myacc);
    int addmoney(SendBanking sendBanking);
    int submoney(SendBanking sendBanking);
}
