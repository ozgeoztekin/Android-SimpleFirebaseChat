
package com.oztekino.simplefirebasechat;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserUtil {

    private static User user;
    private static Map<String, String> typingUsernameList = new LinkedHashMap<>();

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

    public static String handleUserTypingList(Context context,
                                              User user) {

        if (user.isTyping() && user.getStatus() == UserStatus.ONLINE.ordinal()
                && !user.getUserId().equals(UserUtil.user.getUserId())) { // do not show current user
            typingUsernameList.put(user.getUserId(), user.getUsername());
        } else {
            typingUsernameList.remove(user.getUserId());
        }
        return getTypingUsernames(context);
    }

    private static String getTypingUsernames(Context context) {

        String text = "";
        Iterator<String> iterator = typingUsernameList.values().iterator();
        while (iterator.hasNext()) {
            text = text.concat(iterator.next());

            if (iterator.hasNext()) {
                text = text.concat(", ");
            }
        }

        if (typingUsernameList.size() > 4) {
            return context.getString(R.string.several_typing_message);
        } else if (typingUsernameList.size() > 1) {
            return context.getString(R.string.typing_message, text, "are");
        } else if (typingUsernameList.size() == 1) {
            return context.getString(R.string.typing_message, text, "is");
        }

        return "";
    }


    public static User getUser() {
        return user;
    }
}

