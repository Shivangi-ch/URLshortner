package com.urlshortner.service;

import com.urlshortner.Config;
import com.urlshortner.repository.UrlShortnerRepository;
import org.springframework.stereotype.Service;

@Service
public class ShortUrlService
{
    private final UrlShortnerRepository _urlShortnerRepository;
    private final String _serverUrl;

    public ShortUrlService(UrlShortnerRepository urlShortnerRepository, Config config)
    {
        _urlShortnerRepository = urlShortnerRepository;
        _serverUrl = config.ServerUrl;
    }

    public String shortenUrl(String originalUrl) throws Exception
    {
        try {
            long databaseId = _urlShortnerRepository.InsertAndGetId(originalUrl);
            String shortKey = encodeBase62(databaseId);
            String shortUrl = _serverUrl + shortKey;
            boolean updated = _urlShortnerRepository.updateShortKey(databaseId, shortKey);
            if(updated == true) {
                return shortUrl;
            }
            else {
                throw new Exception("Failed to update shortkey in DB");
            }
        }
        catch (Exception ex)
        {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    public String getOriginalUrl(String shortKey) throws Exception
    {
        try
        {
            return _urlShortnerRepository.findOriginalUrlByShortKey(shortKey);
        }
        catch (Exception ex)
        {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    private String encodeBase62(long id)
    {
        String base62Chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String shortKey = "";
        while (id > 0) {
            shortKey =  shortKey + (base62Chars.charAt((int) (id % 62)));
            id /= 62;
        }
        return new StringBuilder(shortKey).reverse().toString();
    }
}
