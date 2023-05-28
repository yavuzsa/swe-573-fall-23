package com.project.storyapp.services;


import com.project.storyapp.entities.Location;
import com.project.storyapp.entities.User;
import com.project.storyapp.repos.UserRepository;
import com.project.storyapp.repos.LocationRepository;
import org.springframework.stereotype.Service;

@Service
public class LocationService {


    private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationById(Long locationId) {
        return locationRepository.findById(locationId).orElse(null);
    }



}
