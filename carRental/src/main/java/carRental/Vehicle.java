package carRental;

//abstraction
public abstract class Vehicle {
 private int vehicleId;
 private String model;
 private String brand;

 public Vehicle(int vehicleId, String model, String brand) {
     this.vehicleId = vehicleId;
     this.model = model;
     this.brand = brand;
 }

 public int getVehicleId() {
     return vehicleId;
 }

 public String getModel() {
     return model;
 }

 public String getBrand() {
     return brand;
 }

 // Abstract method to be overridden by subclasses
 public abstract void printDetails();

 @Override
 public String toString() {
     return "Vehicle ID: " + vehicleId + "\nModel: " + model + "\nBrand: " + brand;
 }
}
