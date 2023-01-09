package com.erdemnayin.websocket.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class CustomMessage {
    private String content;

}
