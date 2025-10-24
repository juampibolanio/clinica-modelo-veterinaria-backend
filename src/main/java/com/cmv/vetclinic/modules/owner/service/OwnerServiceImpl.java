package com.cmv.vetclinic.modules.owner.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cmv.vetclinic.modules.owner.dto.OwnerRequest;
import com.cmv.vetclinic.modules.owner.dto.OwnerResponse;
import com.cmv.vetclinic.modules.owner.mapper.OwnerMapper;
import com.cmv.vetclinic.modules.owner.model.Owner;
import com.cmv.vetclinic.modules.owner.repository.OwnerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService {

        private final OwnerRepository ownerRepository;
        private final OwnerMapper ownerMapper;

        public OwnerResponse createOwner(OwnerRequest request) {
                Owner owner = Owner.builder()
                                .name(request.getName())
                                .surname(request.getSurname())
                                .email(request.getEmail())
                                .phoneNumber(request.getPhoneNumber())
                                .address(request.getAddress())
                                .documentNumber(request.getDocumentNumber())
                                .build();

                Owner savedOwner = ownerRepository.save(owner);
                return ownerMapper.toResponse(savedOwner);
        }

        @Override
        public OwnerResponse getOwnerById(Long id) {
                Owner owner = ownerRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Owner not found"));

                return ownerMapper.toResponse(owner);
        }

        @Override
        public List<OwnerResponse> getAllOwners(String name, String surname, String documentNumber,
                        Integer page, Integer size, String sortBy, String direction) {

                Sort sort = direction != null && direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();

                Pageable pageable = (page != null && size != null) ? PageRequest.of(page, size, sort)
                                : Pageable.unpaged();

                Specification<Owner> spec = OwnerSpecification.filterBy(name, surname, documentNumber);

                Page<Owner> ownersPage = ownerRepository.findAll(spec, pageable);

                return ownersPage.getContent().stream()
                                .map(ownerMapper::toResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public OwnerResponse updateOwner(Long id, OwnerRequest request) {
                Owner owner = ownerRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Owner not found"));

                owner.setName(request.getName());
                owner.setSurname(request.getSurname());
                owner.setEmail(request.getEmail());
                owner.setPhoneNumber(request.getPhoneNumber());
                owner.setAddress(request.getAddress());
                owner.setDocumentNumber(request.getDocumentNumber());

                Owner ownerUpdated = ownerRepository.save(owner);
                return ownerMapper.toResponse(ownerUpdated);
        }

        @Override
        @Transactional
        public OwnerResponse partialUpdateOwner(Long id, Map<String, Object> updates) {
                Owner owner = ownerRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Owner not found"));

                updates.forEach((key, value) -> {
                        switch (key) {
                                case "name" -> owner.setName((String) value);
                                case "surname" -> owner.setSurname((String) value);
                                case "email" -> owner.setEmail((String) value);
                                case "phoneNumber" -> owner.setPhoneNumber((String) value);
                                case "address" -> owner.setAddress((String) value);
                                case "documentNumber" -> owner.setDocumentNumber((String) value);
                                case "totalDebt" -> owner.setTotalDebt(Double.valueOf(value.toString()));
                                default -> {
                                       // ignore unknown fields
                                }
                        }
                });
                Owner ownerUpdated = ownerRepository.save(owner);
                return ownerMapper.toResponse(ownerUpdated);
        }

        @Override
        public void deleteOwner(Long id) {
                Owner owner = ownerRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Owner not found"));
                ownerRepository.delete(owner);
        }

}
