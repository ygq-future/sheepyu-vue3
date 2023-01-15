package top.sheepyu.framework.security.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author ygq
 * @date 2022-12-03 17:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LoginUser {
    private Long id;
    private Integer userType;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expireTime;
}
