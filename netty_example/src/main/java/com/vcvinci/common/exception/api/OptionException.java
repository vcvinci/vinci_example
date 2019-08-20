/**
 * FileName: OptionException
 * Author:   HuangTaiHong
 * Date:     2018/12/14 15:14
 * Description: OptionException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.api;

import com.vcvinci.common.exception.client.ClientException;

/**
 * 〈一句话功能简述〉<br>
 * 〈OptionException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/14
 * @since 1.0.0
 */
public class OptionException extends ClientException {
    public OptionException() {

    }

    public OptionException(String message) {
        super(message);
    }

    public OptionException(Throwable cause) {
        super(cause);
    }

    public OptionException(String message, Throwable cause) {
        super(message, cause);
    }
}