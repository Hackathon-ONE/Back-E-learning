package learning.platform.mapper;

import learning.platform.dto.NotificationResponseDTO;
import learning.platform.entity.NotificationRecipient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    // MapStruct uses "dot notation" to access fields of nested objects.
    // source = "event.id" means: "get the 'event' object and then get its 'id' field".
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.notificationType", target = "notificationType")
    @Mapping(source = "event.content", target = "content")
    @Mapping(source = "event.createdAt", target = "createdAt")
    @Mapping(source = "read", target = "read")
    NotificationResponseDTO toDTO(NotificationRecipient recipient);
}