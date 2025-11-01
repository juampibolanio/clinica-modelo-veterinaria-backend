package com.cmv.vetclinic.modules.veterinaryStats.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {
    private long totalOwners;
    private long totalPets;
    private long totalProducts;
    private long vaccinesApplied; 
    private long appointmentsThisMonth; 
    private List<WeekAppointments> appointmentsByWeek;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeekAppointments {
        private String week; 
        private long count;
    }
}
