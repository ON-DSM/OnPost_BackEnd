package com.onpost.domain.controller;

import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.google.gson.Gson;
import com.onpost.domain.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class EmailController {

    private final EmailService emailService;

    @Value("${cloud.aws.template}")
    private String TEMPLATE;

    @Value("${cloud.aws.mail}")
    private String SENDER;

    @PostMapping("/certified")
    public String Send(@RequestParam String email) {
        String code = RandomString.make(6);
        List<String> receivers = new ArrayList<>();
        receivers.add(email);

        Destination des = new Destination();
        des.setToAddresses(receivers);

        Map<String, String> data = new HashMap<>();
        data.put("code", code);
        String templateData = new Gson().toJson(data);

        SendTemplatedEmailRequest request = new SendTemplatedEmailRequest();
        request.setTemplate(TEMPLATE);
        request.setDestination(des);
        request.setTemplateData(templateData);
        request.setSource(SENDER);

        emailService.sendMail(request);
        return code;
    }
}
