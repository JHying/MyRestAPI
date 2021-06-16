package tw.hyin.mySpringBoot.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author H-yin on 2021.
 */
@Data
@Entity
@NoArgsConstructor
@DynamicInsert//解決 not null 欄位沒給值時，不會自動塞 default 的問題
@Table(name = "QuestionnaireQuestion", catalog = "TESTDB", schema = "dbo")
public class QuestionnaireQuestion implements Serializable {

    @Id
    @Column(name = "QuestionID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    @Column(name = "QuestionnaireType")
    private String questionnaireType;

    @Column(name = "QuestionNo")
    private String questionNo;

    @Column(name = "QuestionContent")
    private String questionContent;

    @Column(name = "QuestionType")
    private String questionType;

    @Column(name = "QuestionOrder")
    private Integer questionOrder;

    @Column(name = "QuestionDesc")
    private String questionDesc;

    @ApiModelProperty("前置題ID (對應 questionId)")
    @Column(name = "PreQuestionID")
    private Integer preQuestionId;

    @ApiModelProperty("前置選項 (多選對應 selectionId, 單選對應 answer)")
    @Column(name = "PreValue")
    private Integer preValue;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "questionId", referencedColumnName = "questionId", insertable = false, updatable = false)
    @OrderBy(value = "selectionOrder ASC")
    private Set<QuestionnaireSelection> selections;

}
