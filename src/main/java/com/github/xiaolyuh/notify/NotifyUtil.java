package com.github.xiaolyuh.notify;

import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

/**
 * 通知弹框
 *
 * @author yuhao.wang3
 */
public class NotifyUtil {
    private static final NotificationGroup TOOLWINDOW_NOTIFICATION = NotificationGroupManager.getInstance().getNotificationGroup("GitFlowPlusNotification");
    private static final NotificationGroup BALLOON_NOTIFICATION = NotificationGroupManager.getInstance().getNotificationGroup("GitFlowPlusNotification");
    private static final NotificationGroup NONE = NotificationGroupManager.getInstance().getNotificationGroup("GitFlowPlusNotification");


    public static void notifySuccess(Project project, String title, String message) {
        notify(NotificationType.INFORMATION, BALLOON_NOTIFICATION, project, title, message);
    }

    public static void notifyError(Project project, String title, String message) {
        notify(NotificationType.ERROR, TOOLWINDOW_NOTIFICATION, project, title, message);
    }

    public static void notifyGitCommand(Project project, String command) {
        notify(NotificationType.INFORMATION, NONE, project, "Git command:", command);
    }

    private static void notify(NotificationType type, NotificationGroup group, Project project, String title, String message) {
        group.createNotification(title, message, type, null).notify(project);
    }
}
