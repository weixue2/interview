package com.huatu.tiku.interview.service.impl;

import com.google.common.collect.Lists;
import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.constant.UserStatusConstant;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.po.UserClassRelation;
import com.huatu.tiku.interview.exception.ReqException;
import com.huatu.tiku.interview.repository.ClassInfoRepository;
import com.huatu.tiku.interview.repository.UserClassRelationRepository;
import com.huatu.tiku.interview.repository.UserRepository;
import com.huatu.tiku.interview.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午4:32
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ClassInfoRepository classInfoRepository;

    @Autowired
    UserClassRelationRepository userClassRelationRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Boolean updateUser(User user, HttpServletRequest request) {
//        Object o = request.getSession().getAttribute("openId");
//        if(o == null){
//            throw new ReqException(ResultEnum.OPENID_ERROR);
//        }
//        String openId = o.toString();
//        String openId = "od2aM0j6XSIwjAt2fExHeegjOWn8";

        User user_ = userRepository.findByOpenId(user.getOpenId());
        System.out.println("讓我看看user:" + user_);
        if (user_ != null) {
            user_.setPhone(user.getPhone());
            user_.setSex(user.getSex());
            user_.setName(user.getName());
            user_.setIdCard(user.getIdCard());
            user_.setPregnancy(user.getPregnancy());
            user_.setStatus(1);
            user_.setNation(user.getNation());
            user_.setKeyContact(user.getKeyContact());
            user = userRepository.save(user_);
        }
        System.out.println("user:" + user_);
        return user == null ? false : true;
    }

    @Override
    public void createUser(String openId) {
        User user = new User();
        user.setOpenId(openId);
        user.setStatus(-1);
        userRepository.save(user);
    }

    @Override
    public User getUser(String openId) {
//        return userRepository.getUserByOpenIdAndStatus(openId, 1);
        return userRepository.findByOpenId(openId);
    }

    @Override
    public User getUserByOpenId(String openId) {
        return userRepository.findByOpenId(openId);
    }

    @Override
    public List<User> findAllUser(String content) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = Lists.newArrayList();
                if (StringUtils.isNotEmpty(content)) {
                    predicates.add(cb.like(root.get("name"), "%" + content + "%"));
                }
                predicates.add(cb.equal(root.get("status"), UserStatusConstant.NO_INFO.getStatus()));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return userRepository.findAll(specification);
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findByStatus(1);
    }

    @Override
    public Long getCluss(String openId) {
        List<UserClassRelation> list = userClassRelationRepository.findByOpenIdAndStatus(openId, 1);
        // 如果学员所在班级大于1，报错
        if(list.size() > 1){
            throw new ReqException(ResultEnum.CLASS_UNIQUE_ERROR);
        }
        if(list.isEmpty()){
//            throw new ReqException(ResultEnum.CLASS_NULL_ERROR);
            return 0L;
        }

        return list.get(0).getClassId();
    }

}
