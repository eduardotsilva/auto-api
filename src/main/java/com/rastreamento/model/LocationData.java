package com.rastreamento.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LocationData {
    private String vehicleId;
    private double latitude;
    private double longitude;
    private double speed;
    private LocalDateTime timestamp;
} 