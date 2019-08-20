package com.vcvinci.common.exception.client;

import com.vcvinci.common.exception.api.AuthorizationException;

import java.util.Collections;
import java.util.Set;

public class TopicAuthorizationException extends AuthorizationException {

    private final Set<String> unauthorizedTopics;

    public TopicAuthorizationException(Set<String> unauthorizedTopics) {
        super("Not authorized to access topics: " + unauthorizedTopics);
        this.unauthorizedTopics = unauthorizedTopics;
    }

    public TopicAuthorizationException(String unauthorizedTopic) {
        this(Collections.singleton(unauthorizedTopic));
    }

    public Set<String> unauthorizedTopics() {
        return unauthorizedTopics;
    }
}
