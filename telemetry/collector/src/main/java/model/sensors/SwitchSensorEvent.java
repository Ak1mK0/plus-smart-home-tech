package model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.sensors.abstractModel.SensorEvent;
import model.sensors.abstractModel.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class SwitchSensorEvent extends SensorEvent {
//    текущее состояние переключателя. true - включен, false - выключен.
    private Boolean state;

    @Override
    public SensorEventType getType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
} 