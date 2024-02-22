package ca.cmput301t05.placeholder.events;

public class QRCodeManager {


    public QRCodeManager(){

    }

    public QRCode generateQRCode (Event event){
        return new QRCode(event);
    }

    /*
    public Boolean checkType(QRCode qr){
        if (qr.getType()){
            return true;
        }
        else{
            return false;
        }
    }
     */


}
