package tw.hyin.mySpringBoot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author H-yin on 2021.
 */
@Data
@Entity
@NoArgsConstructor
@DynamicInsert//解決 not null 欄位沒給值時，不會自動塞 default 的問題
@Table(name = "QuestionnaireAnswer", catalog = "TESTDB", schema = "dbo")
public class QuestionnaireAnswer implements Serializable {

    @Id
    @Column(name = "AnswerID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;

    @Column(name = "QuestionnaireID")
    private Integer questionnaireId;

    @Column(name = "QuestionID")
    private Integer questionId;

    @Column(name = "SelectionID")
    private Integer selectionId;

    @Column(name = "Answer")
    private Integer answer;

    @Column(name = "TextAnswer")
    private String textAnswer;

}
