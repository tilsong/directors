package com.directors.presentation.region;

import com.directors.application.region.RegionService;
import com.directors.presentation.region.response.NearestAddressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/region")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/nearestAddress/{distance}")
    public ResponseEntity<NearestAddressResponse> getNearestAddress(@PathVariable int distance, @AuthenticationPrincipal String userIdByToken) {
        var nearestAddress = regionService.getNearestAddress(userIdByToken, distance);
        return new ResponseEntity<>(new NearestAddressResponse(nearestAddress), HttpStatus.OK);
    }
}
