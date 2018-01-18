package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.LearningSituation;
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
    @Query("update LearningSituation ls set ls.status=-1 where ls.id=?5")
    int updateToDel(Long id);


    @Query(value = "SELECT avg(behavior) ,avg(language_expression) ,avg(focus_topic) ,avg(is_organized) ,avg(have_substance) " +
            "FROM t_learning_situation WHERE user_id = ?1 AND answer_date = curdate()", nativeQuery = true)
    List<Double> countTodayAvg(long userId);


    @Query(value = "SELECT practice_content ,count(1)  FROM t_learning_situation WHERE user_id = ?1 " +
            "AND answer_date = curdate() GROUP BY practice_content ORDER BY practice_content asc", nativeQuery = true)
    List<List<Integer>> countTodayAnswerCount(long userId);





    @Query(value = "SELECT avg(behavior) ,avg(language_expression) ,avg(focus_topic) ,avg(is_organized) ,avg(have_substance) " +
            "FROM t_learning_situation WHERE user_id = ?1 ", nativeQuery = true)
    List<Double> countTotalAvg(long userId);


    @Query(value = "SELECT practice_content ,count(1)  FROM t_learning_situation WHERE user_id = ?1 " +
            " GROUP BY practice_content ORDER BY practice_content asc", nativeQuery = true)
    List<List<Integer>> countTotalAnswerCount(long userId);



}
