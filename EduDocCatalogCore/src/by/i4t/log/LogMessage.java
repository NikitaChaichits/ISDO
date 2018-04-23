package by.i4t.log;

import org.apache.logging.log4j.message.Message;

import by.i4t.helper.EduDocsAppLogSettings;
import by.i4t.objects.User;

public class LogMessage implements Message {
    private final User user;
    private final EduDocsAppLogSettings actionType;
    private final String msg;

    public LogMessage(User user, EduDocsAppLogSettings actionType, String msg) {
        this.user = user;
        this.actionType = actionType;
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public EduDocsAppLogSettings getActionType() {
        return actionType;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String getFormat() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFormattedMessage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object[] getParameters() {
        // TODO Auto-generated method stub
        Object[] UserDataArray = {user, actionType, msg};
        return UserDataArray;
    }

    @Override
    public Throwable getThrowable() {
        // TODO Auto-generated method stub
        return null;
    }

    public String toString() {
        return user.getName() + " " + actionType + " " + msg;
    }
}
