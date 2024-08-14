package registration.uz.hgpuserregistration.Order;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class OrderStatistic {
    private String year;
    private String month;
    private int number;

    @Override
    public String toString() {
        return "OrderStatistic{year='" + year + "', month='" + month + "', number=" + number + "}";
    }
}
