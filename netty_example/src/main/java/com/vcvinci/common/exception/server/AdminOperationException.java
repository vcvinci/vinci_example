/**
 * FileName: AdminOperationException
 * Author:   HuangTaiHong
 * Date:     2018/12/3 15:40
 * Description: AdminOperationException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br>
 * 〈AdminOperationException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/3
 * @since 1.0.0
 */
public class AdminOperationException extends ServerException {
    public AdminOperationException() {

    }

    public AdminOperationException(String message) {
        super(message);
    }

    public AdminOperationException(Throwable cause) {
        super(cause);
    }

    public AdminOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}