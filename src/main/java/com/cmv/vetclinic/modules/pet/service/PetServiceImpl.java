package com.cmv.vetclinic.modules.pet.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cmv.vetclinic.exceptions.OwnerExceptions.OwnerNotFoundException;
import com.cmv.vetclinic.exceptions.PetExceptions.InvalidPetDataException;
import com.cmv.vetclinic.exceptions.PetExceptions.PetNotFoundException;
import com.cmv.vetclinic.modules.appliedVaccine.repository.AppliedVaccineRepository;
import com.cmv.vetclinic.modules.appointment.model.Appointment;
import com.cmv.vetclinic.modules.appointment.repository.AppointmentRepository;
import com.cmv.vetclinic.modules.clinicalHistory.repository.ClinicalHistoryRepository;
import com.cmv.vetclinic.modules.owner.model.Owner;
import com.cmv.vetclinic.modules.owner.repository.OwnerRepository;
import com.cmv.vetclinic.modules.pet.dto.PetRequest;
import com.cmv.vetclinic.modules.pet.dto.PetResponse;
import com.cmv.vetclinic.modules.pet.mapper.PetMapper;
import com.cmv.vetclinic.modules.pet.model.Pet;
import com.cmv.vetclinic.modules.pet.model.Genders;
import com.cmv.vetclinic.modules.pet.repository.PetRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;
    private final PetMapper petMapper;
    private final AppliedVaccineRepository appliedVaccineRepository;
    private final ClinicalHistoryRepository clinicalHistoryRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public PetResponse createPet(PetRequest request) {
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new OwnerNotFoundException(request.getOwnerId()));

        if (request.getBirthDate().isAfter(LocalDate.now())) {
            throw new InvalidPetDataException("Birth date cannot be in the future");
        }

        if (request.getWeight() < 0) {
            throw new InvalidPetDataException("Weight cannot be negative");
        }

        Pet pet = petMapper.toEntity(request);
        pet.setOwner(owner);

        Pet savedPet = petRepository.save(pet);

        return petMapper.toResponse(savedPet);
    }

    @Override
    public PetResponse getPetById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));
        return petMapper.toResponse(pet);
    }

    @Override
    public Page<PetResponse> getAllPets(Integer page,
            Integer size,
            String sortBy,
            String direction,
            String name,
            String species,
            String breed,
            String gender,
            Long ownerId,
            Integer ageMin,
            Integer ageMax,
            Double weightMin,
            Double weightMax) {

        int p = (page == null) ? 0 : Math.max(page, 0);
        int s = (size == null) ? 10 : Math.max(size, 0);
        String sortField = (sortBy == null || sortBy.isBlank()) ? "id" : sortBy;
        Sort sort = ("desc".equalsIgnoreCase(direction)) ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(p, s, sort);

        if (ageMin != null && ageMax != null && ageMin > ageMax) {
            int tmp = ageMin;
            ageMin = ageMax;
            ageMax = tmp;
        }
        if (weightMin != null && weightMax != null && weightMin > weightMax) {
            double tmp = weightMin;
            weightMin = weightMax;
            weightMax = tmp;
        }

        Specification<Pet> spec = PetSpecification.filterBy(
                name, species, breed, gender, ownerId, ageMin, ageMax, weightMin, weightMax);

        Page<Pet> pageEntities = petRepository.findAll(spec, pageable);
        return pageEntities.map(petMapper::toResponse);
    }

    @Override
    public List<PetResponse> getPetsByOwnerId(Long ownerId) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new OwnerNotFoundException(ownerId));

        List<Pet> pets = petRepository.findAllByOwnerId(owner.getId());

        return pets.stream()
                .map(petMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PetResponse updatePet(Long id, PetRequest request) {

        if (request.getBirthDate().isAfter(LocalDate.now())) {
            throw new InvalidPetDataException("Birth date cannot be in the future");
        }

        if (request.getWeight() < 0) {
            throw new InvalidPetDataException("Weight cannot be negative");
        }

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setBirthDate(request.getBirthDate());
        pet.setGender(Genders.valueOf(request.getGender().toUpperCase()));
        pet.setColor(request.getColor());
        pet.setWeight(request.getWeight());
        pet.setAllergies(request.getAllergies());
        pet.updateAge();

        if (!pet.getOwner().getId().equals(request.getOwnerId())) {
            Owner owner = ownerRepository.findById(request.getOwnerId())
                    .orElseThrow(() -> new OwnerNotFoundException(request.getOwnerId()));
            pet.setOwner(owner);
        }

        Pet updatedPet = petRepository.save(pet);
        return petMapper.toResponse(updatedPet);
    }

    @Override
    @Transactional
    public PetResponse partialUpdatePet(Long id, Map<String, Object> updates) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> pet.setName((String) value);
                case "species" -> pet.setSpecies((String) value);
                case "breed" -> pet.setBreed((String) value);
                case "color" -> pet.setColor((String) value);
                case "weight" -> pet.setWeight(Double.valueOf(value.toString()));
                case "allergies" -> pet.setAllergies((String) value);
                case "gender" -> {
                    try {
                        pet.setGender(Genders.valueOf(value.toString().toUpperCase()));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
                case "birthDate" -> {
                    try {
                        LocalDate date = LocalDate.parse((String) value);
                        pet.setBirthDate(date);
                        pet.updateAge();
                    } catch (Exception ignored) {
                    }
                }
                default -> {
                }
            }
        });

        Pet saved = petRepository.save(pet);
        return petMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deletePet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        // 🧩 1. Eliminar vacunas aplicadas de esta mascota
        appliedVaccineRepository.deleteAllByPetId(id);

        // 🧩 2. Eliminar historias clínicas de esta mascota
        clinicalHistoryRepository.deleteAllByPetId(id);

        // 3️⃣ Eliminar turnos (appointments)
        appointmentRepository.deleteAllByPetId(id);

        // 🧩 3. Eliminar la mascota
        petRepository.delete(pet);
    }

}