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
@Table(name = "QuestionnaireSelection", catalog = "TESTDB", schema = "dbo")
public class QuestionnaireSelection implements Serializable {

    @Id
    @Column(name = "SelectionID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer selectionId;

    @Column(name = "QuestionID")
    private Integer questionId;

    @Column(name = "SelectionValue")
    private Integer selectionValue;

    @Column(name = "SelectionContent")
    private String selectionContent;

    @Column(name = "SelectionOrder")
    private Integer selectionOrder;

}
