package co2.framework.controller;

import co2.application.CO2Metric;
import co2.application.MetricsCollector;
import co2.application.MetricsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MetricsCollectorControllerIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void collectMetric() throws Exception {
        String sensorId="id";
        LocalDateTime time = LocalDateTime.now();
        mvc.perform(post("/api/v1/sensors/{uuid}/mesurements",sensorId)
                .content(objectMapper.writeValueAsString(CO2Metric.builder()
                        .co2(1000)
                        .time(time)
                        .build()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}