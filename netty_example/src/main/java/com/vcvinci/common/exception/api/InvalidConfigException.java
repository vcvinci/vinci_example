/**
 * FileName: InvalidConfigException
 * Author:   HuangTaiHong
 * Date:     2018/12/14 15:08
 * Description: InvalidConfigException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.api;

import com.vcvinci.common.exception.client.ClientException;

/**
 * 〈一句话功能简述〉<br>
 * 〈InvalidConfigException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/14
 * @since 1.0.0
 */
public class InvalidConfigException extends ClientException {
    public InvalidConfigException() {

    }

    public InvalidConfigException(String message) {
        super(message);
    }

    public InvalidConfigException(Throwable cause) {
        super(cause);
    }

    public InvalidConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}