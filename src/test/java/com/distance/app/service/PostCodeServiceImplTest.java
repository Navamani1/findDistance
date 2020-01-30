package com.distance.app.service;

import static com.distance.app.util.DistanceEnum.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.distance.app.domain.Distance;
import com.distance.app.entity.PostCodeEntity;
import com.distance.app.repository.PostCodeRepository;
import com.distance.app.util.DistanceEnum;
import com.distance.app.util.PostCodeException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class PostCodeServiceImplTest {

    @InjectMocks
    private PostCodeServiceImpl postCodeServiceImpl;

    @Mock
    private PostCodeRepository postCodeRepository;

    @Test
    public void calculateDistance() {
        when(postCodeRepository.findByPostCode("AB101XG")).thenReturn(formPostCodeEntity("AB101XG", 57.14416516, -2.114847768, 1L));
        when(postCodeRepository.findByPostCode("AB101XT")).thenReturn(formPostCodeEntity("AB101XT", 27.14416516, -2.114847767, 1L));

        Distance distance = postCodeServiceImpl.calculateDistance("AB101XG", "AB101XT");
        assertNotNull("distance is null", distance);
        assertEquals(3335.8477, distance.getDistance(), 0.0001);
        assertEquals("unit is not as expected", KM, distance.getUnit());
    }

    @Test
    public void getPostCodeDetails() {
        when(postCodeRepository.findByPostCode(anyString())).thenReturn(formPostCodeEntity("AB101XG", 57.14416516, -2.114847768, 1L));

        PostCodeEntity postCodeEntity = postCodeServiceImpl.getPostCodeDetails("AB101XG");
        assertNotNull("postCodeEntity is null", postCodeEntity);
        assertEquals("postCode is not as expected", "AB101XG", postCodeEntity.getPostCode());
        assertEquals(57.14416516, postCodeEntity.getLatitude(), 0.0001);
        assertEquals(-2.114847768, postCodeEntity.getLongitude(), 0.0001);
    }

    @Test
    public void getPostCodeDetailsById() {
        when(postCodeRepository.findById(anyLong())).thenReturn(Optional.of(formPostCodeEntity("1", 57.14416516, -2.114847768, 1L)));

        PostCodeEntity postCodeEntity = postCodeServiceImpl.getPostCodeDetails("1");
        assertNotNull("postCodeEntity is null", postCodeEntity);
        assertEquals("postCode is not as expected", "1", postCodeEntity.getPostCode());
        assertEquals(57.14416516, postCodeEntity.getLatitude(), 0.0001);
        assertEquals(-2.114847768, postCodeEntity.getLongitude(), 0.0001);
    }

    @Test
    public void getPostCodeDetails_Exception() {
        when(postCodeRepository.findByPostCode(anyString())).thenReturn(null);
        try {
            postCodeServiceImpl.getPostCodeDetails("AB101XG");
        } catch (PostCodeException e) {
            assertEquals("Exception is not as expected", "Given PostCode not available", e.getMessage());
        }
    }

    @Test
    public void getAllPostCodeDetails() {
        when(postCodeRepository.findAll()).thenReturn(Collections.singletonList(formPostCodeEntity("AB101XG", 57.14416516, -2.114847768, 1L)));

        List<PostCodeEntity> postCodeEntities = postCodeServiceImpl.getAllPostCodeDetails();
        assertEquals("postCodeEntity is not as expected", 1, postCodeEntities.size());
        assertEquals("postCode is not as expected", "AB101XG", postCodeEntities.get(0).getPostCode());
    }

    @Test
    public void getAllPostCodeDetails_Exception() {
        when(postCodeRepository.findAll()).thenReturn(Collections.emptyList());
        try {
            postCodeServiceImpl.getAllPostCodeDetails();
        } catch (PostCodeException e) {
            assertEquals("Exception is not as expected", "No PostCode details are available", e.getMessage());
        }
    }

    @Test
    public void savePostCodeDetails() {
        when(postCodeRepository.save(any(PostCodeEntity.class))).thenReturn(formPostCodeEntity("AB101XG", 57.14416516, -2.114847768, 1L));

        PostCodeEntity postCodeEntity = postCodeServiceImpl.savePostCodeDetails(new PostCodeEntity());
        assertNotNull("postCodeEntity is null", postCodeEntity);
        assertEquals("postCode is not as expected", "AB101XG", postCodeEntity.getPostCode());
        assertEquals(57.14416516, postCodeEntity.getLatitude(), 0.0001);
        assertEquals(-2.114847768, postCodeEntity.getLongitude(), 0.0001);
    }

    @Test
    public void deletePostCodeDetailsByPostCode() {
        when(postCodeRepository.findByPostCode(anyString())).thenReturn(formPostCodeEntity("AB101XG", 57.14416516, -2.114847768, 1L));

        postCodeServiceImpl.deletePostCodeDetailsByPostCode("AB101XG");
        verify(postCodeRepository).delete(any(PostCodeEntity.class));
    }

    @Test
    public void deletePostCodeDetailsById() {
        when(postCodeRepository.findById(anyLong())).thenReturn(Optional.of(formPostCodeEntity("AB101XG", 57.14416516, -2.114847768, 1L)));

        postCodeServiceImpl.deletePostCodeDetailsById(2L);
        verify(postCodeRepository).delete(any(PostCodeEntity.class));
    }

    private PostCodeEntity formPostCodeEntity(String postCode, double latitude, double longitude, Long id) {
        PostCodeEntity postCodeEntity = new PostCodeEntity();
        postCodeEntity.setPostCode(postCode);
        postCodeEntity.setLatitude(latitude);
        postCodeEntity.setLongitude(longitude);
        postCodeEntity.setId(id);
        return postCodeEntity;
    }

}