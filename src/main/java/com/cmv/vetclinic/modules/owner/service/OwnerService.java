package com.cmv.vetclinic.modules.owner.service;

import java.util.List;
import java.util.Map;

import com.cmv.vetclinic.modules.owner.dto.OwnerRequest;
import com.cmv.vetclinic.modules.owner.dto.OwnerResponse;

public interface OwnerService {

    OwnerResponse createOwner(OwnerRequest request);

    OwnerResponse getOwnerById(Long id);

    List<OwnerResponse> getAllOwners(String name, String surname, String documentNumber,
            Integer page, Integer size, String sortBy, String direction);

    OwnerResponse updateOwner(Long id, OwnerRequest request);

    OwnerResponse partialUpdateOwner(Long id, Map<String, Object> updates);

    void deleteOwner(Long id);
}
