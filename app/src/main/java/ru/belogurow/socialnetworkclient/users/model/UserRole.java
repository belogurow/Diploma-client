package ru.belogurow.socialnetworkclient.users.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by alexbelogurow on 28.03.2018.
 */

public enum  UserRole {
    /**
     * Admin role
     */
    ADMIN,

    /**
     * User role
     */
    USER;

    public List<UserRole> getStatuses() {
        return Arrays.asList(ADMIN, USER);
    }
}
