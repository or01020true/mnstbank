package org.example.hacking02_sk.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.example.hacking02_sk.mapper.BankingMapper;
import org.example.hacking02_sk.mapper.BankinghistMapper;
import org.example.hacking02_sk.model.Banking;
import org.example.hacking02_sk.model.DetailHistory;
import org.example.hacking02_sk.model.SendBanking;
import org.example.hacking02_sk.model.User;
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
    BankinghistMapper bankinghistMapper;
    Map<String, String> checkBankingData;
    List<Banking> bankList;
    SendBanking sendBanking;
    String acc;

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
                model.addAttribute("myacc", this.acc);
            }
            //System.out.println(page);
            model.addAttribute("name", user.getMyname());
            return "banking/" + page;
        }
    }

    @PostMapping("getmyacc")
    @ResponseBody
    Map<String, Integer> getmyacc(@RequestBody Map<String, String> myacc){
        Banking bank = bankingMapper.myacc(Integer.parseInt(myacc.get("myacc")));
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
    ModelAndView sendBank(SendBanking sendBanking) {
        ModelAndView mav = new ModelAndView("banking/alert");
        String msg = "";
        Banking banking = bankingMapper.myacc(sendBanking.getMyacc());
        int submoney = banking.getMymoney() - sendBanking.getMyaccbalance();
        String memo = sendBanking.getMyaccmemo();

        if (submoney < 0) {
            msg = "잔고가 부족합니다.";
            mav.addObject("msg", msg);
            return mav;
        }else if (sendBanking.getMyaccpw() != banking.getMyaccpw()) {
            msg = "비밀번호를 다시 입력해주세요.";
            mav.addObject("msg", msg);
            return mav;
        }else if (sendBanking.getMyacc() == sendBanking.getMysendacc()) {
            msg = "출금계좌와 입금계좌는 중복될 수 없습니다.";
            mav.addObject("msg", msg);
            return mav;
        }

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
    String detailhistory(DetailHistory detailHistory, HttpSession session) {
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
