package com.grammr.language.speech.controller.dto;

public record TTSRequest(
    String text,
    double speed
){

}
