package com.Comunicator.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertData {

    public static String convertLocalDatTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = localDateTime.format(formatter);
        return formattedDateTime;
    }
}
