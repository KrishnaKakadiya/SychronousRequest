package com.example.adjust.restfuls;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NullHostNameVerifier implements HostnameVerifier
{
    public boolean verify(String hostname, SSLSession session)
    {
        return true;
    }
}