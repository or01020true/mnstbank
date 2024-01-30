package org.example.hacking02_sk.controller;

import org.example.hacking02_sk.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("board")
public class BoardController {
    @Autowired
    BoardMapper boardMapper;

    @RequestMapping("list")
    String boardList(Model model){
        System.out.println(boardMapper.list());
        model.addAttribute("list", boardMapper.list());
        return "board/boardList";
    }
}
