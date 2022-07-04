package com.onpost.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageDto {

    private String message;
    private String roomId;
}
