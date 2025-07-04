package com.jaden_2.solar.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jaden_2.solar.backend.DTOs.sheets.MechSheet;
import com.jaden_2.solar.backend.DTOs.sheets.TechSheet;
import com.jaden_2.solar.backend.DTOs.sheets.WireSpec;
import com.jaden_2.solar.backend.entities.inventory.Panel;
import com.jaden_2.solar.backend.entities.enums.SWG;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * A detailed specification of solar array
 * @since 2025
 * @version 1.0
 * @author Sylvanus Jedidiah*/
@Data
@NoArgsConstructor
@Entity
public class ArraySpecs {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer arrayId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "username", name = "creator", nullable = false)
    @JsonIgnore
    private Creator creator;

    private String brand;
    private String model;
    private MechSheet mechanicalProperties;
    private TechSheet electricalProperties;
    @JsonIgnore
    private WireSpec wireSpecification;
    private Configuration configuration;
    private Double calculatePanelCapacity;

    public ArraySpecs(Panel panel, Configuration config, SWG gauge, double requirement){
        brand = panel.getBrand();
        model = panel.getModel();
        mechanicalProperties = new MechSheet(panel.getLenght(), panel.getWidth());
        wireSpecification = new WireSpec(gauge.name(), gauge.getDiameter(), gauge.getMaxCurrent());
        electricalProperties = new TechSheet(panel.getPower(), panel.getVmp(), panel.getType(), panel.getImp(), panel.getIsc(), panel.getVoc());
        configuration = config;
        calculatePanelCapacity = requirement;
    }
}
