package model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.hub.abstractModel.DeviceType;
import model.hub.abstractModel.HubEvent;

@Getter
@Setter
@ToString(callSuper = true)
public class DeviceAddedEvent extends HubEvent {
    private String id;
    private DeviceType deviceType;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_ADDED;
    }
}
