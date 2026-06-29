package com.orientation.backend.users.application.commands.collaborators;

public record CreateCollaboratorCommand (
        String dni,
        String name,
        String surname,
        String email,
        String phone,
        boolean external,
        String organizationName
){
}
