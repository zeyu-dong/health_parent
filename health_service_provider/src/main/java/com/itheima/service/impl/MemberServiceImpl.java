package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    public Member findByTelephone(String telephone) {

        return memberDao.findByTelephone(telephone);
    }

    public void add(Member member) {
        if (member.getPassword()!=null) {
            String password = member.getPassword();
            String s = MD5Utils.md5(password);
            member.setPassword(s);
        }
        memberDao.add(member);
    }
    public List<Integer> findMemberCountByMonth(List<String> month) {

//        List<Integer> memberCount = new ArrayList<>();
//        for (String s : list) {
//            String date = s + ".31";
//            Integer count = memberDao.findMemberCountBeforeDate(date);
//            memberCount.add(count);
//        }
//        return null;
////        return memberCount;

        List<Integer> list = new ArrayList<>();
        for(String m : month){
            m = m + ".31";//格式：2019.04.31
            Integer count = memberDao.findMemberCountBeforeDate(m);
            list.add(count);
        }
        return list;

    }
}
