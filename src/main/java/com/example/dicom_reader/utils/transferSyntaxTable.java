package com.example.dicom_reader.utils;

public class transferSyntaxTable {
    //url:https://blog.csdn.net/u014738683/article/details/54573611
    //url:https://www.dicomlibrary.com/dicom/transfer-syntax/

    //Implicit VR Little Endian
    public static final String UID_0 = "1.2.840.10008.1.2";
    public static final String UID_1 = "1.2.840.10008.1.2.1";
    public static final String UID_2 = "1.2.840.10008.1.2.1.99";
    public static final String UID_3 = "1.2.840.10008.1.2.2";
    //JPEG Lossless
    public static final String UID_4 = "1.2.840.10008.1.2.4.70";
    public static final String UID_5 = "1.2.840.10008.1.2.4.90";
    public static final String UID_6 = "1.2.840.10008.1.2.5";
    //Default Transfer Syntax for Lossy
    public static final String UID_7 = "1.2.840.10008.1.2.4.50";
    public static final String UID_8 = "1.2.840.10008.1.2.4.51";
    public static final String UID_9 = "1.2.840.10008.1.2.4.100";
    public static final String UID_10 = "1.2.840.10008.1.2";

    public void infoUID0(){
        System.out.println("未压缩，隐式VR小端");
    }

    public void infoUID5(){
        System.out.println("无损压缩，JPEG2000");
    }
}
