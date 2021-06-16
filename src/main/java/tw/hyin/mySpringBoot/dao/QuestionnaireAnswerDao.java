package tw.hyin.mySpringBoot.dao;

import org.springframework.stereotype.Repository;
import tw.hyin.mySpringBoot.pojo.questionnaire.AnswerRsp;
import tw.hyin.mySpringBoot.utils.SqlQueryUtil;
import tw.hyin.mySpringBoot.entity.QuestionnaireAnswer;
import tw.hyin.mySpringBoot.utils.QueryUtil;

import java.util.List;

/**
 * @author H-yin on 2021.
 */
@Repository
@SuppressWarnings("unchecked")
public class QuestionnaireAnswerDao extends BaseDAO<QuestionnaireAnswer> {

    public List<QuestionnaireAnswer> getAnswers(Integer questionnaireId) {
        QueryUtil<QuestionnaireAnswer> queryUtil = new QueryUtil<>(getSession(), QuestionnaireAnswer.class);
        queryUtil.addEqual("questionnaireId", questionnaireId);
        return (List<QuestionnaireAnswer>) queryUtil.getResultList(getSession(), false);
    }

    public List<AnswerRsp> getOnlyQuestionAndAns(Integer questionnaireId) {

        SqlQueryUtil<AnswerRsp> sqlQueryUtil = new SqlQueryUtil<>(AnswerRsp.class);

        String sql = "SELECT " +
                " qa.AnswerID as answerId, " +
                " qa.QuestionnaireID as questionnaireId, " +
                " qa.QuestionID as questionId, " +
                " qa.SelectionID as selectionId, " +
                " qa.Answer as answer, " +
                " qa.TextAnswer as textAnswer, " +
                " qqq.QuestionContent as questionContent, " +
                " qs.SelectionContent as selectionContent " +
                " FROM TESTDB.dbo.QuestionnaireAnswer qa " +
                " INNER JOIN TESTDB.dbo.QuestionnaireQuestion qqq " +
                " ON qqq.QuestionID = qa.QuestionID " +
                " LEFT JOIN TESTDB.dbo.QuestionnaireSelection qs " +
                " ON qs.QuestionID = qa.QuestionID AND qs.SelectionValue = qa.Answer " +
                " WHERE 1=1 " +
                sqlQueryUtil.addParameter(questionnaireId, "AND qa.QuestionnaireID = :questionnaireId ", "questionnaireId", questionnaireId) +
                " and (answer is not null or textAnswer is not null) "+
                " order by questionId ";

        return sqlQueryUtil.getResultList(getSession(), sql);
    }

}
