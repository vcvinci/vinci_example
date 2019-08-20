/**
 * FileName: InvalidReplicationFactorException
 * Author:   HuangTaiHong
 * Date:     2018/12/1 18:01
 * Description: InvalidReplicationFactorException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br> 
 * 〈InvalidReplicationFactorException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/1
 * @since 1.0.0
 */
public class InvalidReplicationFactorException extends ServerException {
    public InvalidReplicationFactorException() {

    }

    public InvalidReplicationFactorException(String message) {
        super(message);
    }

    public InvalidReplicationFactorException(Throwable cause) {
        super(cause);
    }

    public InvalidReplicationFactorException(String message, Throwable cause) {
        super(message, cause);
    }
}