package com.masbytes.rbacapi.shared.infrastructure.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHashUtil {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Convierte un password en texto plano a su hash BCrypt.
     *
     * @param rawPassword contraseña en texto plano
     * @return hash BCrypt de la contraseña
     */
    public static String toHash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * Verifica si un password en texto plano coincide con un hash almacenado.
     *
     * @param rawPassword contraseña en texto plano
     * @param hash hash BCrypt almacenado
     * @return true si coincide, false en caso contrario
     */
    public static boolean matches(String rawPassword, String hash) {
        return encoder.matches(rawPassword, hash);
    }

    // Método main para pruebas rápidas
    public static void main(String[] args) {
        String raw = "admin123";
        String hash = toHash(raw);

        System.out.println("Password: " + raw);
        System.out.println("Hash: " + hash);
        System.out.println("Coincide? " + matches(raw, hash));
    }
}
