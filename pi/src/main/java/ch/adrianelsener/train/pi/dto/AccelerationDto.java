package ch.adrianelsener.train.pi.dto;

import ch.adrianelsener.train.pi.dto.properties.Acceleration;
import ch.adrianelsener.train.pi.dto.properties.Device;
import ch.adrianelsener.train.pi.dto.properties.Direction;
import ch.adrianelsener.train.pi.dto.properties.Speed;

public class AccelerationDto extends AbstractDto {
    private final Acceleration acceleration;
    private final Speed speed;
    private final Direction direction;

    public AccelerationDto() {
        acceleration = new Acceleration();
        speed = new Speed();
        direction = Direction.STOP;
    }

    private AccelerationDto(AccelerationDto current, Acceleration acceleration) {
        this.acceleration = acceleration;
        speed = current.speed;
        direction = current.direction;
    }

    private AccelerationDto(final AccelerationDto current, final Speed speed) {
        acceleration = current.acceleration;
        this.speed = speed;
        direction = current.direction;
    }

    private AccelerationDto(final AccelerationDto current, final Direction direction) {
        acceleration = current.acceleration;
        speed = current.speed;
        this.direction = direction;
    }

    private AccelerationDto(final AccelerationDto current, final Device device) {
        acceleration = current.acceleration;
        speed = current.speed;
        direction = current.direction;
    }

    public AccelerationDto setAcceleration(Acceleration acceleration) {
        return new AccelerationDto(this, acceleration);
    }

    public AccelerationDto setAcceleration(final int accel, final int stepsize) {
        Acceleration acceleration = new Acceleration(accel, stepsize);
        return new AccelerationDto(this, acceleration);
    }

    public Acceleration getAcceleration() {
        return acceleration;
    }

    public AccelerationDto setSpeed(Speed speed) {
        return new AccelerationDto(this, speed);
    }

    public AccelerationDto setSpeed(int speed) {
        return new AccelerationDto(this, new Speed(speed));
    }

    public Speed getSpeed() {
        return speed;
    }

    public AccelerationDto setDirection(Direction direction) {
        return new AccelerationDto(this, direction);
    }

    public Direction getDirection() {
        return direction;
    }

    public AccelerationDto setDevice(final Device device) {
        return new AccelerationDto(this, device);
    }
}
