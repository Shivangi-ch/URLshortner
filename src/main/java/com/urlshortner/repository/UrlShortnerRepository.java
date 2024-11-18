package com.urlshortner.repository;
import com.urlshortner.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class UrlShortnerRepository
{
    private final String _dbConnectionUrl;

    public UrlShortnerRepository(Config config)
    {
        _dbConnectionUrl = config.dbConnectionUrl;
    }

    public long InsertAndGetId(String originalUrl) throws Exception
    {
        try
        {
            long generatedId = -1;
            String sql = "INSERT INTO url_shortener (originalUrl) VALUES (?) RETURNING id";
            Connection connection = DriverManager.getConnection(_dbConnectionUrl);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, originalUrl);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next())
            {
                generatedId = resultSet.getLong("id");
            }
            return generatedId;
        }
        catch (Exception ex)
        {
            System.err.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    public boolean updateShortKey(long databaseId, String shortKey) throws Exception
    {
        try
        {
            String sql = "UPDATE url_shortener SET shortKey = ? WHERE id = ?";
            Connection connection = DriverManager.getConnection(_dbConnectionUrl);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, shortKey);
            preparedStatement.setLong(2, databaseId);

            int rowsUpdated = preparedStatement.executeUpdate();

            if(rowsUpdated >= 1) return true;
            else return false;
        }
        catch (Exception ex)
        {
            System.err.println("An error occurred while updating the short key: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }

    public String findOriginalUrlByShortKey(String shortKey) throws Exception
    {
        try
        {
            String originalUrl = null;
            String sql = "SELECT originalUrl FROM url_shortener WHERE shortKey = ?";
            Connection connection = DriverManager.getConnection(_dbConnectionUrl);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, shortKey);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                originalUrl = resultSet.getString("originalUrl");
            }
            return originalUrl;
        }
        catch (Exception ex)
        {
            System.err.println("An error occurred while finding the original URL: " + ex.getMessage());
            ex.printStackTrace();
            throw ex;
        }
    }
}

