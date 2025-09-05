package learning.platform.service.impl;

import learning.platform.dto.SystemNotificationRequestDTO;
import learning.platform.dto.NotificationResponseDTO;
import learning.platform.entity.NotificationEvent;
import learning.platform.entity.NotificationRecipient;
import learning.platform.entity.NotificationRecipientId;
import learning.platform.entity.User;
import learning.platform.enums.NotificationType;
import learning.platform.enums.Role;
import learning.platform.exception.ResourceNotFoundException;
import learning.platform.mapper.NotificationMapper;
import learning.platform.repository.NotificationEventRepository;
import learning.platform.repository.NotificationRecipientRepository;
import learning.platform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase NotificationServiceImpl.
 * Se utiliza Mockito para simular las dependencias (repositorios, mappers)
 * y probar la lógica de negocio en aislamiento.
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationEventRepository eventRepository;

    @Mock
    private NotificationRecipientRepository recipientRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void testCreateSystemNotification_ForStudentsOnly() {
        // Arrange (Preparar)
        SystemNotificationRequestDTO request = new SystemNotificationRequestDTO();
        request.setContent("Notification for students");
        request.setNotificationType(NotificationType.NOTIF_STUDENT);

        // Simulamos que el repositorio encuentra 2 estudiantes
        List<User> studentList = List.of(
                new User(1L, "Student 1", "s1@test.com", "pass", Role.STUDENT, true),
                new User(2L, "Student 2", "s2@test.com", "pass", Role.STUDENT, true)
        );
        when(userRepository.findByRole(Role.STUDENT)).thenReturn(studentList);

        // Simulamos el guardado del evento
        when(eventRepository.save(any(NotificationEvent.class))).thenReturn(new NotificationEvent());

        // Act (Actuar)
        notificationService.createSystemNotification(request);

        // Assert (Afirmar)
        // Verificamos que se llamó a buscar usuarios con el rol STUDENT
        verify(userRepository, times(1)).findByRole(Role.STUDENT);

        // Capturamos la lista de destinatarios para verificar
        ArgumentCaptor<List<NotificationRecipient>> recipientCaptor = ArgumentCaptor.forClass(List.class);
        verify(recipientRepository, times(1)).saveAll(recipientCaptor.capture());

        // Verificamos que el tamaño de la lista de destinatarios sea 2
        assertEquals(2, recipientCaptor.getValue().size());
    }

    @Test
    void testCreateSystemNotification_ForInstructorsOnly() {
        // Arrange
        SystemNotificationRequestDTO request = new SystemNotificationRequestDTO();
        request.setContent("Notification for instructors");
        request.setNotificationType(NotificationType.NOTIF_INSTRUCTOR);

        List<User> instructorList = List.of(
                new User(3L, "Instructor 1", "i1@test.com", "pass", Role.INSTRUCTOR, true)
        );
        when(userRepository.findByRole(Role.INSTRUCTOR)).thenReturn(instructorList);
        when(eventRepository.save(any(NotificationEvent.class))).thenReturn(new NotificationEvent());

        // Act
        notificationService.createSystemNotification(request);

        // Assert
        verify(userRepository, times(1)).findByRole(Role.INSTRUCTOR);

        ArgumentCaptor<List<NotificationRecipient>> recipientCaptor = ArgumentCaptor.forClass(List.class);
        verify(recipientRepository, times(1)).saveAll(recipientCaptor.capture());

        assertEquals(1, recipientCaptor.getValue().size());
    }

    @Test
    void testCreateSystemNotification_ForGeneralAudience() {
        // Arrange
        SystemNotificationRequestDTO request = new SystemNotificationRequestDTO();
        request.setContent("General notification");
        request.setNotificationType(NotificationType.NOTIF_GENERAL);

        List<User> allUsers = List.of(
                new User(1L, "Student 1", "s1@test.com", "pass", Role.STUDENT, true),
                new User(2L, "Instructor 1", "i1@test.com", "pass", Role.INSTRUCTOR, true)
        );
        when(userRepository.findAll()).thenReturn(allUsers);
        when(eventRepository.save(any(NotificationEvent.class))).thenReturn(new NotificationEvent());

        // Act
        notificationService.createSystemNotification(request);

        // Assert
        // Para notificaciones generales, el servicio ahora llama a findAll()
        verify(userRepository, times(1)).findAll();

        ArgumentCaptor<List<NotificationRecipient>> recipientCaptor = ArgumentCaptor.forClass(List.class);
        verify(recipientRepository, times(1)).saveAll(recipientCaptor.capture());

        assertEquals(2, recipientCaptor.getValue().size());
    }

    @Test
    void testGetUnreadNotificationsForUser() {
        // Arrange
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();

        // Configuramos el mock para que devuelva un Page con un NotificationRecipient
        NotificationRecipient mockRecipient = new NotificationRecipient();
        mockRecipient.setRead(false);
        mockRecipient.setEvent(new NotificationEvent());
        mockRecipient.setUser(new User());

        Page<NotificationRecipient> recipientPage = new PageImpl<>(List.of(mockRecipient));
        when(recipientRepository.findByUserIdAndIsReadFalseOrderByEventCreatedAtDesc(userId, pageable))
                .thenReturn(recipientPage);

        // Configuramos el mock del mapper para que convierta el objeto a DTO
        when(notificationMapper.toDTO(any(NotificationRecipient.class)))
                .thenReturn(new NotificationResponseDTO());

        // Act
        Page<NotificationResponseDTO> result = notificationService.getUnreadNotificationsForUser(userId, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(recipientRepository, times(1)).findByUserIdAndIsReadFalseOrderByEventCreatedAtDesc(userId, pageable);
        verify(notificationMapper, times(1)).toDTO(mockRecipient);
    }

    @Test
    void testMarkNotificationAsRead_Success() {
        // Arrange
        Long userId = 1L;
        Long eventId = 10L;
        NotificationRecipient recipient = new NotificationRecipient();
        recipient.setRead(false);

        // Para esta prueba, necesitamos un objeto completo con ID para el "findById"
        recipient.setId(new NotificationRecipientId(eventId, userId));

        when(recipientRepository.findById_UserIdAndId_EventId(userId, eventId))
                .thenReturn(Optional.of(recipient));

        // Act
        notificationService.markNotificationAsRead(userId, eventId);

        // Assert
        ArgumentCaptor<NotificationRecipient> captor = ArgumentCaptor.forClass(NotificationRecipient.class);
        verify(recipientRepository, times(1)).save(captor.capture());

        assertTrue(captor.getValue().isRead());
    }

    @Test
    void testMarkNotificationAsRead_WhenNotificationNotFound_ShouldThrowException() {
        // Arrange
        Long userId = 1L;
        Long eventId = 10L;
        // Simulamos que el repositorio no encuentra la notificación
        when(recipientRepository.findById_UserIdAndId_EventId(userId, eventId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.markNotificationAsRead(userId, eventId);
        });

        verify(recipientRepository, never()).save(any());
    }
}