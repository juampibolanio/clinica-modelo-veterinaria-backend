package com.cmv.vetclinic.modules.appointment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cmv.vetclinic.modules.appointment.dto.AppointmentRequest;
import com.cmv.vetclinic.modules.appointment.dto.AppointmentResponse;
import com.cmv.vetclinic.modules.appointment.model.Appointment;
import com.cmv.vetclinic.modules.appointment.model.AppointmentStatus;
import com.cmv.vetclinic.modules.owner.model.Owner;
import com.cmv.vetclinic.modules.user.model.User;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "veterinarianId", target = "veterinarian.id")
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(source = "petId", target = "pet.id")
    @Mapping(target = "status", expression = "java(mapStatus(request.getStatus()))")
    Appointment toEntity(AppointmentRequest request);

    @Mapping(source = "veterinarian.id", target = "veterinarianId")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "pet.id", target = "petId")
    @Mapping(target = "veterinarianName", expression = "java(fullName(appt.getVeterinarian()))")
    @Mapping(target = "ownerName", expression = "java(ownerFullName(appt.getOwner()))")
    @Mapping(target = "petName", expression = "java(appt.getPet()!=null?appt.getPet().getName():null)")
    AppointmentResponse toResponse(Appointment appt);

    default AppointmentStatus mapStatus(String s) {
        if (s == null || s.isBlank()) return AppointmentStatus.PENDING;
        try { return AppointmentStatus.valueOf(s.toUpperCase()); }
        catch (IllegalArgumentException e){ return AppointmentStatus.PENDING; }
    }

    default String fullName(User u){ return (u==null)?null:(u.getName()+" "+u.getSurname()); }
    default String ownerFullName(Owner o){ return (o==null)?null:(o.getName()+" "+o.getSurname()); }
}
