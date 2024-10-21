package com.hagsig.Hagsig;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class HagsigController {

    private final HagsigService hagsigService;

    public HagsigController(HagsigService hagsigService) {
        this.hagsigService = hagsigService;
    }
    @GetMapping("/api")
    @ResponseBody
    public String api() {
        return hagsigService.getJsonHagsig();
    }

//    @GetMapping("/api/announcement")
//    @ResponseBody
//    public List<Map<String, String>> getAnnouncementAsJson() {
//        return hagsigService.getAnnouncement();
//    }


}
