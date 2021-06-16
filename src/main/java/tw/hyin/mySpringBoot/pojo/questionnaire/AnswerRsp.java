package tw.hyin.mySpringBoot.pojo.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author H-yin on 2021.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRsp implements Serializable {

    private Integer answerId;
    private Integer questionnaireId;
    private Integer questionId;
    private Integer selectionId;
    private Integer answer;
    private String textAnswer;
    private String questionContent;
    private String selectionContent;

}
