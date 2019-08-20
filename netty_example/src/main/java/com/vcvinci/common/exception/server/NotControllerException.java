/**
 * FileName: NotControllerException
 * Author:   HuangTaiHong
 * Date:     2018/12/1 15:59
 * Description: Controller已转移到其他Broker异常
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

import com.vcvinci.common.exception.RetriableException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈Controller已转移到其他Broker异常〉
 *
 * @author HuangTaiHong
 * @create 2018/12/1
 * @since 1.0.0
 */
public class NotControllerException extends ServerException implements RetriableException {
    public NotControllerException() {

    }

    public NotControllerException(String message) {
        super(message);
    }

    public NotControllerException(Throwable cause) {
        super(cause);
    }

    public NotControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}