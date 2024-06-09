package it.unisalento.pasproject.resourceservice.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.unisalento.pasproject.resourceservice.domain.Availability;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AvailabilityDeserializer extends JsonDeserializer<List<Availability>> {
    @Override
    public List<Availability> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        var treeNode = p.getCodec().readTree(p);
        var availabilityList = new ArrayList<Availability>();

        if (treeNode instanceof ArrayNode arrayNode) {
            for (JsonNode node : arrayNode) {
                var availability = deserializeAvailability(node);
                availabilityList.add(availability);
            }
        }

        return availabilityList;
    }

    private Availability deserializeAvailability(JsonNode node) {
        var availability = new Availability();

        availability.setDayOfWeek(deserializeDayOfWeek(node));
        availability.setStartTime(deserializeStartTime(node));
        availability.setEndTime(deserializeEndTime(node));

        return availability;
    }

    private DayOfWeek deserializeDayOfWeek(JsonNode node) {
        var dayOfWeekString = node.get("dayOfWeek").asText();
        if (!dayOfWeekString.isEmpty()) {
            return DayOfWeek.valueOf(dayOfWeekString.toUpperCase());
        }
        return null;
    }

    private LocalTime deserializeStartTime(JsonNode node) {
        var startTimeString = node.get("startTime").asText();
        if (!startTimeString.isEmpty()) {
            return LocalTime.parse(startTimeString);
        }
        return null;
    }

    private LocalTime deserializeEndTime(JsonNode node) {
        var endTimeString = node.get("endTime").asText();
        if (!endTimeString.isEmpty()) {
            return LocalTime.parse(endTimeString);
        }
        return null;
    }
}
