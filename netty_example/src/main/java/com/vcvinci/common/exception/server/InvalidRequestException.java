/**
 * FileName: InvalidRequestException
 * Author:   HuangTaiHong
 * Date:     2018/12/6 14:13
 * Description: InvalidRequestException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br> 
 * 〈InvalidRequestException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/6
 * @since 1.0.0
 */
public class InvalidRequestException extends ServerException {
    public InvalidRequestException() {
        super();
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}