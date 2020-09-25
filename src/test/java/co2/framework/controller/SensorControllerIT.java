package co2.framework.controller;

import co2.application.CO2Metric;
import co2.application.MetricsCollector;
import co2.application.SensorAlert;
import co2.domain.Status;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SensorControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MetricsCollector metricsCollector;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStatus() throws Exception {
        String id = "id";
        metricsCollector.collect(CO2Metric.builder()
                .co2(100)
                .time(LocalDateTime.now())
                .uuid(id)
                .build());

        String contentAsString = mockMvc.perform(get("/api/v1/sensors/{uuid}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        StatusResponse statusResponse = objectMapper.readValue(contentAsString, StatusResponse.class);

        assertThat(statusResponse.getStatus()).isEqualTo(Status.OK);
    }

    @Test
    void getMetrics() throws Exception {
        String id = "id";
        metricsCollector.collect(CO2Metric.builder()
                .co2(100)
                .time(LocalDateTime.now())
                .uuid(id)
                .build());
        metricsCollector.collect(CO2Metric.builder()
                .co2(200)
                .time(LocalDateTime.now())
                .uuid(id)
                .build());
        metricsCollector.collect(CO2Metric.builder()
                .co2(300)
                .time(LocalDateTime.now())
                .uuid(id)
                .build());

        String contentAsString = mockMvc.perform(get("/api/v1/sensors/{uuid}/metrics", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MetricsResponse metricsResponse = objectMapper.readValue(contentAsString, MetricsResponse.class);

        assertThat(metricsResponse.getMaxLast30Days()).isEqualTo(300);
        assertThat(metricsResponse.getAvgLast30Days()).isEqualTo((300 + 200 + 100) / 3);
    }

    @Test
    void getAlerts() throws Exception {
        String id = "id";
        CO2Metric c1 = CO2Metric.builder()
                .co2(11100)
                .time(LocalDateTime.now())
                .uuid(id)
                .build();
        metricsCollector.collect(c1);
        CO2Metric c2 = CO2Metric.builder()
                .co2(11200)
                .time(LocalDateTime.now())
                .uuid(id)
                .build();
        metricsCollector.collect(c2);
        CO2Metric c3 = CO2Metric.builder()
                .co2(42300)
                .time(LocalDateTime.now())
                .uuid(id)
                .build();
        metricsCollector.collect(c3);

        String contentAsString = mockMvc.perform(get("/api/v1/sensors/{uuid}/alerts", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<SensorAlert> alerts = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertThat(alerts).containsExactly(new SensorAlert(c1.getTime(), c3.getTime(), c1.getCo2(), c2.getCo2(), c3.getCo2()));
    }
}