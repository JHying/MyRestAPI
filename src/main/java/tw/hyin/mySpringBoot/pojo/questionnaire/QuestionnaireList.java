package tw.hyin.mySpringBoot.pojo.questionnaire;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tw.hyin.mySpringBoot.entity.Questionnaire;

import java.io.Serializable;
import java.util.List;

/**
 * @author H-yin on 2021.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionnaireList implements Serializable {

    private List<Questionnaire> questionnaires;

}
