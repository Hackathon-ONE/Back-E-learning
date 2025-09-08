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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    /**
     * Prueba el caso en que se crea una notificación del sistema dirigida
     * exclusivamente a usuarios con el rol de ESTUDIANTE.
     * <p>
     * Se verifica que el método de repositorio 'findByRole' sea llamado una
     * vez con el rol 'STUDENT' y que la cantidad de destinatarios guardados
     * coincida con el número de estudiantes simulados.
     */
    @Test
    void testCreateSystemNotification_ForStudentsOnly() {
        // Preparación (Arrange)
        SystemNotificationRequestDTO request = new SystemNotificationRequestDTO();
        request.setContent("Notification for students");
        request.setNotificationType(NotificationType.NOTIF_STUDENTS);

        // Se simulan los usuarios estudiantes
        User student1 = new User();
        student1.setId(1L);
        student1.setFullName("Student 1");
        student1.setEmail("s1@test.com");
        student1.setPasswordHash("pass");
        student1.setRole(Role.STUDENT);
        student1.setActive(true);

        User student2 = new User();
        student2.setId(2L);
        student2.setFullName("Student 2");
        student2.setEmail("s2@test.com");
        student2.setPasswordHash("pass");
        student2.setRole(Role.STUDENT);
        student2.setActive(true);

        List<User> studentList = List.of(student1, student2);

        when(userRepository.findByRole(Role.STUDENT)).thenReturn(studentList);
        when(eventRepository.save(any(NotificationEvent.class))).thenReturn(new NotificationEvent());

        // Acción (Act)
        notificationService.createSystemNotification(request);

        // Verificación (Assert)
        verify(userRepository, times(1)).findByRole(Role.STUDENT);
        ArgumentCaptor<List<NotificationRecipient>> recipientCaptor = ArgumentCaptor.forClass(List.class);
        verify(recipientRepository, times(1)).saveAll(recipientCaptor.capture());
        assertEquals(2, recipientCaptor.getValue().size());
    }

    /**
     * Prueba el caso en que se crea una notificación del sistema dirigida
     * exclusivamente a usuarios con el rol de INSTRUCTOR.
     * <p>
     * Se verifica que el método 'findByRole' sea llamado con el rol 'INSTRUCTOR'
     * y que la cantidad de destinatarios guardados sea la correcta.
     */
    @Test
    void testCreateSystemNotification_ForInstructorsOnly() {
        // Preparación
        SystemNotificationRequestDTO request = new SystemNotificationRequestDTO();
        request.setContent("Notification for instructors");
        request.setNotificationType(NotificationType.NOTIF_INSTRUCTORS);

        // Se simula un usuario instructor
        User instructor = new User();
        instructor.setId(3L);
        instructor.setFullName("Instructor 1");
        instructor.setEmail("i1@test.com");
        instructor.setPasswordHash("pass");
        instructor.setRole(Role.INSTRUCTOR);
        instructor.setActive(true);

        List<User> instructorList = List.of(instructor);

        when(userRepository.findByRole(Role.INSTRUCTOR)).thenReturn(instructorList);
        when(eventRepository.save(any(NotificationEvent.class))).thenReturn(new NotificationEvent());

        // Acción
        notificationService.createSystemNotification(request);

        // Verificación
        verify(userRepository, times(1)).findByRole(Role.INSTRUCTOR);
        ArgumentCaptor<List<NotificationRecipient>> recipientCaptor = ArgumentCaptor.forClass(List.class);
        verify(recipientRepository, times(1)).saveAll(recipientCaptor.capture());
        assertEquals(1, recipientCaptor.getValue().size());
    }

    /**
     * Prueba el caso en que se crea una notificación del sistema para
     * la audiencia general (todos los usuarios).
     * <p>
     * Se verifica que el método 'findAll' sea llamado para obtener a todos
     * los usuarios y que la cantidad de destinatarios guardados sea la
     * correspondiente.
     */
    @Test
    void testCreateSystemNotification_ForGeneralAudience() {
        // Preparación
        SystemNotificationRequestDTO request = new SystemNotificationRequestDTO();
        request.setContent("General notification");
        request.setNotificationType(NotificationType.NOTIF_GENERAL);

        // Se simulan un estudiante y un instructor para la audiencia general
        User student1 = new User();
        student1.setId(1L);
        student1.setFullName("Student 1");
        student1.setEmail("s1@test.com");
        student1.setPasswordHash("pass");
        student1.setRole(Role.STUDENT);
        student1.setActive(true);

        User instructor1 = new User();
        instructor1.setId(2L);
        instructor1.setFullName("Instructor 1");
        instructor1.setEmail("i1@test.com");
        instructor1.setPasswordHash("pass");
        instructor1.setRole(Role.INSTRUCTOR);
        instructor1.setActive(true);

        List<User> allUsers = List.of(student1, instructor1);

        when(userRepository.findAll()).thenReturn(allUsers);
        when(eventRepository.save(any(NotificationEvent.class))).thenReturn(new NotificationEvent());

        // Acción
        notificationService.createSystemNotification(request);

        // Verificación
        verify(userRepository, times(1)).findAll();
        ArgumentCaptor<List<NotificationRecipient>> recipientCaptor = ArgumentCaptor.forClass(List.class);
        verify(recipientRepository, times(1)).saveAll(recipientCaptor.capture());
        assertEquals(2, recipientCaptor.getValue().size());
    }

    /**
     * Prueba el caso en que un usuario solicita ver sus notificaciones no leídas.
     * <p>
     * Se verifica que el método del repositorio se llame con el ID de usuario
     * correcto y que el mapeador convierta la entidad a un DTO de respuesta.
     */
    @Test
    void testGetUnreadNotificationsForUser() {
        // Preparación
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();

        // Se simula un destinatario de notificación no leído
        NotificationRecipient mockRecipient = new NotificationRecipient();
        mockRecipient.setRead(false);
        mockRecipient.setEvent(new NotificationEvent());
        mockRecipient.setUser(new User());

        Page<NotificationRecipient> recipientPage = new PageImpl<>(List.of(mockRecipient));
        when(recipientRepository.findByUserIdAndIsReadFalseOrderByEventCreatedAtDesc(userId, pageable))
                .thenReturn(recipientPage);
        when(notificationMapper.toDTO(any(NotificationRecipient.class)))
                .thenReturn(new NotificationResponseDTO());

        // Acción
        Page<NotificationResponseDTO> result = notificationService.getUnreadNotificationsForUser(userId, pageable);

        // Verificación
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(recipientRepository, times(1)).findByUserIdAndIsReadFalseOrderByEventCreatedAtDesc(userId, pageable);
        verify(notificationMapper, times(1)).toDTO(mockRecipient);
    }

    /**
     * Prueba el caso en que se marca una notificación como leída con éxito.
     * <p>
     * Se verifica que el servicio localice el destinatario correcto y actualice
     * su estado 'isRead' a verdadero. Se utiliza un ArgumentCaptor para
     * confirmar que el objeto guardado tiene el estado correcto.
     */
    @Test
    void testMarkNotificationAsRead_Success() {
        // Preparación
        Long userId = 1L;
        Long eventId = 10L;
        NotificationRecipient recipient = new NotificationRecipient();
        recipient.setRead(false);
        recipient.setId(new NotificationRecipientId(eventId, userId));

        when(recipientRepository.findById_UserIdAndId_EventId(userId, eventId))
                .thenReturn(Optional.of(recipient));

        // Acción
        notificationService.markNotificationAsRead(userId, eventId);

        // Verificación
        ArgumentCaptor<NotificationRecipient> captor = ArgumentCaptor.forClass(NotificationRecipient.class);
        verify(recipientRepository, times(1)).save(captor.capture());
        assertTrue(captor.getValue().isRead());
    }

    /**
     * Prueba que el servicio lanza una `ResourceNotFoundException`
     * cuando se intenta marcar como leída una notificación que no existe.
     * <p>
     * Se verifica que el método 'save' del repositorio no sea llamado en este caso.
     */
    @Test
    void testMarkNotificationAsRead_WhenNotificationNotFound_ShouldThrowException() {
        // Preparación
        Long userId = 1L;
        Long eventId = 10L;
        when(recipientRepository.findById_UserIdAndId_EventId(userId, eventId))
                .thenReturn(Optional.empty());

        // Acción y Verificación
        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.markNotificationAsRead(userId, eventId);
        });

        verify(recipientRepository, never()).save(any());
    }

    /**
     * Prueba el caso en que un usuario solicita notificaciones pero no tiene ninguna
     * notificación no leída.
     * <p>
     * Se verifica que el servicio devuelva una página vacía y que no se intente
     * mapear ninguna entidad a DTOs.
     */
    @Test
    void testGetUnreadNotificationsForUser_NoNotificationsFound() {
        // Preparación
        Long userId = 1L;
        Pageable pageable = Pageable.unpaged();
        when(recipientRepository.findByUserIdAndIsReadFalseOrderByEventCreatedAtDesc(userId, pageable))
                .thenReturn(Page.empty());

        // Acción
        Page<NotificationResponseDTO> result = notificationService.getUnreadNotificationsForUser(userId, pageable);

        // Verificación
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(recipientRepository, times(1)).findByUserIdAndIsReadFalseOrderByEventCreatedAtDesc(userId, pageable);
        verify(notificationMapper, never()).toDTO(any(NotificationRecipient.class));
    }
}
