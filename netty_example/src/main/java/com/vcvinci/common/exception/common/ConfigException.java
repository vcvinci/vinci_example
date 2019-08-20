package com.vcvinci.common.exception.common;

/**
 * Thrown if the user supplies an invalid configuration
 */

import com.vcvinci.common.exception.DoveException;

/**
 * @author sunyupeng
 */
public class ConfigException extends DoveException {

    private static final long serialVersionUID = 1L;

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String name, Object value) {
        this(name, value, null);
    }

    public ConfigException(String name, Object value, String message) {
        super("Invalid value " + value + " for configuration " + name + (message == null ? "" : ": " + message));
    }

}
