package tw.hyin.mySpringBoot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import tw.hyin.mySpringBoot.config.JsonDateDeseriConfig;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author H-yin on 2021.
 */
@Data
@Entity
@NoArgsConstructor
@DynamicInsert//解決 not null 欄位沒給值時，不會自動塞 default 的問題
@Table(name = "Questionnaire", catalog = "TESTDB", schema = "dbo")
public class Questionnaire implements Serializable {

    @Id
    @Column(name = "QuestionnaireID")
    private Integer questionnaireId;

    @Column(name = "QuestionnaireType")
    private String questionnaireType;

    @Column(name = "UserID")
    private String userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JsonDeserialize(using = JsonDateDeseriConfig.class)
    @Column(name = "CreateDate")
    private Date createDate;

}
