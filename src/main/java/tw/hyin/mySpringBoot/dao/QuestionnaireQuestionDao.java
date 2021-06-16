package tw.hyin.mySpringBoot.dao;

import org.springframework.stereotype.Repository;
import tw.hyin.mySpringBoot.entity.QuestionnaireQuestion;
import tw.hyin.mySpringBoot.utils.QueryUtil;

import java.util.List;

/**
 * @author H-yin on 2021.
 */
@Repository
@SuppressWarnings("unchecked")
public class QuestionnaireQuestionDao extends BaseDAO<QuestionnaireQuestion> {

    public List<QuestionnaireQuestion> getQuestions(String questionnaireType) {
        QueryUtil<QuestionnaireQuestion> queryUtil = new QueryUtil<>(getSession(), QuestionnaireQuestion.class);
        queryUtil.addEqual("questionnaireType", questionnaireType);
        return (List<QuestionnaireQuestion>)
                getSession().createQuery(queryUtil.getCriterias(false)
                        .orderBy(queryUtil.getCb().asc(queryUtil.getRoot().get("questionNo")))).getResultList();
    }

}
