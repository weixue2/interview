package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.LearningSituation;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.LearningSituationService;
import com.huatu.tiku.interview.util.LogPrint;
import com.huatu.tiku.interview.util.common.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 14:34
 * @Modefied By: 学习情况
 */
@Slf4j
@RestController
@RequestMapping("/end/ls")
public class LearningSituationController {

    @Autowired
    private LearningSituationService learningSituationService;

    /**
     * 新增学员学习情况
     * @param learningSituation
     * @return
     */
    @LogPrint
    @PostMapping(value="",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result add(@RequestBody LearningSituation learningSituation){
        learningSituation.setCreator("admin");
        log.info("请求参数learningSituation：{}",learningSituation);
        return learningSituationService.save(learningSituation)? Result.ok(): Result.build(ResultEnum.INSERT_FAIL);
    }


    /**
     * 删除学员学习情况
     * @param id
     * @return
     */
    @LogPrint
    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result del(@PathVariable  Long id){

        return learningSituationService.del(id) == 0 ?Result.build(ResultEnum.DELETE_FAIL):Result.ok();
    }

    /**
     *  修改学员学习情况
     * @param learningSituation
     * @return
     */
    @LogPrint
    @PutMapping(value="",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result update(LearningSituation learningSituation){
        learningSituation = learningSituationService.findOne(learningSituation.getId());
        if (learningSituation == null){
            return Result.build(ResultEnum.UPDATE_FAIL);
        }else{
            return learningSituationService.save(learningSituation)? Result.ok(): Result.build(ResultEnum.UPDATE_FAIL);
        }
    }


    /**
     * 查询某条学习情况记录
     */
    @LogPrint
    @GetMapping(value="detail/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result detail(@PathVariable  Long id){
        log.info("id：{}",id);
        LearningSituation learningSituation = learningSituationService.findOne(id);
        return learningSituation != null? Result.ok(learningSituation): Result.build(ResultEnum.FIND_FAIL);
    }


    /**
     * 查询学习情况记录列表
     */
    @LogPrint
    @GetMapping(value="list",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result list(@RequestParam(name = "name") String name,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){

        Pageable pageRequest = new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "gmtCreate");
        log.info("name: {},pageRequest: {}", name, pageRequest);
        if(null == name){
            name = "";
        }
        List<LearningSituation> list = learningSituationService.findList(name,pageRequest);
        long c = learningSituationService.countByNameLikeStatus(name);

        PageUtil p = PageUtil.builder()
                .result(list)
                .next(c > page * pageSize ? 1 : 0)
                .total(c)
                .totalPage((0 == c % pageSize) ? (c / pageSize) : (c / pageSize + 1))
                .build();
        return  Result.ok(p);
    }




}
