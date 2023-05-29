package mytaxi.krutyporokh.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mytaxi.partola.models.Car;
import mytaxi.partola.models.CustomUser;
import mytaxi.partola.models.Driver;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDriverCarForm {
    private CustomUser customUser;
    private Driver driver;
    private Car car;

}
