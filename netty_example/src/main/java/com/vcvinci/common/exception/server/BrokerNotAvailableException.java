/**
 * FileName: BrokerNotAvailableException
 * Author:   HuangTaiHong
 * Date:     2018/12/11 16:13
 * Description: BrokerNotAvailableException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br>
 * 〈BrokerNotAvailableException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/11
 * @since 1.0.0
 */
public class BrokerNotAvailableException extends ServerException {
    public BrokerNotAvailableException() {

    }

    public BrokerNotAvailableException(String message) {
        super(message);
    }

    public BrokerNotAvailableException(Throwable cause) {
        super(cause);
    }

    public BrokerNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}