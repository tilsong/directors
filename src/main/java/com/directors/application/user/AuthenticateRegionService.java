package com.directors.application.user;

import com.directors.domain.region.Address;
import com.directors.domain.region.Region;
import com.directors.domain.region.RegionApiClient;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRegion;
import com.directors.domain.user.UserStatus;
import com.directors.infrastructure.exception.user.NoSuchUserException;
import com.directors.infrastructure.jpa.region.JpaRegionRepository;
import com.directors.infrastructure.jpa.user.JpaUserRegionRepository;
import com.directors.infrastructure.jpa.user.JpaUserRepository;
import com.directors.presentation.user.request.AuthenticateRegionRequest;
import com.directors.presentation.user.response.AuthenticateRegionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateRegionService {
    private final RegionApiClient regionApiClient;
    private final JpaRegionRepository regionRepository;
    private final JpaUserRegionRepository userRegionRepository;
    private final JpaUserRepository userRepository;

    @Transactional
    public AuthenticateRegionResponse authenticate(AuthenticateRegionRequest request, String userId) {
        var addressByApi = regionApiClient.findRegionAddressByLocation(request.latitude(), request.longitude());

        var region = regionRepository.findByAddressFullAddress(addressByApi.getFullAddress())
                .orElseThrow(NoSuchElementException::new);

        var userAddress = updateUserRegionAddress(userId, region);

        return new AuthenticateRegionResponse(userAddress.getFullAddress(), userAddress.getUnitAddress());
    }

    private Address updateUserRegionAddress(String userId, Region region) {
        if (userRegionRepository.existsByUserId(userId)) {
            return updateExistUserRegion(userId, region).getAddress();
        }
        return saveUserRegion(userId, region).getAddress();
    }

    private UserRegion saveUserRegion(String userId, Region region) {
        User user = userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(userId));
        var newUserRegion = UserRegion.of(region.getAddress(), user, region);

        UserRegion savedUserRegion = userRegionRepository.save(newUserRegion);
        return savedUserRegion;
    }

    private UserRegion updateExistUserRegion(String userId, Region region) {
        var userRegion = userRegionRepository.findByUserId(userId).get();

        userRegion.setAddress(region.getAddress());
        userRegion.setRegion(region);

        return userRegionRepository.save(userRegion);
    }

}
