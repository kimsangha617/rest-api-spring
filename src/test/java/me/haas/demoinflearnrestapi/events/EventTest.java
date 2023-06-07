package me.haas.demoinflearnrestapi.events;

import junitparams.JUnitParamsRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(JUnitParamsRunner.class)
class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring Rest API")
                .description("REST API development with Spring!")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        Event event = new Event();

        String name = "Event";
        String description = "Spring";
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

    private static Stream<Arguments> paramsForTestFree() {
        return Stream.of(
                Arguments.of(0, 0, true),
                Arguments.of(0, 100, false),
                Arguments.of(100, 0, false)
        );
    }

    private static Stream<Arguments> paramsForTestOffLine() {
        return Stream.of(
                Arguments.of("강남역 네이버 D2 스타텁 팩토리", true),
                Arguments.of("  ", false),
                Arguments.of(null, false)
        );
    }

    @ParameterizedTest
    @MethodSource("paramsForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //When
        event.update();
        //Then
        assertThat(event.isFree()).isEqualTo(isFree);

    }

    @ParameterizedTest
    @MethodSource("paramsForTestOffLine")
    public void testOffline(String location, boolean isOffLine) {
        Event event = Event.builder()
                .location(location)
                .build();

        //When
        event.update();
        assertThat(event.isOffline()).isEqualTo(isOffLine);

    }
}