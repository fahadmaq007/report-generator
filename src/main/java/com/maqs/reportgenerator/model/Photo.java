package com.maqs.reportgenerator.model;

import fr.opensagres.xdocreport.template.annotations.FieldMetadata;
import fr.opensagres.xdocreport.template.annotations.ImageMetadata;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Photo {
    private byte[] bytes;
    private Double latitude;
    private Double longitude;
    private String url;

    @FieldMetadata( images = { @ImageMetadata( name = "photo" ) }, description="Photo"  )
    public byte[] getBytes() {
        return bytes;
    }
}
