/**
 * FileName: TopicExistsException
 * Author:   HuangTaiHong
 * Date:     2018/12/3 10:08
 * Description: TopicExistsException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br> 
 * 〈TopicExistsException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/3
 * @since 1.0.0
 */
public class TopicExistsException extends ServerException {
    public TopicExistsException() {

    }

    public TopicExistsException(String message) {
        super(message);
    }

    public TopicExistsException(Throwable cause) {
        super(cause);
    }

    public TopicExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}