package org.example.hacking02_sk.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.ibatis.jdbc.Null;
import org.example.hacking02_sk.mapper.BankingMapper;
import org.example.hacking02_sk.mapper.BankinghistMapper;
import org.example.hacking02_sk.model.Banking;
import org.example.hacking02_sk.model.DetailHistory;
import org.example.hacking02_sk.model.MyCsrfToken;
import org.example.hacking02_sk.model.SendBanking;
import org.example.hacking02_sk.model.User;
import org.example.hacking02_sk.service.Encrypt;
import org.example.hacking02_sk.service.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("banking")
public class BankingController {
    @Autowired
    BankingMapper bankingMapper;
    
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    BankinghistMapper bankinghistMapper;
    Map<String, String> checkBankingData;
    List<Banking> bankList;
    SendBanking sendBanking;
    String acc;
    String csrfToken;

    @ModelAttribute("checkData")
    Map<String, String> check() {
        return checkBankingData;
    }

    @ModelAttribute("accList")
    List<Banking> accList(HttpSession session){
        User user = (User)session.getAttribute("user");
//        System.out.println(user);
        if (user != null){
            bankList = bankingMapper.myid(user.getMyid());
        }
        return bankList;
    }

    @ModelAttribute("bankList")
    String[] bankList(){
        String banks = "MNST,KEB하나은행,SC제일은행,국민은행,신한은행,외환은행,우리은행,한국시티은행,농협,기업은행";
        String[] list = banks.split(",");
        return list;
    }

    @RequestMapping("{page}")
    String bankingInOut(
            @PathVariable String page,
            HttpSession session,
            Model model){
        User user = (User)session.getAttribute("user");
//        System.out.println(user + "??");
        if (user == null) {
//            System.out.println("여기로 들어옴?");
            model.addAttribute("msg", "로그인 해주세요.");
            model.addAttribute("check", 2);
            return "banking/alert";
        }else {
            long sessionCreateTime = session.getCreationTime();
            Date time = new Date(sessionCreateTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String myid = session.getAttribute();
            if (page.equals("myaccount")) {
                List<Banking> banking = bankingMapper.myid(user.getMyid());
//            System.out.println(banking);
                model.addAttribute("accs", banking);
            }else if (page.equals("bankingResult")) {
                model.addAttribute("result", sendBanking);
            }else if (page.equals("acchistory")) {
                List<SendBanking> sendBankings = new ArrayList<>();
                for(Banking bank : bankList) {
                    SendBanking sendBanking = bankinghistMapper.sendbanking(bank.getMyacc());
                    sendBanking.setMyacc(sendBanking.getMyaccDec());
                    sendBanking.setMysendacc(sendBanking.getMysendaccDec());
                    if (sendBanking != null) {
                        sendBankings.add(sendBanking);
                    }
                }

                if (sendBankings.size() == 0) {
                    model.addAttribute("msg", "조회된 내역이 없습니다.");
                    return "banking/alert";
                }
                model.addAttribute("sendBankings", sendBankings);
                model.addAttribute("time", sdf.format(time));
            }else if (page.equals("detailhistory")) {
                csrfToken = new MyCsrfToken().getCsrfToken();
                model.addAttribute("csrfToken", csrfToken);
                model.addAttribute("myacc", this.acc);
            }else if (page.equals("inout")) {
                csrfToken = new MyCsrfToken().getCsrfToken();
                model.addAttribute("csrfToken", csrfToken);
            }
            //System.out.println(page);
            model.addAttribute("name", user.getMyname());
            return "banking/" + page;
        }
    }

    @PostMapping("getmyacc")
    @ResponseBody
    Map<String, Integer> getmyacc(@RequestBody Map<String, String> myacc){
        Banking bank = bankingMapper.myacc(Encrypt.encryptAES(myacc.get("myacc")));
        Map<String, Integer> acc = new HashMap<>();
        acc.put("money", bank.getMymoney());
        return acc;
    };

    @PostMapping("confirm")
    @ResponseBody
    String confirm(@RequestBody Map<String, String> data) {
        checkBankingData = data;
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String date = currentTime.format(dateFormatter);
        String time = currentTime.format(timeFormatter);
        checkBankingData.put("myaccdate", date);
        checkBankingData.put("myacctime", time);
        return "Y";
    }

    @PostMapping("sendBank")
    ModelAndView sendBank(SendBanking sendBanking, HttpServletRequest request, HttpSession session) {
        ModelAndView mav = new ModelAndView("banking/alert");
        String msg = "";
        /* csrf secure
        User user = (User)session.getAttribute("user");
        Banking checkBanking = bankingMapper.myid(user.getMyid()).get(0);
        System.out.println(checkBanking.getMyacc());
        System.out.println(sendBanking.getMyacc());
        if (!checkBanking.getMyacc().equals(sendBanking.getMyacc())) {
            msg = "조작하지 마세요.";
            mav.addObject("msg", msg);
            return mav;
        }
        */

//        System.out.println(sendBanking.getMyaccbalance());


        if (sendBanking.getMyaccbalance() < 0 || String.valueOf(sendBanking.getMyaccbalance()).contains("-")) {
//            System.out.println(sendBanking.getMyaccbalance());
            msg = "조작하지 마세요.";
            mav.addObject("msg", msg);
            return mav;
        }
        /* csrf secure
        else if (!sendBanking.getCsrfToken().equals(csrfToken)) {
            msg = "조작하지 마세요.";
            mav.addObject("msg", msg);
            return mav;
        }
        */

        char[] ch1 = sendBanking.getMyaccmemo().toCharArray();
        char[] ch2 = sendBanking.getMyaccioname().toCharArray();

        for (int i=0; i<sendBanking.getMyaccmemo().length(); i++) {
            if (ch1[i] == '<' || ch1[i] == '>' || ch1[i] == '/' || ch1[i] == '\'' || ch1[i] == ',') {
                ch1[i] = ' ';
            }
        }
        sendBanking.setMyaccmemo(String.valueOf(ch1));
        for (int i=0; i<sendBanking.getMyaccioname().length(); i++) {
            if (ch2[i] == '<' || ch2[i] == '>' || ch2[i] == '/' || ch2[i] == '\'' || ch2[i] == ',') {
                ch2[i] = ' ';
            }
        }
        sendBanking.setMyaccioname(String.valueOf(ch2));

        Banking banking = bankingMapper.myacc(sendBanking.getMyacc());
        int submoney = banking.getMymoney() - sendBanking.getMyaccbalance();
        String memo = sendBanking.getMyaccmemo();

        if (submoney < 0) {
            msg = "잔고가 부족합니다.";
            mav.addObject("msg", msg);
            return mav;
        }else if (!Encrypt.hashMD5(sendBanking.getMyaccpw()).equals(banking.getMyaccpw())) {
            msg = "비밀번호를 다시 입력해주세요.";
            mav.addObject("msg", msg);
            return mav;
        }else if (sendBanking.getMyacc() == sendBanking.getMysendacc()) {
            msg = "출금계좌와 입금계좌는 중복될 수 없습니다.";
            mav.addObject("msg", msg);
            return mav;
        }else if (sendBanking.getMyaccbalance() == 0) {
            msg = "금액을 제대로 입력해주세요.";
            mav.addObject("msg", msg);
            return mav;
        }

        // JWT validation - start
        String jwt = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JWT") && jwtUtil.validateToken(cookie.getValue())) {
                    jwt = jwtUtil.getToken(request.getCookies());
                }
            }
        }

        if (jwt == null) {
            msg = "JWT가 존재하지 않습니다.";
            mav.addObject("msg", msg);
            return mav;
        } else {
            System.out.println("(hy debug) JWT 인증성공 : " + jwt);
        }
        // JWT validation - end

        if (sendBanking.getMysendbank().equals("MNST")) {
            Banking banking2 = bankingMapper.myacc(sendBanking.getMysendacc());
            int addmoney = 0;
            if (banking2 == null) {
                msg = "입금계좌번호를 확인해주세요.";
                mav.addObject("msg", msg);
                return mav;
            }

            sendBanking.setMyaccout(submoney);
//            System.out.println(sendBanking);
            bankingMapper.submoney(sendBanking);
            bankinghistMapper.insert(sendBanking);

            addmoney = banking2.getMymoney() + sendBanking.getMyaccbalance();
            sendBanking.setMyaccin(addmoney);
            sendBanking.setMyaccout(0);
            sendBanking.setMysendacc(sendBanking.getMyacc());
            sendBanking.setMyacc(banking2.getMyacc());
            sendBanking.setMyaccmemo("");
            bankingMapper.addmoney(sendBanking);
            bankinghistMapper.insert(sendBanking);
//            System.out.println(sendBanking);
        }

        sendBanking.setMyaccmemo(memo);
        sendBanking.setMyaccout(submoney);
        mav.addObject("msg", "이체처리 완료되었습니다.");
        mav.addObject("check", 1);
        this.sendBanking = sendBanking;
        //System.out.println(sendBanking);
        return mav;
    }

    @PostMapping("accSearch")
    @ResponseBody
    String accSearch(@RequestParam String acc) {
        this.acc = acc;
        return "Y";
    }

    @PostMapping("detailhistory")
    String detailhistory(DetailHistory detailHistory, HttpSession session, Model model) {
        if (detailHistory.getCheckSearch() == 1) {
            if (detailHistory.getKeyword() == null || detailHistory.getKeyword().strip().equals("")) {
                model.addAttribute("msg", "조작하지 마세요.");
                return "banking/alert";
            }
            /* csrf secure
            else if (!detailHistory.getCsrfToken().equals(csrfToken)) {
                model.addAttribute("msg", "조작하지 마세요.");
                return "banking/alert";
            }
            */
        }
        /* csrf secure
        else {
            if (!detailHistory.getCsrfToken().equals(csrfToken)) {
                model.addAttribute("msg", "조작하지 마세요.");
                return "banking/alert";
            }
        }
        */

        if (detailHistory.getKeyword() != null) {
            char[] ch3 = detailHistory.getKeyword().toCharArray();
            for (int i=0; i<detailHistory.getKeyword().length(); i++) {
                if (ch3[i] == '<' || ch3[i] == '>' || ch3[i] == '/' || ch3[i] == '\'' || ch3[i] == ',') {
                    ch3[i] = ' ';
                }
            }
            detailHistory.setKeyword(String.valueOf(ch3));
        }else {
            char[] ch1 = detailHistory.getBreakdown().toCharArray();
            char[] ch2 = detailHistory.getDeal().toCharArray();
            char[] ch4 = detailHistory.getPredate().toCharArray();
            char[] ch5 = detailHistory.getPostdate().toCharArray();

            for (int i=0; i<detailHistory.getBreakdown().length(); i++) {
                if (ch1[i] == '<' || ch1[i] == '>' || ch1[i] == '/' || ch1[i] == '\'' || ch1[i] == ',') {
                    ch1[i] = ' ';
                }
            }
            detailHistory.setBreakdown(String.valueOf(ch1));
            for (int i=0; i<detailHistory.getDeal().length(); i++) {
                if (ch2[i] == '<' || ch2[i] == '>' || ch2[i] == '/' || ch2[i] == '\'' || ch2[i] == ',') {
                    ch2[i] = ' ';
                }
            }
            detailHistory.setDeal(String.valueOf(ch2));
            for (int i=0; i<detailHistory.getPredate().length(); i++) {
                if (ch4[i] == '<' || ch4[i] == '>' || ch4[i] == '/' || ch4[i] == '\'' || ch4[i] == ',') {
                    ch4[i] = ' ';
                }
            }
            detailHistory.setPredate(String.valueOf(ch4));
            for (int i=0; i<detailHistory.getPostdate().length(); i++) {
                if (ch5[i] == '<' || ch5[i] == '>' || ch5[i] == '/' || ch5[i] == '\'' || ch5[i] == ',') {
                    ch5[i] = ' ';
                }
            }
            detailHistory.setPostdate(String.valueOf(ch5));
        }

        List<SendBanking> sendBankings;
        if (detailHistory.getKeyword() != null) {
            sendBankings = bankinghistMapper.searchmemo(detailHistory.getKeyword());
        }else {
            sendBankings = bankinghistMapper.history(detailHistory);
        }
        int accin = 0;
        int accout = 0;
        for (SendBanking sendbank : sendBankings) {
            accout += sendbank.getMyaccout();
            accin += sendbank.getMyaccin();
        }

        session.setAttribute("accin", accin);
        session.setAttribute("accout", accout);
        session.setAttribute("historys", sendBankings);
//        System.out.println(detailHistory);
//        System.out.println(accin + " " + accout);
        return "redirect:/banking/detailhistory";
    }
}
