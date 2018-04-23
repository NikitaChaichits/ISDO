package edu.org.models.lineitems;

import by.i4t.objects.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
@AllArgsConstructor
public class NotificationDataLineItem {

    private String id;
    private String receiverId;
    private String receiverName;
    private String theme;
    private String sendingDate;
    private String read;
    private String status;

    private byte[] attachment;
    private String attachType;
    private String senderName;
    private String message;

    public NotificationDataLineItem() {}

    public NotificationDataLineItem(Notification notification){
        valueOf(notification);
    }

    public void valueOf(Notification notification){
        setId(notification.getID().toString());
        setReceiverId(notification.getReceiverId().getID().toString());
        setReceiverName(notification.getReceiverId().getName());
        setTheme(notification.getTheme());
        setSendingDate(new SimpleDateFormat("dd-MM-yyyy").format(notification.getSendingDate()));
        setRead(notification.isRead() ? "Прочитано" : "Не прочитано");
        setStatus(notification.isStatus() ? "Уведомлен" : "Не уведомлен");
        setSenderName(notification.getSenderId().getName());
        setMessage(notification.getMessage());

        setAttachment(notification.getAttachment());
        setAttachType(notification.getAttachType());
    }
}
