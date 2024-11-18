package com.urlshortner.controller;

import com.urlshortner.service.ShortUrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlShortnerController
{
    private final ShortUrlService _shortUrlService;

    public UrlShortnerController(ShortUrlService shortUrlService)
    {
        _shortUrlService = shortUrlService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> CreateShortUrl(@RequestBody String originalUrl)
    {
        try
        {
            String shortUrl = _shortUrlService.shortenUrl(originalUrl); //should return the short url
            return ResponseEntity.ok(shortUrl);
        }
        catch(Exception ex)
        {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
        }
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<Void> redirect(@PathVariable String shortKey)
    {
        try
        {
            String originalUrl = _shortUrlService.getOriginalUrl(shortKey);
            if (originalUrl != null) {
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/health")
    public String health()
    {
        return "I am Running";
    }
}
