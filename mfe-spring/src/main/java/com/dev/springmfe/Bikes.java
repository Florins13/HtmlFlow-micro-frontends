package com.dev.springmfe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
public class Bikes {

    @Value("${api.url}")
    private String apiUrl;

    @CrossOrigin(origins = "*")
    @GetMapping("/bikes")
    public String getBikes(Model model) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        String externalUrl = apiUrl;

        String responseData = restTemplate.getForObject(externalUrl, String.class);

        ObjectMapper mapper = new ObjectMapper();
        List<Bike> bikes = mapper.readValue(responseData, new TypeReference<>() {
        });
        model.addAttribute("bikes", bikes);
        return "bikes";
    }
}
