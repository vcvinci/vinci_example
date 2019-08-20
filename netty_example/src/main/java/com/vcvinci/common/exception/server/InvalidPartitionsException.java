/**
 * FileName: InvalidPartitionsException
 * Author:   HuangTaiHong
 * Date:     2018/12/1 17:59
 * Description: InvalidPartitionsException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br>
 * 〈InvalidPartitionsException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/1
 * @since 1.0.0
 */
public class InvalidPartitionsException extends ServerException {
    public InvalidPartitionsException() {

    }

    public InvalidPartitionsException(String message) {
        super(message);
    }

    public InvalidPartitionsException(Throwable cause) {
        super(cause);
    }

    public InvalidPartitionsException(String message, Throwable cause) {
        super(message, cause);
    }
}