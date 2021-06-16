package tw.hyin.mySpringBoot.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tw.hyin.mySpringBoot.config.JsonDateDeseriConfig;

import java.io.Serializable;
import java.util.Date;

/**
 * @author AlanHsu on 2021.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileObj implements Serializable {

    @ApiModelProperty("檔案路徑")
    private String filePath;

    @ApiModelProperty("檔案名稱")
    private String fileName;

    @ApiModelProperty("上傳者")
    private String userId;

    @ApiModelProperty("上傳時間")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JsonDeserialize(using = JsonDateDeseriConfig.class)
    private Date uploadDate;

}
