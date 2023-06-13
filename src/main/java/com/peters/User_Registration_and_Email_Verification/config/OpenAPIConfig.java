package com.peters.User_Registration_and_Email_Verification.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Peter",
                        email = "depitaztech@gmail.com",
                        url = "https://www.linkedin.com/in/peters-abraham-58161b247/"

                ),
                description = "OpenApi documentation for user management service",
                title = "Central Estore Service",
                version = "1.0"
        ),
        security = {
                @SecurityRequirement(name = "JWTAuth")
        }
)
@SecurityScheme(
        name = "JWTAuth",
        description = "JWT authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT"
)
public class OpenAPIConfig {
}
