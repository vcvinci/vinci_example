/**
 * FileName: AdminCommandFailedException
 * Author:   HuangTaiHong
 * Date:     2018/12/12 14:31
 * Description: AdminCommandFailedException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br>
 * 〈AdminCommandFailedException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/12
 * @since 1.0.0
 */
public class AdminCommandFailedException extends ServerException {
    public AdminCommandFailedException() {

    }

    public AdminCommandFailedException(String message) {
        super(message);
    }

    public AdminCommandFailedException(Throwable cause) {
        super(cause);
    }

    public AdminCommandFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}