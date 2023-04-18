package com.example.nu_mad_sp2023_final_project_12;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Utils {
    public static String generateUniqueID(List<String> chatEmails) {
//        Using Java UUID (Unique User ID) generator utility to create the IDs from the list of user emails......
        Collections.sort(chatEmails);
        String uuid = UUID.nameUUIDFromBytes(
                chatEmails.toString().getBytes())
                .toString().substring(0,16);
        return uuid;
    }
}
