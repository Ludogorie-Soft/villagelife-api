package com.example.ludogoriesoft.village.services;

import com.example.ludogorieSoft.village.config.DatabaseUtils;
import com.example.ludogorieSoft.village.services.VillageGroundCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
class VillageGroundCategoryServiceTests {

    @InjectMocks
    private VillageGroundCategoryService villageGroundCategoryService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockStatic(DatabaseUtils.class);
    }

    @Test
    void testUpdateVillageGroundCategories() throws SQLException {
        long villageId = 1L;
        long groundCategoryId = 2L;

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        try {
            when(connection.prepareStatement(any())).thenReturn(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            when(DatabaseUtils.getConnection()).thenReturn(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        villageGroundCategoryService.updateVillageGroundCategories(villageId, groundCategoryId);

        verify(statement, times(1)).setLong(1, groundCategoryId);
        verify(statement, times(1)).setLong(2, villageId);
        verify(statement, times(1)).executeUpdate();
    }








}
