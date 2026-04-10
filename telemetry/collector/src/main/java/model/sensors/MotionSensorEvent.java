package model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.sensors.abstractModel.SensorEvent;
import model.sensors.abstractModel.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {
//    качество связи
    private int linkQuality;
//    наличие/отсутствие движения
    private Boolean motion;
//    напряжение
    private int voltage;

    @Override
    public SensorEventType getType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
} 