package com.example.nu_mad_sp2023_final_project_12.interfaces;

import android.net.Uri;

public interface SendImage
{
    public void send(Uri URI, String toUser, String toUserEmail,String currentConvo);
}