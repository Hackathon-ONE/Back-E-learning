package learning.platform.controller;

import learning.platform.dto.MaterialCreateRequest;
import learning.platform.dto.MaterialResponse;
import learning.platform.dto.MaterialUpdateRequest;
import learning.platform.enums.ContentType;
import learning.platform.service.MaterialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterialControllerTest {

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private MaterialController materialController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMaterial() {
        Long lessonId = 1L;
        MaterialCreateRequest request = new MaterialCreateRequest(
                lessonId, "Video de prueba", "https://example.com/video.mp4", ContentType.VIDEO
        );

        MaterialResponse mockResponse = new MaterialResponse(1L, lessonId, "Video de prueba", "https://example.com/video.mp4", ContentType.VIDEO);

        when(materialService.createMaterial(lessonId, request)).thenReturn(mockResponse);

        ResponseEntity<MaterialResponse> responseEntity = materialController.createMaterial(lessonId, request);

        assertEquals(201, responseEntity.getStatusCode().value());
        assertEquals(mockResponse, responseEntity.getBody());
        verify(materialService, times(1)).createMaterial(lessonId, request);
    }

    @Test
    void testGetMaterialsByLesson() {
        Long lessonId = 1L;
        Long userId = 42L;

        MaterialResponse material1 = new MaterialResponse(1L, lessonId, "Material 1", "url1", ContentType.PDF);
        MaterialResponse material2 = new MaterialResponse(2L, lessonId, "Material 2", "url2", ContentType.VIDEO);

        when(materialService.getMaterialsByLesson(lessonId, userId)).thenReturn(List.of(material1, material2));

        ResponseEntity<List<MaterialResponse>> response = materialController.getMaterialsByLesson(lessonId, userId);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(materialService, times(1)).getMaterialsByLesson(lessonId, userId);
    }

    @Test
    void testUpdateMaterial() {
        Long materialId = 1L;
        MaterialUpdateRequest request = new MaterialUpdateRequest("Nuevo título", "https://example.com/nuevo.mp4", ContentType.VIDEO);

        MaterialResponse updated = new MaterialResponse(materialId, 1L, "Nuevo título", "https://example.com/nuevo.mp4", ContentType.VIDEO);

        when(materialService.updateMaterial(materialId, request)).thenReturn(updated);

        ResponseEntity<MaterialResponse> response = materialController.updateMaterial(materialId, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(updated, response.getBody());
        verify(materialService, times(1)).updateMaterial(materialId, request);
    }

    @Test
    void testDeleteMaterial() {
        Long materialId = 1L;

        doNothing().when(materialService).deleteMaterial(materialId);

        ResponseEntity<Void> response = materialController.deleteMaterial(materialId);

        assertEquals(204, response.getStatusCode().value());
        verify(materialService, times(1)).deleteMaterial(materialId);
    }

    @Test
    void testGetMaterialById() {
        Long materialId = 1L;
        Long userId = 42L;

        MaterialResponse mockResponse = new MaterialResponse(materialId, 1L, "Material Test", "url", ContentType.VIDEO);
        when(materialService.getMaterialById(materialId, userId)).thenReturn(mockResponse);

        ResponseEntity<MaterialResponse> response = materialController.getMaterialById(materialId, userId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(mockResponse, response.getBody());
        verify(materialService, times(1)).getMaterialById(materialId, userId);
    }

    // ------------------- CASOS DE EXCEPCIÓN -------------------

    @Test
    void testCreateMaterialThrowsException() {
        Long lessonId = 999L;
        MaterialCreateRequest request = new MaterialCreateRequest(
                lessonId, "Video fallo", "url", ContentType.VIDEO
        );

        when(materialService.createMaterial(lessonId, request)).thenThrow(new IllegalArgumentException("Lección no encontrada"));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> materialController.createMaterial(lessonId, request));

        assertEquals("Lección no encontrada", thrown.getMessage());
        verify(materialService, times(1)).createMaterial(lessonId, request);
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Error de prueba");
        ResponseEntity<String> response = materialController.handleIllegalArgumentException(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Error de prueba", response.getBody());
    }
}
