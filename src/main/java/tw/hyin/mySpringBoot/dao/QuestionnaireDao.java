package tw.hyin.mySpringBoot.dao;

import org.springframework.stereotype.Repository;
import tw.hyin.mySpringBoot.entity.Questionnaire;
import tw.hyin.mySpringBoot.utils.QueryUtil;

import java.util.List;

/**
 * @author H-yin on 2021.
 */
@Repository
@SuppressWarnings("unchecked")
public class QuestionnaireDao extends BaseDAO<Questionnaire> {

    public List<Questionnaire> getQuestionnaire(String userId) {
        QueryUtil<Questionnaire> queryUtil = new QueryUtil<>(getSession(), Questionnaire.class);
        queryUtil.addEqual("userId", userId);
        return (List<Questionnaire>) queryUtil.getResultList(getSession(), false);
    }

}
