package com.jaden_2.solar.backend.services;

import com.jaden_2.solar.backend.entities.Configuration;
import com.jaden_2.solar.backend.entities.ControllerSpecs;
import com.jaden_2.solar.backend.entities.inventory.ChargeController;
import com.jaden_2.solar.backend.exceptions.ChargeControllerNotFoundException;
import com.jaden_2.solar.backend.repositories.ChargeControllerRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.hibernate.action.internal.EntityActionVetoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ControllerService {
    private final ChargeControllerRepo repo;

    /**
     * Recommends charge controller and a suitable configuration based on what's available in the database
     * @param arrayPower Total power_w of the solar array (Wp)
     * @param maxVolts Total open circuit voltage of the solar array
     * @return ControllerSpecs A spec sheet for the charge controller*/
    public ControllerSpecs evaluateController(double arrayPower, double maxVolts){
        double controllerSize = SystemEstimator.estimateController(arrayPower, maxVolts); // estimates the capacity of the charge controller
        controllerSize = (int) Math.ceil(controllerSize);
        ChargeController controller;
        // check if there is a charge controller that perfectly fits the capacity recommended
        // if not make use of what is available and connect in parallel to meet capacity
        try{
            controller = repo.findTopByMaxChargeCurrentGreaterThanEqualOrderByMaxChargeCurrentAsc(controllerSize)
                    .orElseThrow(ChargeControllerNotFoundException::new);
            return new ControllerSpecs(controller, controllerSize ,new Configuration(1, 1));
        }catch (ChargeControllerNotFoundException e){
            controller = getSmallerController(controllerSize);
            int parallel = (int) Math.ceil(controllerSize/controller.getMaxChargeCurrent());
            return new ControllerSpecs(controller, controllerSize ,new Configuration(1, parallel));
        }
    }

    public ChargeController getSmallerController(double capacity){
        return repo.findTopByMaxChargeCurrentLessThanEqualOrderByMaxChargeCurrentAsc(capacity);
    }

    public ChargeController getControllerById(Integer id) throws EntityActionVetoException {
        return repo.findById(id).orElseThrow(()-> new EntityNotFoundException("Invalid charge controller ID"));
    }
    public List<ChargeController> getControllers(){
        return repo.findAll();
    }
}
