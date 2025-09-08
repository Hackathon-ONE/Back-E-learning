package learning.platform.service.impl;

import learning.platform.dto.SystemNotificationRequestDTO;
import learning.platform.dto.NotificationResponseDTO;
import learning.platform.entity.NotificationEvent;
import learning.platform.entity.NotificationRecipient;
import learning.platform.entity.NotificationRecipientId;
import learning.platform.entity.User;
import learning.platform.enums.Role;
import learning.platform.exception.ResourceNotFoundException;
import learning.platform.mapper.NotificationMapper;
import learning.platform.repository.NotificationEventRepository;
import learning.platform.repository.NotificationRecipientRepository;
import learning.platform.repository.UserRepository;
import learning.platform.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
// Se elimina @RequiredArgsConstructor, se crea el constructor manualmente
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final NotificationEventRepository eventRepository;
    private final NotificationRecipientRepository recipientRepository;
    private final NotificationMapper notificationMapper;

    // Constructor para la inyección de dependencias (reemplaza a @RequiredArgsConstructor)
    public NotificationServiceImpl(UserRepository userRepository, NotificationEventRepository eventRepository, NotificationRecipientRepository recipientRepository, NotificationMapper notificationMapper) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.recipientRepository = recipientRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    @Transactional
    public void createSystemNotification(SystemNotificationRequestDTO request) {
        // 1. Crear el evento de notificación
        NotificationEvent event = new NotificationEvent();
        event.setNotificationType(request.getNotificationType());
        event.setContent(request.getContent());
        NotificationEvent savedEvent = eventRepository.save(event);

        // 2. Determinar los destinatarios según el tipo de notificación
        List<User> recipients;
        switch (request.getNotificationType()) {
            case NOTIF_STUDENTS:
                recipients = userRepository.findByRole(Role.STUDENT);
                break;
            case NOTIF_INSTRUCTORS:
                recipients = userRepository.findByRole(Role.INSTRUCTOR);
                break;
            case NOTIF_GENERAL:
            default: // Por defecto, si es general, se envía a todos
                recipients = userRepository.findAll();
                break;
        }

        if (recipients.isEmpty()) {
            return;
        }

        // 3. Crear un registro de destinatario para cada uno
        List<NotificationRecipient> recipientEntries = recipients.stream()
                .map(user -> {
                    NotificationRecipient recipient = new NotificationRecipient();
                    recipient.setId(new NotificationRecipientId(savedEvent.getId(), user.getId()));
                    recipient.setEvent(savedEvent);
                    recipient.setUser(user);
                    recipient.setRead(false);
                    return recipient;
                })
                .collect(Collectors.toList());

        // 4. Guardar todos los registros de destinatarios
        recipientRepository.saveAll(recipientEntries);
    }

    @Override
    public Page<NotificationResponseDTO> getUnreadNotificationsForUser(Long userId, Pageable pageable) {
        return recipientRepository.findByUserIdAndIsReadFalseOrderByEventCreatedAtDesc(userId, pageable)
                .map(notificationMapper::toDTO);
    }

    @Override
    @Transactional
    public void markNotificationAsRead(Long userId, Long eventId) {
        NotificationRecipient recipient = recipientRepository.findById_UserIdAndId_EventId(userId, eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found for this user."));

        recipient.setRead(true); // Asumo que tienes el método setRead() en la entidad
        recipientRepository.save(recipient);
    }
}