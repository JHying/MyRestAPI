package tw.hyin.mySpringBoot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.hyin.mySpringBoot.dao.QuestionnaireAnswerDao;
import tw.hyin.mySpringBoot.dao.QuestionnaireDao;
import tw.hyin.mySpringBoot.dao.QuestionnaireQuestionDao;
import tw.hyin.mySpringBoot.entity.Questionnaire;
import tw.hyin.mySpringBoot.entity.QuestionnaireAnswer;
import tw.hyin.mySpringBoot.entity.QuestionnaireQuestion;
import tw.hyin.mySpringBoot.pojo.LoginInfo;
import tw.hyin.mySpringBoot.pojo.questionnaire.AddAnswerReq;
import tw.hyin.mySpringBoot.pojo.questionnaire.AnswerRsp;
import tw.hyin.mySpringBoot.pojo.questionnaire.QuestionnaireList;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class QuestionnaireService {

    private final QuestionnaireQuestionDao questionDao;
    private final QuestionnaireAnswerDao answerDao;
    private final QuestionnaireDao questionnaireDao;

    @Autowired
    public QuestionnaireService(QuestionnaireQuestionDao questionDao, QuestionnaireAnswerDao answerDao
            , QuestionnaireDao questionnaireDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
        this.questionnaireDao = questionnaireDao;
    }

    /**
     * 取得問卷題目
     */
    @Cacheable(value = "questionCache")
    public List<QuestionnaireQuestion> getQuestions(String questionnaireType) {
        return questionDao.getQuestions(questionnaireType);
    }

    /**
     * 取得使用者填寫之問卷清單
     */
    @Cacheable(value = "questionnaireCache", key = "#user.userId")
    public QuestionnaireList getQuestionnaire(LoginInfo user) {
        return QuestionnaireList.builder()
                .questionnaires(questionnaireDao.getQuestionnaire(user.getUserId())).build();
    }

    /**
     * 取得填答內容
     */
    public List<AnswerRsp> getAnswers(Integer questionnaireId) {
        Questionnaire questionnaire = questionnaireDao.selectBean(questionnaireId);
        return questionnaire != null ?
                answerDao.getOnlyQuestionAndAns(questionnaire.getQuestionnaireId()) : null;
    }

    /**
     * 新增問卷
     */
    @CacheEvict(value = "questionnaireCache", key = "#user.userId")
    public void addAnswer(AddAnswerReq addAnswerReq, LoginInfo user) throws Exception {
        //建立問卷
        Questionnaire questionnaire = new Questionnaire();
        Long serialNo = questionnaireDao.createSerialNo(10000000L, "questionnaireId");
        questionnaire.setQuestionnaireId(serialNo.intValue());
        questionnaire.setUserId(user.getUserId());
        questionnaire.setQuestionnaireType(addAnswerReq.getQuestionnaireType());
        questionnaire.setCreateDate(new Date());
        questionnaireDao.saveBean(questionnaire);
        //寫入答案
        for (QuestionnaireAnswer ans : addAnswerReq.getAnswerList()) {
            ans.setQuestionnaireId(serialNo.intValue());
            answerDao.saveBean(ans);
        }
    }

    /**
     * 編輯問卷
     */
    public void updateAnswer(AddAnswerReq addAnswerReq) throws Exception {
        QuestionnaireAnswer answer;
        for (QuestionnaireAnswer ans : addAnswerReq.getAnswerList()) {
            answer = answerDao.selectBean(ans.getAnswerId());
            answer.setAnswer(ans.getAnswer());
            answer.setTextAnswer(ans.getTextAnswer());
            answerDao.updateBean(answer);
        }
    }

    /**
     * 刪除問卷
     */
    @CacheEvict(value = "questionnaireCache", key = "#user.userId")
    public void deleteQuestionaire(Integer questionnaireId, LoginInfo user) throws Exception {
        answerDao.deleteByCondition("QuestionnaireID", questionnaireId);
        questionnaireDao.deleteBeanById(questionnaireId);
    }

}
