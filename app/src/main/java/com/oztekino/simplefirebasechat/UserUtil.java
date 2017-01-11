
package com.oztekino.simplefirebasechat;

import com.google.firebase.auth.FirebaseUser;

public class UserUtil {

    private static User user;

    private UserUtil() {
    }

    public static void createUserInstance(FirebaseUser firebaseUser) {
        user = new User(firebaseUser.getUid(),
                firebaseUser.getDisplayName() == null
                        ? firebaseUser.getEmail().substring(0, firebaseUser.getEmail().indexOf('@'))
                        : firebaseUser.getDisplayName(),
                UserStatus.ONLINE.ordinal(),
                false,
                ColorUtil.getRandomColor());
    }

    public static void syncCurrentUser(User user) {
        UserUtil.user = user;
    }

    public static User getUser() {
        return user;
    }
}

