package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.LearningSituation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 16:06
 * @Modefied By:学习情况
 */
@Repository
public interface LearningSituationRepository extends JpaRepository<LearningSituation, Long> {

    @Transactional
    @Modifying
    @Query("update LearningSituation ls set ls.status=-1 where ls.id=?1")
    int updateToDel(Long id);


    @Query(value = "SELECT ifnull(avg(behavior),0) ,ifnull(avg(language_expression),0) ,ifnull(avg(focus_topic),0) ,ifnull(avg(is_organized),0) ,ifnull(avg(have_substance),0) " +
            "FROM t_learning_situation WHERE open_id = ?1 AND answer_date = curdate() and status = 1", nativeQuery = true)
    List<Object[]> countTodayAvg(String openId);


    @Query(value = "SELECT practice_content , ifnull(count(1),0) FROM t_learning_situation WHERE open_id = ?1 " +
            "AND answer_date = curdate() and status = 1 GROUP BY practice_content ORDER BY practice_content asc", nativeQuery = true)
    List<Object[]> countTodayAnswerCount(String openId);





    @Query(value = "SELECT ifnull(avg(behavior),0) ,ifnull(avg(language_expression),0) ,ifnull(avg(focus_topic),0) ,ifnull(avg(is_organized),0) ,ifnull(avg(have_substance),0)  " +
            "FROM t_learning_situation WHERE open_id = ?1 and status = 1 ", nativeQuery = true)
    List<Object[] > countTotalAvg(String openId);


    @Query(value = "SELECT practice_content ,ifnull(count(1),0)  FROM t_learning_situation WHERE open_id = ?1 and status = 1 " +
            " GROUP BY practice_content ORDER BY practice_content asc", nativeQuery = true)
    List<Object[]> countTotalAnswerCount(String openId);


    List<LearningSituation> findByStatusAndNameLike(int status,String name, Pageable pageRequest);

    long countByStatusAndNameLike( int status,String name);


    @Query("select ls.remark from  LearningSituation ls where ls.openId = ?1 and ls.answerDate = ?2 and ls.status = 1 order by ls.gmtCreate asc")
    List<String> findRemarksByOpenIdAndAnswerDateAndStatusOrderByGmtCreateAsc(String openId,String date);

}
