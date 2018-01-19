package com.huatu.tiku.interview.task;

import com.google.gson.Gson;
import com.huatu.common.exception.BizException;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.constant.cache.RedisKeyConstant;
import com.huatu.tiku.interview.service.LearningReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * Created by x6 on 2018/1/19.
 *
 * 报道通知
 */
@Slf4j
public class RegisterReportTask {


    @Autowired
    private LearningReportService learningReportService;
    @Autowired
    private RedisTemplate<String ,String> redisTemplate;
    @Autowired
    RestTemplate restTemplate;


    @PostConstruct
    public void init() {
        //添加停止任务线程
        Runtime.getRuntime().addShutdownHook(new Thread(()-> unlock()));
    }

    //每分钟执行一次
//    @Scheduled(fixedRate = 60000)
    public void submitMatchAnswer() throws BizException {
        if (!getLock()) {
            return;
        }
        String serverIp = "";
        try {
            serverIp = getServerIp();
            log.info("getServerIp:"+getServerIp());

            //处理业务（发送报道的图文通知）


        } catch (UnknownHostException e) {
            e.printStackTrace();
        }finally {
            unlock();
        }
        log.info("auto submit match answer task end.server={}", serverIp);
    }


    private String getServerIp() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();//获取的是本地的IP地址 //PC-20140317PXKX/192.168.0.121
        String hostAddress = address.getHostAddress();//192.168.0.121
        return hostAddress;
    }

    //TODO  图文类型的推送消息
    private String pushRegisterReport()  {
        String msg = "{\"touser\":\"o4gqK1rOtTxZ-cGlmHF5oyx6he58\", \"msgtype\":\"text\",\"text\":{ \"content\": \"谁让你这么早下班的？？？."+ UUID.randomUUID()+"\"} }";
        String jsonString = new Gson().toJson(msg).toString();
        // 调用接口获取access_token
        String accessToken = redisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN);
        String requestUrl = WeChatUrlConstant.MESSAGE_MANY_URL.replace(WeChatUrlConstant.ACCESS_TOKEN, accessToken);



        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<>(msg, headers);

        String result =  restTemplate.postForObject(requestUrl,formEntity,String.class);
        log.info("result:"+result);

        return null;
    }




    /**
     * 释放定时任务锁
     */
    private void unlock() {
        String lockKey = RedisKeyConstant.SAVE_REPORT_LOCK;
        String currentServer = (String)redisTemplate.opsForValue().get(lockKey);

        log.info("current server={}",currentServer);
        String serverIp = "";
        try {
            serverIp = getServerIp();
            log.info("getServerIp:"+getServerIp());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (serverIp.equals(currentServer)) {
            redisTemplate.delete(lockKey);

            log.info("release lock,server={},timestamp={}",currentServer,System.currentTimeMillis());
        }
    }

    /**
     *
     * @return 是否获得锁
     */
    private boolean getLock() {
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();

        String lockKey = RedisKeyConstant.SAVE_REPORT_LOCK;
        String value = opsForValue.get(lockKey);
        log.info("get lock timestamp={}",System.currentTimeMillis());
        String serverIp = "";
        try {
            serverIp = getServerIp();
            log.info("getServerIp:"+getServerIp());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (StringUtils.isBlank(value)) { //值为空
            boolean booleanValue = opsForValue.setIfAbsent(lockKey, serverIp).booleanValue();

            if(booleanValue || serverIp.equals(opsForValue.get(lockKey))){
                return true;
            }else{
                return false;
            }

        } else if (StringUtils.isNoneBlank(value) && !value.equals(serverIp)) {
            //被其它服务器锁定
            log.info("auto submit match lock server={},return", value);
            return false;
        } else { //被自己锁定
            return true;
        }
    }

}
