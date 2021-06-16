package tw.hyin.mySpringBoot.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author H-yin on 2020.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo implements Serializable {

    private String userId;
    private String userName;
    private Integer deptNo;
    private List<String> roles;

}
