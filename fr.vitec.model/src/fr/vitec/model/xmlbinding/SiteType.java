//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.22 at 09:18:42 AM CET 
//


package fr.vitec.model.xmlbinding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SiteType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SiteType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Cinemotions"/>
 *     &lt;enumeration value="AlloCine"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SiteType")
@XmlEnum
public enum SiteType {

    @XmlEnumValue("Cinemotions")
    CINEMOTIONS("Cinemotions"),
    @XmlEnumValue("AlloCine")
    ALLO_CINE("AlloCine");
    private final String value;

    SiteType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SiteType fromValue(String v) {
        for (SiteType c: SiteType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
