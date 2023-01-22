package top.sheepyu.framework.sms.core.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ygq
 * @date 2023-01-22 15:03
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailParams implements SenderParams {
    private String receiveEmail;
    private Integer codeLength = 4;

    public EmailParams(String receiveEmail) {
        this.receiveEmail = receiveEmail;
    }
}
