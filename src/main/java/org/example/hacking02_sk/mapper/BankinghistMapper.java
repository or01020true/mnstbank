package org.example.hacking02_sk.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.example.hacking02_sk.model.DetailHistory;
import org.example.hacking02_sk.model.SendBanking;

@Mapper
public interface BankinghistMapper {
    List<SendBanking> history(DetailHistory detailHistory);
    List<SendBanking> searchmemo(String keyword);
    SendBanking sendbanking(int myacc);
    int insert(SendBanking sendBanking);
}
