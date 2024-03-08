package ca.cmput301t05.placeholder.qrcode;

/**
 * An enum for QR_Codes so we can easily check their type
 */
public enum QRCodeType {
    INFO, // Event information
    CHECK_IN, // Event check-in

    ERROR //if no qr is found
}