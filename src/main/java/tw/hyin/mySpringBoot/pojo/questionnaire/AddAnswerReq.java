package tw.hyin.mySpringBoot.pojo.questionnaire;

import lombok.Builder;
import lombok.Data;
import tw.hyin.mySpringBoot.entity.QuestionnaireAnswer;

import java.io.Serializable;
import java.util.List;

/**
 * @author H-yin on 2021.
 */
@Data
@Builder
public class AddAnswerReq implements Serializable {

    private String questionnaireType;
    private List<QuestionnaireAnswer> answerList;

}
