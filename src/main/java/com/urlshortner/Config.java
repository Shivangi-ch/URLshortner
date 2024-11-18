package com.urlshortner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config
{
    @Value("${app.config.dbConnectionUrl}")
    public String dbConnectionUrl;

    @Value("${app.server.url}")
    public String ServerUrl;
}
