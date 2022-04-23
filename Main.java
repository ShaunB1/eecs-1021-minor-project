import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.ssd1306.SSD1306;
import org.firmata4j.I2CDevice;

import java.util.Timer;

public class Main {
    static final byte I2C0 = 0x3c;
    public static void main(String[] args) {
        String myPort = "COM6";
        IODevice myBoard = new FirmataDevice(myPort);

        try {
            myBoard.start();
            myBoard.ensureInitializationIsDone();

            I2CDevice i2cObject = myBoard.getI2CDevice(I2C0);
            SSD1306 myOLED = new SSD1306(i2cObject, SSD1306.Size.SSD1306_128_64);
            myOLED.init();

            Pin moistureSensor = myBoard.getPin(15);

            Pin waterPump = myBoard.getPin(2);
            waterPump.setMode(Pin.Mode.OUTPUT);

            Timer myTimer = new Timer();

            var task = new senseMoisture(myOLED, moistureSensor, waterPump);

            myTimer.schedule(task, 0, 1000);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
