import org.firmata4j.ssd1306.MonochromeCanvas;
import org.firmata4j.ssd1306.SSD1306;
import org.firmata4j.Pin;

import java.util.TimerTask;

public class senseMoisture extends TimerTask {
    private Pin pin1;
    private Pin pin2;
    private SSD1306 myOLED;

    public senseMoisture(SSD1306 myOLED, Pin pin1, Pin pin2) {
        this.myOLED = myOLED;
        this.pin1 = pin1;
        this.pin2 = pin2;
    }

    @Override
    public void run() {
        double[] moistureValues = new double[5];

        for(int i=0; i<= moistureValues.length-2; i++) {
            moistureValues[i] = moistureValues[i + 1];
        }

        moistureValues[moistureValues.length-1] = pin1.getValue();

        double sum = 0.0;
        for(int i=0; i<= moistureValues.length-1; i++) {
            sum = sum + moistureValues[i];
        }

        double average = sum / moistureValues.length;

        double moistureLevel = (-1.0/60.0) * average + 2.5;
        int lineValue = (32/15)*(int)average - (192);

        if(moistureLevel <= 0.3) {
            System.out.println("Soil is dry!");
            try {
                myOLED.clear();
                myOLED.getCanvas().setTextsize(2);
                myOLED.getCanvas().drawString(0, 0, "Moisture: " + String.format("%.1f", moistureLevel) + " (Dry)" + "\nDryness:");
                myOLED.getCanvas().drawHorizontalLine(0, 55, lineValue, MonochromeCanvas.Color.BRIGHT);
                myOLED.display();

                pin2.setValue(1);
                System.out.println("Pumping!");
                Thread.sleep(4000);
                pin2.setValue(0);
                System.out.println("Pumping stopped!");
                Thread.sleep(5000);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        } else if(moistureLevel>0.3 && moistureLevel <= 0.6) {
            System.out.println("Soil is wet, but not completely saturated!");
            try {
                myOLED.clear();
                myOLED.getCanvas().setTextsize(2);
                myOLED.getCanvas().drawString(0, 0, "Moisture: " + String.format("%.1f", moistureLevel) + " (Damp)" + "\nDryness:");
                myOLED.getCanvas().drawHorizontalLine(0, 55, lineValue, MonochromeCanvas.Color.BRIGHT);
                myOLED.display();

                pin2.setValue(1);
                System.out.println("Pumping!");
                Thread.sleep(2000);
                pin2.setValue(0);
                System.out.println("Pumping stopped!");
                Thread.sleep(5000);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                System.out.println("Soil sufficiently wet");
                myOLED.clear();
                myOLED.getCanvas().setTextsize(2);
                myOLED.getCanvas().drawString(0, 0, "Moisture: " + String.format("%.1f", moistureLevel) + " (Wet)" + "\nDryness:");
                myOLED.getCanvas().drawHorizontalLine(0, 55, lineValue, MonochromeCanvas.Color.BRIGHT);
                myOLED.display();
                Thread.sleep(5000);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
