package ru.belogurow.socialnetworkclient.chat.model;

import java.util.Arrays;
import java.util.List;

public enum FileType {
    JPG,
    STL,
    PDF,
    DICOM,
    DOC,
    TXT,
    OTHER;

    public static List<String> getTypes() {
        return Arrays.asList(
                JPG.toString(),
                STL.toString(),
                PDF.toString(),
                DICOM.toString(),
                DOC.toString(),
                TXT.toString(),
                OTHER.toString()
                );
    }
}
