package me.haas.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.haas.demoinflearnrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;
             //Mock 으로 만들어져 있는 DispatcherServlet 상대로 가짜 요청을 만들어서 DispatcherServlet으로 보내고
             //응답을 확인 할 수 있는 Test 를 만들수 있다, Web 과 관련된 것만 등록 해주기 때문에 Slicing Test라 한다
             //단위 테스트라 보기에는 너무 많은 것들이 개입 되어있다, DispatcherServlet 뿐만 아니라 EventController
             //DispatcherServlet이 가지고 있는 Handler, DataMapper, 여러가지 Converter 들 조합되서 테스트가 되기때문에..
             //MockMvc 클래스는 요청을 만들수 있고 응답을 검증할수 있는 Spring mvc Test에 있어 가장 핵심적인 클래스
             //MockMvc는 웹서버를 띄우지 않기 때문에 좀 더 빠르지만 DispatcherServlet 을 만들기 때문에 단위 테스트보단 빠르지 않다

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        Event event = Event.builder()
                        .id(100)
                        .name("Spring")
                        .beginEnrollmentDateTime(LocalDateTime.of(2023,05,01,16,00))
                        .closeEnrollmentDateTime(LocalDateTime.of(2023,05,02,16,00))
                        .beginEventDateTime(LocalDateTime.of(2023,05,05,10,00))
                        .endEventDateTime(LocalDateTime.of(2023,05,06,00,00))
                        .basePrice(10000)
                        .maxPrice(20000)
                        .limitOfEnrollment(100)
                        .location("강남 D2 스타터 팩토리")
                        .free(true)
                        .offline(false)
                        .eventStatus(EventStatus.PUBLISHED)
                        .build();

//        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON) //HyperTextApplicationLanguage
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated()) // isCreated() -> 201 response
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023,05,03,16,00))
                .closeEnrollmentDateTime(LocalDateTime.of(2023,05,02,16,00))
                .beginEventDateTime(LocalDateTime.of(2023,05,05,10,00))
                .endEventDateTime(LocalDateTime.of(2023,05,04,00,00))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남 D2 스타터 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }
}
