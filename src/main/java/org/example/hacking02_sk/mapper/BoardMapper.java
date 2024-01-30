package org.example.hacking02_sk.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.example.hacking02_sk.model.Board;

@Mapper
public interface BoardMapper {
    List<Board> list();
}
