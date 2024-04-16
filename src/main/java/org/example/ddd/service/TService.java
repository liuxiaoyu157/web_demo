package org.example.ddd.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.xmlbeans.XmlException;

import java.io.IOException;

public interface TService {
    String readWord1() throws IOException;


    String readPhoto() throws IOException;

    String readExcel() throws IOException;

    String readWord2() throws IOException, InvalidFormatException;

    String readWord3() throws IOException, XmlException;
}
