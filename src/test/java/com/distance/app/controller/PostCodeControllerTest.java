package com.distance.app.controller;

import static com.distance.app.util.DistanceEnum.KM;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.distance.app.domain.Distance;
import com.distance.app.entity.PostCodeEntity;
import com.distance.app.service.PostCodeServiceImpl;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

@RunWith(SpringRunner.class)
@WebMvcTest(PostCodeController.class)
public class PostCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostCodeServiceImpl paymentServiceImpl;

    @Test
    public void calculateDistance() throws Exception {
        when(paymentServiceImpl.calculateDistance(anyString(), anyString())).thenReturn(formDistance());

        mockMvc.perform(get("/uk/findDistance")
                        .param("origin", "AB101XG")
                        .param("destination", "AB101XT")
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.distance").value(20.34))
                        .andExpect(jsonPath("$.unit").value("KM"));
    }

    @Test
    public void getPostcode() throws Exception {
        when(paymentServiceImpl.getPostCodeDetails(anyString())).thenReturn(formPostCodeEntity());

        mockMvc.perform(get("/uk/postcode/AB101XG")
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.postCode").value("AB101XG"))
                        .andExpect(jsonPath("$.latitude").value(57.14416516))
                        .andExpect(jsonPath("$.longitude").value(-2.114847768));
    }

    @Test
    public void getAllClusterSettings() throws Exception {
        when(paymentServiceImpl.getAllPostCodeDetails()).thenReturn(Collections.singletonList(formPostCodeEntity()));

        mockMvc.perform(get("/uk/postcodes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.[0].postCode").value("AB101XG"));
    }

    @Test
    public void savePostCodeDetails() throws Exception {
        given(paymentServiceImpl.savePostCodeDetails(any(PostCodeEntity.class))).willReturn(formPostCodeEntity());

        Gson gson = new Gson();
        mockMvc.perform(MockMvcRequestBuilders.post("/uk/postcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(formPostCodeEntity())))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.postCode").value("AB101XG"))
                        .andExpect(jsonPath("$.latitude").value(57.14416516))
                        .andExpect(jsonPath("$.longitude").value(-2.114847768));
    }

    @Test
    public void deletePostCodeDetailsByPostCode() throws Exception {
        mockMvc.perform(delete("/uk/postcode")
                        .param("postcode", "AB101XG")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk());

        verify(paymentServiceImpl, times(1)).deletePostCodeDetailsByPostCode(anyString());
    }

    @Test
    public void deletePostCodeDetailsById() throws Exception {
        mockMvc.perform(delete("/uk/postcode")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk());

        verify(paymentServiceImpl, times(1)).deletePostCodeDetailsById(anyLong());
    }

    @Test
    public void deletePostCodeDetails_Exception() throws Exception {
        try {
            mockMvc.perform(delete("/uk/postcode")
                            .contentType(MediaType.APPLICATION_JSON_VALUE));
        } catch (Exception e) {
            assertNotNull("Exception message is not as expected", e.getMessage());
        }
    }

    private PostCodeEntity formPostCodeEntity() {
        PostCodeEntity postCodeEntity = new PostCodeEntity();
        postCodeEntity.setPostCode("AB101XG");
        postCodeEntity.setLatitude(57.14416516);
        postCodeEntity.setLongitude(-2.114847768);
        postCodeEntity.setId(1L);
        return postCodeEntity;
    }

    private Distance formDistance() {
        Distance distance = new Distance();
        distance.setDistance(20.34);
        distance.setUnit(KM);
        return distance;
    }

}