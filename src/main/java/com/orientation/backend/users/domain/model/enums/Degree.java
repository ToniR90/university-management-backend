package com.orientation.backend.users.domain.model.enums;

public enum Degree {
    ELECTRONIC_ENGINEERING("Grau en Enginyeria Electrònica Industrial i Automàtica"),
    COMPUTER_ENGINEERING("Grau en Enginyeria Informàtica de Gestió i Sistemes d’Informació"),
    MECHANICAL_ENGINEERING("Grau en Enginyeria Mecànica"),
    INDUSTRIAL_ORGANIZATION("Grau en Enginyeria d’Organització Industrial"),
    AI_AND_ROBOTICS("Grau en Intel·ligència Artificial i Robòtica Aplicada"),
    DOUBLE_CS_VIDEOGAMES("Doble titulació en Enginyeria Informàtica i Disseny i Producció de Videojocs"),
    AUDIOVISUAL_MEDIA("Grau en Mitjans Audiovisuals"),
    VIDEOGAME_DESIGN("Grau en Disseny i Producció de Videojocs"),
    DOUBLE_ELECTRONIC_MECHANICAL("Simultaneïtat d’Enginyeria Electrònica i Mecànica"),
    DOUBLE_ELECTRONIC_CS("Simultaneïtat d’Enginyeria Electrònica i Informàtica"),
    DOUBLE_VIDEOGAMES_AUDIOVISUAL("Simultaneïtat de Disseny i Producció de Videojocs + Mitjans Audiovisuals"),
    BUSINESS_ADMINISTRATION("Grau en Administració d’Empreses i Gestió de la Innovació"),
    BUSINESS_ADMINISTRATION_EN("Grau en Administració d’Empreses i Gestió de la Innovació (docència en anglès)"),
    DIGITAL_MARKETING("Grau en Màrqueting i Comunitats Digitals"),
    MARITIME_LOGISTICS("Grau en Logística i Negocis Marítims"),
    DOUBLE_TOURISM_BUSINESS("Doble titulació en Turisme i Gestió de l’Oci i ADE i Gestió de la Innovació"),
    DOUBLE_BUSINESS_MARKETING("Doble titulació en ADE i Gestió de la Innovació i Màrqueting i Comunitats Digitals"),
    NURSING("Grau en Infermeria"),
    SPORTS_SCIENCE("Grau en Ciències de l’Activitat Física i de l’Esport (CAFE)"),
    PHYSIOTHERAPY("Grau en Fisioteràpia"),
    DOUBLE_PHYSIO_SPORTS("Doble titulació en Fisioteràpia i Ciències de l’Activitat Física i de l’Esport (CAFE)"),
    DOUBLE_TOURISM_MARKETING("Doble titulació en Turisme i Màrqueting"),
    NUTRITION("Grau en Nutrició Humana i Dietètica");

    private final String displayName;

    Degree (String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Degree fromString(String value) {
        try {
            return Degree.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Degree no vàlid: " + value);
        }
    }
}
