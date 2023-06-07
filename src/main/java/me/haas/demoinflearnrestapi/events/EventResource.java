package me.haas.demoinflearnrestapi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


//BeanSerializer
//Resource로 깔끔하게 처리
//TODO Resource<Event>(2.1.0 RELEASE) -> EntityModel(Event)(2.2.5 RELEASE)
public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links) {
        super(event, Arrays.asList(links));
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }


}
