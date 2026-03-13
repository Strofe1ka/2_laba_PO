package com.example.demo.service;

import com.example.demo.entity.Car;
import com.example.demo.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car createCar(Car car) {
        car.setAvailable(true);
        return carRepository.save(car);
    }

    public List<Car> getAllCars(Boolean available) {
        if (available != null && available) {
            return carRepository.findByAvailableTrue();
        }
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByAvailableTrue();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Optional<Car> updateCar(Long id, Car updatedCar) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setBrand(updatedCar.getBrand());
                    car.setModel(updatedCar.getModel());
                    car.setPlateNumber(updatedCar.getPlateNumber());
                    car.setAvailable(updatedCar.isAvailable());
                    return carRepository.save(car);
                });
    }

    public boolean deleteCar(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
