package carRental;

//inheritance
public class Car extends Vehicle {
 private boolean availability;

 public Car(int vehicleId, String model, String brand, boolean availability) {
     super(vehicleId, model, brand);
     this.availability = availability;
 }

 public boolean isAvailability() {
     return availability;
 }

 public void setAvailability(boolean availability) {
     this.availability = availability;
 }

 @Override
 public void printDetails() {
     System.out.println("Car Details:");
     System.out.println(this);
     System.out.println("Availability: " + (availability ? "Yes" : "No"));
 } //polymorphism

 @Override
 public String toString() {
     return super.toString();
 }//polymorphism
}
