package com.funnco.petcheckserver.dto;

import com.funnco.petcheckserver.dto.request.RequestLocationDto;
import com.funnco.petcheckserver.dto.request.RequestUserDto;
import com.funnco.petcheckserver.entity.VisitedLocation;
import com.funnco.petcheckserver.dto.response.ResponseAnimalDto;
import com.funnco.petcheckserver.dto.response.ResponseLocationDto;
import com.funnco.petcheckserver.dto.response.ResponseUserDto;
import com.funnco.petcheckserver.dto.response.ResponseVisitedLocationDto;
import com.funnco.petcheckserver.entity.*;

public class DtoMapper {

    public static ResponseLocationDto mapEntityLocationToResponseDto(Location location){
        return new ResponseLocationDto(
                location.getId(),
                location.getLatitude(),
                location.getLongitude()
        );
    }

    public static Location mapRequestLocationDtoToEntity(RequestLocationDto dto){
        Location location = new Location();
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        return location;
    }

    public static ResponseUserDto mapEntityUserToResponseDto(User user){
        return new ResponseUserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }

    public static User mapRequestUserDtoToEntity(RequestUserDto dto){
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static ResponseAnimalDto mapEntityAnimalToDto(Animal animal) {
        return new ResponseAnimalDto(
                animal.getId(),
                animal.getAnimalTypes().stream().map(AnimalType::getId).toList(),
                animal.getWeight(),
                animal.getLength(),
                animal.getHeight(),
                animal.getGender(),
                animal.getLifeStatus(),
                animal.getChippingDateTime(),
                animal.getChipper().getId(),
                animal.getChippingLocation().getId(),
                animal.getVisitedLocations().stream().map(VisitedLocation::getId).toList(),
                animal.getDeathDateTime()
        );

    }
    public static ResponseVisitedLocationDto mapEntityVisitedLocationToDto(VisitedLocation visitedLocation){
        return new ResponseVisitedLocationDto(
                visitedLocation.getId(),
                visitedLocation.getDateTimeOfVisitLocationPoint(),
                visitedLocation.getLocationPoint().getId()
        );
    }
}
