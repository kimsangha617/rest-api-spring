package me.haas.demoinflearnrestapi.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;
             //Mock 으로 만들어져 있는 DispatcherServlet 상대로 가짜 요청을 만들어서 DispatcherServlet으로 보내고
             //응답을 확인 할 수 있는 Test 를 만들수 있다, Web 과 관련된 것만 등록 해주기 때문에 Slicing Test라 한다
             //단위 테스트라 보기에는 너무 많은 것들이 개입 되어있다, DispatcherServlet 뿐만 아니라 EventController
             //DispatcherServlet이 가지고 있는 Handler, DataMapper, 여러가지 Converter 들 조합되서 테스트가 되기때문에..
             //MockMvc 클래스는 요청을 만들수 있고 응답을 검증할수 있는 Spring mvc Test에 있어 가장 핵심적인 클래스
             //MockMvc는 웹서버를 띄우지 않기 때문에 좀 더 빠르지만 DispatcherServlet 을 만들기 때문에 단위 테스트보단 빠르지 않다

    @Test
    public void createEvent() throws Exception {
        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON) //HyperTextApplicationLanguage
                        )
                .andExpect(status().isCreated()); // isCreated() -> 201 response
    }


}
