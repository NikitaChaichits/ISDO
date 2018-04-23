package by.i4t.repository;

import by.i4t.objects.Notification;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends BaseUUIDRepository<Notification> {
    @Query("select n from Notification n where n.senderId.ID = ?1")
    List<Notification> findAllBySenderId(UUID senderId);

    @Query("select n from Notification n where n.receiverId.ID = ?1")
    List<Notification> findAllByReceiverId(UUID receiverId);

    @Query("select n.attachment from Notification n where n.ID = ?1")
    byte[] findAttachmentByNotificationId(UUID id);
}
