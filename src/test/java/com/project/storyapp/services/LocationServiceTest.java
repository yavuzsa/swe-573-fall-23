package com.project.storyapp.services;

import com.project.storyapp.entities.Location;
import com.project.storyapp.repos.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    private LocationService locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        locationService = new LocationService(locationRepository);
    }

    @Test
    void testGetLocationByIdWithValidId() {
        Long locationId = 1L;
        Location expectedLocation = new Location();

        when(locationRepository.findById(locationId)).thenReturn(Optional.of(expectedLocation));

        Location result = locationService.getLocationById(locationId);

        assertEquals(expectedLocation, result);
        verify(locationRepository, times(1)).findById(locationId);
    }

    @Test
    void testGetLocationByIdWithInvalidId() {
        Long locationId = 1L;

        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        Location result = locationService.getLocationById(locationId);

        assertNull(result);
        verify(locationRepository, times(1)).findById(locationId);
    }
}
