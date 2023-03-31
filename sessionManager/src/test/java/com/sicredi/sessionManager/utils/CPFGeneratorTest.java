package com.sicredi.sessionManager.utils;

import org.junit.jupiter.api.Test;
import utils.CPFGenerator;

import static org.junit.jupiter.api.Assertions.*;

public class CPFGeneratorTest {

    @Test
    public void testGerarCpfValido() {
        String cpf = CPFGenerator.gerarCpfValido();
        assertTrue(isCpfValido(cpf), "CPF gerado deve ser válido");
    }

    @Test
    public void testFormatarCpf() {
        String cpf = "12345678901";
        String cpfFormatado = CPFGenerator.formatarCpf(cpf);
        assertEquals("123.456.789-01", cpfFormatado, "CPF formatado incorretamente");
    }

    @Test
    public void testFormatarCpfMalFormatado() {
        String cpfMalFormatado = "1234567890a";
        assertThrows(NumberFormatException.class, () -> CPFGenerator.formatarCpf(cpfMalFormatado), "Deve lançar NumberFormatException para CPF mal formatado");
    }

    private boolean isCpfValido(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }

        int primeiroDigito = CPFGenerator.calcularDigito(cpf.substring(0, 9), 10);
        int segundoDigito = CPFGenerator.calcularDigito(cpf.substring(0, 9) + primeiroDigito, 11);

        return cpf.equals(cpf.substring(0, 9) + primeiroDigito + segundoDigito);
    }

}

