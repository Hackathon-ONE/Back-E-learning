package learning.platform.service;

import learning.platform.dto.SystemNotificationRequestDTO;
import learning.platform.dto.NotificationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    void createSystemNotification(SystemNotificationRequestDTO request);
    Page<NotificationResponseDTO> getUnreadNotificationsForUser(Long userId, Pageable pageable);
    void markNotificationAsRead(Long userId, Long eventId);
}