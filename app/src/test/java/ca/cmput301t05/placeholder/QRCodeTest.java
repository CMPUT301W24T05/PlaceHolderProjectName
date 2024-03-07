package ca.cmput301t05.placeholder;


import org.junit.Rule;
import org.junit.Test;

import ca.cmput301t05.placeholder.database.DatabaseManager;
import ca.cmput301t05.placeholder.events.Event;
import ca.cmput301t05.placeholder.events.QRCode;
import ca.cmput301t05.placeholder.events.QRCodeManager;

public class QRCodeTest {

    @Rule
    QRCodeManager QRCM = new QRCodeManager();

    @Test
    public void generatorTest(){
        Event event = new Event("exampleEvent", "this is my event info", 100);
        QRCode infoqr = QRCM.generateQRCode(event, "eventinfo");




    }
}
