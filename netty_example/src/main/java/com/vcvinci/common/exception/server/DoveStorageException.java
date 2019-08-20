/**
 * FileName: DoveStorageException
 * Author:   HuangTaiHong
 * Date:     2018/11/26 15:05
 * Description: DoveStorageException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br>
 * 〈DoveStorageException〉
 *
 * @author HuangTaiHong
 * @create 2018/11/26
 * @since 1.0.0
 */
public class DoveStorageException extends ServerException {
    public DoveStorageException() {
        super();
    }

    public DoveStorageException(String message) {
        super(message);
    }

    public DoveStorageException(Throwable cause) {
        super(cause);
    }

    public DoveStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}