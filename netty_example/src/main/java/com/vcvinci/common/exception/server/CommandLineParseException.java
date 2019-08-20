/**
 * FileName: CommandLineParseException
 * Author:   HuangTaiHong
 * Date:     2018/12/10 18:55
 * Description: CommandLineParseException
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.vcvinci.common.exception.server;

/**
 * 〈一句话功能简述〉<br>
 * 〈CommandLineParseException〉
 *
 * @author HuangTaiHong
 * @create 2018/12/10
 * @since 1.0.0
 */
public class CommandLineParseException extends ServerException {
    public CommandLineParseException() {

    }

    public CommandLineParseException(String message) {
        super(message);
    }

    public CommandLineParseException(Throwable cause) {
        super(cause);
    }

    public CommandLineParseException(String message, Throwable cause) {
        super(message, cause);
    }
}