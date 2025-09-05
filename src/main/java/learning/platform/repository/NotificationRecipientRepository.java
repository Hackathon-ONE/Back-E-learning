package learning.platform.repository;

import learning.platform.entity.NotificationRecipient;
import learning.platform.entity.NotificationRecipientId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, NotificationRecipientId> {
    Page<NotificationRecipient> findByUserIdAndIsReadFalseOrderByEventCreatedAtDesc(Long userId, Pageable pageable);

    Optional<NotificationRecipient> findById_UserIdAndId_EventId(Long userId, Long eventId);
}