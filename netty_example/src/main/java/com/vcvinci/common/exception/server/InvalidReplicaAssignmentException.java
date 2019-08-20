/**
 * FileName: InvalidReplicaAssignmentException
 * Author:   HuangTaiHong
 * Date:     2018/12/3 10:44
 * Description: InvalidReplicaAssignmentException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br>
 * 〈InvalidReplicaAssignmentException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/3
 * @since 1.0.0
 */
public class InvalidReplicaAssignmentException extends ServerException {
    public InvalidReplicaAssignmentException() {

    }

    public InvalidReplicaAssignmentException(String message) {
        super(message);
    }

    public InvalidReplicaAssignmentException(Throwable cause) {
        super(cause);
    }

    public InvalidReplicaAssignmentException(String message, Throwable cause) {
        super(message, cause);
    }
}